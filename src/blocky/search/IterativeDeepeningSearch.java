package blocky.search;

import blocky.*;

/**
 * Implements Iterative Deepening Search
 * 
 * @author Matthew Consterdine
 */
public class IterativeDeepeningSearch extends Search {
	@Override
	protected Node search(Node node, State goal, boolean simple) {
		for(int i = 0; true; i++) {
			node.contract(); // Reset between runs
			Node result = new DepthFirstSearch().search(node, goal, i, simple);
			if(result != null) return result;
		}
    }
}
