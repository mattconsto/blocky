package blocky.search;

import java.util.PriorityQueue;

import blocky.*;

/**
 * Implements A* Search
 * 
 * @author Matthew Consterdine
 */
public class AStarSearch extends Search implements Heuristic {
	@Override
	protected Node search(Node node, State goal, boolean simple) {
		PriorityQueue<Node> queue = new PriorityQueue<Node>() {{add(node);}};
		
		while(!queue.isEmpty()) {
			if(queue.peek().state.equals(goal))
				return queue.peek();
			for(Node child : queue.poll().expand(this, goal, simple))
				queue.add(child);
		}
		
		return null;
	}

	@Override
	public double score(State current, State goal) {
		int score = 0;
		
		for(int y = 0; y < current.map.length; y++) {
			for(int x = 0; x < current.map[y].length; x++) {
				int min = Integer.MAX_VALUE;
				for(int k = 0; k < goal.map.length; k++) {
					for(int j = 0; j < goal.map[k].length; j++) {
						if(current.map[y][x] == goal.map[k][j])
							min = Math.min(min, Math.abs(j-x) + Math.abs(k-y));
					}
				}
				score += min;
			}
		}
		
		// Divide by 2 so the heuristic is admissible, swapping moves 2 tiles.
		return score / 2.0;
	}
}
