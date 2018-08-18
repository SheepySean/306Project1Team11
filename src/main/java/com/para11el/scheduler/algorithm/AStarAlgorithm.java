package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.para11el.scheduler.ui.ViewerPaneController;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * A* Algorithm to find the optimal solution. 
 * 
 * @author Jessica Alcantara, Holly Hagenson
 */
public class AStarAlgorithm implements Algorithm{

	/**
	 * Comparator to sort states according to the results of the cost
	 * function.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @author Jessica Alcantara
	 */
	private Comparator<State> _stateComparator = new Comparator<State>() {
		@Override
		public int compare(State o1, State o2) {
			return o1.compareTo(o2);
		}
	};

	private Graph _graph;
	private int _processors;
	private ForkJoinPool _fjp;
	private NodeManager _nm;
	private CostFunctionManager _cfm;
	private Queue<State> _states = new PriorityQueue<State>(_stateComparator);

	public AStarAlgorithm() {}

	/**
	 * Constructor for Algorithm for sequential solution
	 * @param graph Input graph
	 * @param processor Number of processors
	 *
	 * @author Jessica Alcantara
	 */
	public AStarAlgorithm(Graph graph, int processor) {
		_graph = graph;
		_processors = processor;
		_fjp = new ForkJoinPool(1);
		initialise();
	}

	/**
	 * Constructor for Algorithm for parallel solution
	 * @param graph Input graph
	 * @param processor Number of processors
	 * @param cores Number of cores
	 *
	 * @author Jessica Alcantara
	 */
	public AStarAlgorithm(Graph graph, int processor, int cores) {
		_graph = graph;
		_processors = processor;
		_fjp = new ForkJoinPool(cores); 
		initialise();
	}


    /**
     * Initialises the A* algorithm and managers used in the algorithm
     */
    public void initialise() {
        ViewerPaneController.setStatus("Started running the A* algorithm");
        _nm = new NodeManager(_graph);
        _cfm = new CostFunctionManager(_nm, calculateTotalWeight(_graph.nodes()), _processors);

        // Initialize priority queue with entry node initial states
        _graph.nodes().forEach((node) -> {
            if (node.getInDegree() == 0) {
                ArrayList<Task> schedule = new ArrayList<Task>();
                schedule.add(new Task(node,0,1));
                _states.add(new State(node, null, schedule,
                        _cfm.calculateCostFunction(null, node, schedule)));
            }
        });
    }

	/**
	 * Build the solution using the A* algorithm
	 * @return Final optimal solution
	 *
	 * @author Jessica Alcantara
	 */
	public ArrayList<Task> buildSolution() {
		while (_states.size() > 0) {
			// Pop the most promising state
			State state = _states.poll();
			ViewerPaneController.setStatus("Using A* to expand states in the schedule");
			ViewerPaneController.getInstance().setSchedule(state.getSchedule());
			if(ViewerPaneController.isRunning()) {
				ViewerPaneController.update();
			}
/*			try{
				TimeUnit.MILLISECONDS.sleep(100);
			} catch(Exception e) {}*/
			// Check if solution is complete
			if (state.isComplete(_graph.nodes())) {
				ViewerPaneController.toggleTimer(false);
				return state.getSchedule();
			}

			expandState(state);
		}
		return null;
	}

	/**
	 * Expands the state by finding all possible states from the free nodes
	 * @param state Parent state
	 * 
	 * @author Jessica Alcantara
	 */
	public void expandState(State state) {
		ArrayList<Node> freeNodes = getAvailableNodes(state.getSchedule());
		
		List<State> newStates = new ArrayList<State>();
		for (Node node : freeNodes) {
			for (int i=1; i<=_processors; i++) {
				// Parallelize the building of states
				AStarStateTask aStarStateTask = new AStarStateTask(state,node,i,_cfm);
				State newState = _fjp.invoke(aStarStateTask);
				newStates.add(newState);
			}
		}
		
		// Prune duplicate states
		for (State s : newStates) {
			PruningManager pm = new PruningManager(s,_states);
			Boolean duplicate = _fjp.invoke(pm);
			if (!duplicate){
				_states.add(s);
			}
		}
	}

	/**
	 * Find the available nodes that can be scheduled given what nodes have already
	 * been scheduled
	 * @param scheduledTasks
	 * @return ArrayList of available nodes
	 *
	 * @author Rebekah Berriman, Tina Chen
	 */
	public ArrayList<Node> getAvailableNodes(ArrayList<Task> scheduledTasks) {
		ArrayList<Node> scheduledNodes =  new ArrayList<Node>();
		ArrayList<Node> available = new ArrayList<Node>();

		// Get list of scheduled nodes
		for (Task task : scheduledTasks) {
			scheduledNodes.add(task.getNode());
		}

		_graph.nodes().forEach((node) -> {
			// Find nodes that have not been scheduled
			if (!scheduledNodes.contains(node)) {
				ArrayList<Node> parents = getParents(node);

				// Add nodes with no parents to available nodes
				if (parents.size() == 0) {
					available.add(node);
				// Check all the parents of the node have been scheduled
				} else {
					boolean availableNode = true;
					for (Node parentNode : parents) {
						// Node is not available if parent has not been scheduled
						if (!scheduledNodes.contains(parentNode)) {
							availableNode = false;
						}
					}
					if (availableNode) {
						available.add(node);
					}
				}
			}
		});

		return available;
	}

	/**
	 * Returns a list of parent nodes of a node
	 * @param node Node to find parents of
	 *
	 * @author Tina Chen
	 */
	public ArrayList<Node> getParents(Node node) {
		ArrayList<Node> parents = new ArrayList<Node>();
		node.enteringEdges().forEach((edge) -> {
			parents.add(edge.getSourceNode());
		});

		return parents;
	}

	/**
	 * Calculates the sum of weights of all nodes in the graph
	 * @param nodes Collection of nodes in the graph
	 * @return int total of node weights
	 * 
	 * @author Jessica Alcantara
	 */
	public int calculateTotalWeight(Stream<Node> nodes) {
		int weightTotal = 0;
		Iterator<Node> i = nodes.iterator();
		while (i.hasNext()){
			weightTotal += ((Number)i.next().getAttribute("Weight")).intValue();
		}

		return weightTotal;
	}

	/**
	 * Returns the comparator used to order the states in the priority queue
	 * @return Comparator of states
	 * 
	 * @author Jessica Alcantara
	 */
	public Comparator<State> getStateComparator() {
		return _stateComparator;
	}

	/**
	 * Returns the states stored in the priority queue
	 * @return Queue of states
	 * 
	 * @author Jessica Alcantara
	 */
	public Queue<State> getStates() {
		return _states;
	}

	/**
	 * Returns and labels the graph with the startTime and processor numbers of each of the
	 * nodes for the optimal solution
	 * @param solution Final solution
	 * @return graph of the nodes with labels
	 *
	 * @author Rebekah Berriman
	 */
	public Graph getGraph(ArrayList<Task> solution) {
		for (Task task : solution) {
			Node node = task.getNode();
			node.setAttribute("Start", task.getStartTime());
			node.setAttribute("Processor", task.getProcessor());
		}
		return _graph;
	}
}
