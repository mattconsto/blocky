package blocky;

import java.util.ArrayList;
import java.util.List;

import blocky.search.Heuristic;

/**
 * Node
 * 
 * @author Matthew Consterdine
 */
public class Node implements Comparable<Node> {
	public    static long   nodeTime     = 0;
	public    static long   nodeSpaceNow = 0;
	public    static long   nodeSpaceMax = 0;
	public    static long   nodeLimit    = 1_000_000; // Arbitrary value
	
	public    final  Node   parent;
	public    final  int    depth;
	public    final  double cost;
	public    final  State  state;

	protected        List<Node> children     = null;

	public Node(Node parent, String state) {
		this(parent, State.parseString(state));
	}
	

	public Node(Node parent, State state) {
		this(parent, state, 0);
	}
	
	public Node(Node parent, State state, double newCost) {
		if(state == null) throw new RuntimeException("Invalid state given");
		
		this.parent = parent;
		this.depth  = parent != null ? parent.depth + 1 : 0;
		this.cost   = newCost;
		this.state  = state;
	}
	
	/**
	 * Lazily get an array of this node's children
	 * @param simple Allow us to loop back and forth.
	 * @return The children
	 */
	public List<Node> expand(boolean simple) {return expand(null, null, simple);}
	
	/**
	 * Lazily get an array of this node's children using a heuristic to estimate
	 * the cost.
	 * @param heuristic The heuristic
	 * @param goal Our goal
	 * @param simple Allow us to loop back and forth.
	 * @return The children
	 */
	public List<Node> expand(Heuristic heuristic, State goal, boolean simple) {
		// Check if we need to look for children.
		if(children == null) {
			++nodeTime;
			++nodeSpaceNow;
			nodeSpaceMax = Math.max(nodeSpaceNow, nodeSpaceMax);
			if(nodeSpaceMax > nodeLimit)
				throw new RuntimeException("Reached node limit!");
			
			children = new ArrayList<Node>();
			
			// Loop over each valid position
			for(int i = 0, x = 0, y = -1; i < 4; i++, x = (2*i+1)%3-1, y = (2*i+1)/3-1) {
				Position2D now = state.pos.add(x, y);
				if((simple || parent == null || !parent.state.pos.equals(now)) && state.isPositionInMap(now)) {
					State  newState = state.swap(x, y);
					double newCost  = depth;
					if(heuristic != null && goal != null)
						newCost += heuristic.score(newState, goal);
					
					children.add(new Node(this, newState, newCost));
				}
			}
			
			// Backup if we reach a dead end
			//if(children.size() == 0 && parent != null) children.add(parent);
		}
		
		return children;
	}
	
	/**
	 * Free up memory
	 */
	public Node contract() {
		if(children != null) {
			--nodeSpaceNow;
			for(Node child : children)
				child.contract();
		}

		children = null;
		try {finalize();} catch (Throwable ignored) {}
		
		return this;
	}
	
	/**
	 * Prune a tree until we only have a path
	 * @param caller The calling node, use null to start.
	 */
	public Node prune(Node caller) {
		if(children != null)
			for(Node child : children)
				if(child != caller) child.contract();

		if(parent != null) parent.prune(this);
		
		return this;
	}

	@Override
	public int compareTo(Node other) {
		return (int) (this.cost - other.cost);
	}
}
