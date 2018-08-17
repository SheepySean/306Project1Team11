package com.para11el.scheduler.algorithm;

import java.util.concurrent.RecursiveAction;

import org.graphstream.graph.Graph;

public class DFSForkJoin extends RecursiveAction {
	
	protected Graph _graph;
	protected int _processors;
	protected int _cores;
	
	public DFSForkJoin(Graph graph, int processors, int cores) {
		_graph = graph;
		_processors = processors;
		_cores = cores;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		
	}

}
