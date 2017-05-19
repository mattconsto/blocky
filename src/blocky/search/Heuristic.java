package blocky.search;

import blocky.State;

/**
 * Heuristic Interface
 * 
 * @author Matthew Consterdine
 */
public interface Heuristic {
	/**
	 * Estimate the distance between 2 states
	 * @param node The current state
	 * @param goal The goal state
	 * @return The distance
	 */
	public double score(State node, State goal);
}
