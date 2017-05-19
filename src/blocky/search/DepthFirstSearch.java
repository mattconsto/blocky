package blocky.search;

import java.util.Collections;
import java.util.Stack;

import blocky.*;

/**
 * Implements Depth First Search
 * 
 * @author Matthew Consterdine
 */
public class DepthFirstSearch extends Search {
	@Override
	protected Node search(Node node, State goal, boolean simple) {
		return search(node, goal, -1, simple);
    }
	
	/**
	 * Depth Limited Search
	 * @param node The starting Node
	 * @param goal Our goal
	 * @param limit The limit (Negative means no limit)
	 * @param simple Tree search or Graph Search?
	 * @return Either a Node with our goal state, or null if not found
	 */
    public Node search(Node node, State goal, int limit, boolean simple) {
		Stack<Node> stack = new Stack<Node>() {{add(node);}};
		
		while(!stack.isEmpty()) {
			Node current = stack.pop();
			
			if(current.state.equals(goal))
				return current;
			
			Collections.shuffle(current.expand(simple));
			
			int added = 0;
			for(Node child : current.expand(simple)) {
				if(limit < 0 || child.depth < limit) {
					stack.add(child);
					++added;
				}
			}
			
			if(added == 0)
				current.contract();
		}
		
		return null;
    }
}
