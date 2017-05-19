package blocky.search;

import blocky.Node;
import blocky.State;

/**
 * Abstract Search
 * 
 * @author Matthew Consterdine
 */
public abstract class Search {
	/**
	 * Look through a tree or child nodes, for a set goal
	 * @param node The node
	 * @param goal Our goal state
	 * @param simple Tree search or Graph Search?
	 * @return Either a Node with our goal state, or null if not found
	 */
	public Node run(Node node, State goal, boolean simple) {
		// Reset between runs
		Node.nodeTime     = 1;
		Node.nodeSpaceNow = 1;
		Node.nodeSpaceMax = 1;
		
		return node == null || goal == null ||
			   node.state.equals(goal) ? node : search(node, goal, simple);
	}
	
	/**
	 * Protected searcher
	 * @param node The node
	 * @param goal Our goal state
	 * @param simple Tree search or Graph Search?
	 * @return Either a Node with our goal state, or null if not found
	 */
	protected abstract Node search(Node node, State goal, boolean simple);
}
