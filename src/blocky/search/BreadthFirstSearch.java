package blocky.search;

import java.util.LinkedList;
import java.util.Queue;

import blocky.*;

/**
 * Implements Breadth First Search
 * 
 * @author Matthew Consterdine
 */
public class BreadthFirstSearch extends Search {
	@Override
	protected Node search(Node node, State goal, boolean simple) {
		Queue<Node> queue = new LinkedList<Node>() {{add(node);}};
		
		while(!queue.isEmpty()) {
			if(queue.peek().state.equals(goal))
				return queue.peek();
			for(Node child : queue.poll().expand(simple))
				queue.add(child);
		}
		
		return null;
    }
}
