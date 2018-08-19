package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import com.para11el.scheduler.ui.ViewerPaneController;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * A* Algorithm to find the optimal solution. 
 * 
 * @author Jessica Alcantara, Holly Hagenson
 */
public class AStarAlgorithm implements Algorithm, Traversable {
	/**
	 * Comparator to sort states according to the results of the cost
	 * function.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
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
			
			ViewerPaneController.getInstance();
			if (!ViewerPaneController.getTimeout()) {
				// Pop the most promising state
				State state = _states.poll();
				state.getNode().setAttribute("ui.class", "promising");
				ViewerPaneController.setStatus("Using A* to expand states in the schedule");
				ViewerPaneController.getInstance().setSchedule(state.getSchedule());
				if(ViewerPaneController.isRunning()) {
					ViewerPaneController.update(); // Update the schedule view
				}
				// Check if solution is complete
				if (state.isComplete(_graph.nodes())) {
					ViewerPaneController.toggleTimer(false);
					ViewerPaneController.setLabelFinish();
					return state.getSchedule();
				}
				
				expandState(state);
				
			} else {				
				break;
			}	
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
			node.edges().forEach(edge -> {
				edge.setAttribute("ui.class", "free");
			});
			node.setAttribute("ui.class", "free");
		}
		
		// Prune duplicate states
		for (State s : newStates) {
			PruningManager pm = new PruningManager(s,_states);
			Boolean duplicate = _fjp.invoke(pm);
			if (!duplicate){
				_states.add(s);
			}

		}
		state.getNode().edges().forEach(edge -> {
			edge.removeAttribute("ui.class");
		});
		state.getNode().removeAttribute("ui.class");
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
	 * Returns the graph representing scheduling problem
	 * @return GraphStream graph
	 * 
	 * @author Sean Oldfield
	 */
	public Graph getGSGraph() {
		return _graph;
	}
}
