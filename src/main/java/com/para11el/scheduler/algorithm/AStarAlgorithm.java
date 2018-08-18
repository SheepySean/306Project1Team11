package com.para11el.scheduler.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * A* Algorithm to find the optimal solution. 
 * 
 * @author Jessica Alcantara, Holly Hagenson
 */
public class AStarAlgorithm extends Algorithm{

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

	private Queue<State> _states = new PriorityQueue<State>(_stateComparator);
	private NodeManager _nm;
	private CostFunctionManager _cfm;
	private PruningManager _pm;
	private ForkJoinPool _fjp;
	private int _cores;

	public AStarAlgorithm() {
		super();
	}

	public AStarAlgorithm(Graph graph, int processor) {
		super(graph, processor);
		_nm = new NodeManager(graph);
		_cfm = new CostFunctionManager(_nm, calculateTotalWeight(graph.nodes()), processor);
		_pm = new PruningManager();
		_fjp = new ForkJoinPool(1);
		_cores = 1;
	}

	public AStarAlgorithm(Graph graph, int processor, int cores) {
		super(graph, processor, cores);
		_nm = new NodeManager(graph);
		_cfm = new CostFunctionManager(_nm, calculateTotalWeight(graph.nodes()), processor);
		_pm = new PruningManager();
		_fjp = new ForkJoinPool(cores); 
		_cores = cores;
	}

	/**
	 * Initializes the algorithm and build solution
	 * @return Final optimal solution
	 * 
	 * @author Jessica Alcantara
	 */
	public ArrayList<Task> buildSolution() {
		// Initialize priority queue with entry node initial states
		_graph.nodes().forEach((node) -> {
			if (node.getInDegree() == 0) {
				ArrayList<Task> schedule = new ArrayList<Task>();
				schedule.add(new Task(node,0,1));
				_states.add(new State(node, null, schedule,
						_cfm.calculateCostFunction(null, node, schedule)));
			}
		});

		// A* Algorithm
		while (_states.size() > 0) {
			// Pop the most promising state
			State state = _states.poll();

			// Check if solution is complete
			if (state.isComplete(_graph.nodes())) {
				return state.getSchedule();
			}

			// Expand state into children
			try {
				expandState(state);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Expands the state by finding all possible states from the free nodes
	 * @param state Parent state
	 * 
	 * @author Jessica Alcantara
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void expandState(State state) throws InterruptedException, ExecutionException {
		ArrayList<Node> freeNodes = availableNode(state.getSchedule());

		List<Callable<State>> tasks = new ArrayList<Callable<State>>();


		for (Node node : freeNodes) {
			for (int i=1; i<_processors; i++) {
				int processor = i;
				Callable<State> c = new Callable<State>() {
					@Override
					public State call() throws Exception {
						return compute(state,node,processor);
					}
				};
				tasks.add(c);
			}
		}
		
		ExecutorService exec = Executors.newFixedThreadPool(_cores);
			List<Future<State>> results = exec.invokeAll(tasks);
	        for (Future<State> fr : results) {
	        	State newState = fr.get();
	        	if (newState != null) {
	        		_states.add(newState);
	        	}
	        }
			exec.shutdown();
	}

	public State compute(State state, Node freeNode, int processor) {
		ArrayList<Task> schedule = new ArrayList<Task>(state.getSchedule());

		// Schedule the node
		int startTime = getEarliestStartTime(freeNode,schedule,processor);
		schedule.add(new Task(freeNode,startTime,processor));

		// Create state from new schedule
		State newState = new State(freeNode,state,schedule,
				_cfm.calculateCostFunction(state,freeNode,schedule));

		// Prune duplicate states
		if (!_pm.doPrune(newState, _states)){
			return newState;
		}
		
		return null;
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
}
