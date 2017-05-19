package blocky;


import java.util.ArrayList;

import blocky.search.*;

/**
 * Simulation.
 * If you are having issue seeing the Agent, you might need to change encoding
 * Eclipse > Preferences > General > Workspace > Text file encoding
 * 
 * @author Matthew Consterdine
 */
public class Simulation {
	public void runPathFinder() {
		// Get A* Search
		System.out.println(pathToString(simulate(
			new AStarSearch(),
			false,
			State.parseString("    \n A  \n B  \n C ☺\n"),
			State.parseString("    \n    \n    \nABC☺\n"),
			1
		).result[0]));
	}
	
	public void runArrowFinder() {
		// Get depth first search
		// Try catch due to depth first not always finishing
		while(true) {
			try {
				printArrows(simulate(
					new DepthFirstSearch(),
					false,
					State.parseString("    \n    \n    \nABC☺\n"),
					State.parseString("    \n A  \n B  \n C ☺\n"),
					1
				).result[0]);
				System.exit(1);
			} catch (Exception ignored) {}
		}
	}
	
	public void runCSV() {
		Search[] searches = new Search[] {
			new AStarSearch(),
			new DepthFirstSearch(),
			new BreadthFirstSearch(),
			new IterativeDeepeningSearch(),
		};
		
		System.out.println("Difficulty\t\tA*\tA* Graph\tDepth\tDepth Graph\tBreadth\tBreadth Graph\tDeepening\tDeepening Grapth\t\tA*\tA* Graph\tDepth\tDepth Graph\tBreadth\tBreadth Graph\tDeepening\tDeepening Grapth");
		
		for(int mutation = 0; mutation < 2000; mutation++) {
			for(int puzzles = 0; puzzles < 3; puzzles++) {
				State start = State.parseString("A   \n B  \n  C \n   ☺\n");
				State goal  = start.mutate(mutation);
				
				ArrayList<Result> results = new ArrayList<>();
				
				for(Search search : searches)
					for(int i = 0; i < 2; i++)
						results.add(simulate(search, i == 0, start, goal, 3));
				
				if(results.size() > 0 && results.get(0).result.length > 0) {
					System.out.print(results.get(0).result[0].depth + "\t");
					
//					// Get the output of a specified length
//					if(results.get(0).result[0].depth == 16) {
//						System.out.println(pathToString(results.get(0).result[0]));
//						
//						System.out.println(pathToString(run(
//							new AStarSearch(),
//							false,
//							results.get(0).result[0].state,
//							State.parseString("A   \n B  \n  C \n   ☺\n"),
//							1
//						).result[0]));
//						
//						System.exit(1);;
//					}
					
					for(Result result : results)
						System.out.print("\t" + result.time);
					
					System.out.print("\t");

					for(Result result : results)
						System.out.print("\t" + result.space);
					
					System.out.println();
				}
			}
		}
	}
	
	/**
	 * Perform a search a number of times
	 * @param search The search we are using
	 * @param simple Simple search?
	 * @param start Start state
	 * @param goal Goal state
	 * @param repeats Number of repetitions
	 * @return A result
	 */
	public Result simulate(Search search, boolean simple, State start, State goal, int repeats) {
		ArrayList<Node> results = new ArrayList<>();
		ArrayList<Long> times   = new ArrayList<>();
		ArrayList<Long> spaces  = new ArrayList<>();
		
		for(int i = 0; i < repeats; i++) {
			try {
				System.gc();
				results.add(search.run(new Node(null, start), goal, simple).prune(null));
				times.add(Node.nodeTime);
				spaces.add(Node.nodeSpaceMax);
			} catch (Throwable ignored) {}
		}
		
		// Compute averages
		if(results.size() > 0) {
			return new Result(
				results.toArray(new Node[] {}),
				times.stream().mapToLong(e->e).average().getAsDouble(),
				spaces.stream().mapToLong(e->e).average().getAsDouble()
			);
		} else {
			return new Result(
				results.toArray(new Node[] {}),
				-1,
				-1
			);
		}
	}
	
	/**
	 * Convert a path to a string
	 * @param node Goal node
	 * @return A string of the nodes path
	 */
	public String pathToString(Node node) {
		if(node == null) return "invalid node";

		StringBuilder[] builder = new StringBuilder[] {
			new StringBuilder(), new StringBuilder(),
			new StringBuilder(), new StringBuilder(),
			new StringBuilder()
		};
		
		// Fill the path
		Node[] path = new Node[node.depth + 1];
		while (node != null) {
			path[node.depth] = node;
			node = node.parent;
		}
		
		{
			builder[0].append("Start\t");
			String[] lines = path[0].state.toString().split("\\n");
			for(int j = 0; j < lines.length; j++)
				builder[j+1].append(lines[j]).append("\t");
		}
		
		// Display
		Position2D last = path[0].state.pos;
		for(int i = 1; i < path.length; i++) {
			int Δx = (int) Math.signum(path[i].state.pos.x - last.x);
			int Δy = (int) Math.signum(path[i].state.pos.y - last.y);
			
			builder[0].append(Δx == 1 ? "→" : (Δx == -1 ? "←" : (Δy == 1 ? "↓" : (Δy == -1 ? "↑" : "↻")))).append("\t");
			
			String[] lines = path[i].state.toString().split("\\n");
			
			for(int j = 0; j < lines.length; j++)
				builder[j+1].append(lines[j]).append("\t");
			
			last = path[i].state.pos;
		}
		
		return builder[0].toString() + "\n" + builder[1].toString() + "\n" + 
		       builder[2].toString() + "\n" + builder[3].toString() + "\n" + 
		       builder[4].toString(); 
	}
	
	/**
	 * Print arrows indicating the path of a node.
	 * @param node Goal node
	 */
	public void printArrows(Node node) {
		if(node == null) System.err.println("invalid node");
		
		// Fill the path
		Node[] path = new Node[node.depth + 1];
		while (node != null) {
			path[node.depth] = node;
			node = node.parent;
		}
		
		// Display
		Position2D last = path[0].state.pos;
		for(int i = 1; i < path.length; i++) {
			int Δx = (int) Math.signum(path[i].state.pos.x - last.x);
			int Δy = (int) Math.signum(path[i].state.pos.y - last.y);
			
			System.out.print(Δx == 1 ? "→" : (Δx == -1 ? "←" : (Δy == 1 ? "↓" : (Δy == -1 ? "↑" : "↻"))));
			if(i % 100 == 0) System.out.println();
			
			last = path[i].state.pos;
		}
		
		System.out.println("\n" + path[path.length - 1].depth + "\n" + path[0].state + "\n" + path[path.length - 1].state);
	}
	
	public static class Result {
		public final Node[] result;
		public final double time;
		public final double space;
		
		public Result(Node[] result, double time, double space) {
			this.result = result;
			this.time   = time;
			this.space  = space;
		}
	}
	
	public static void main(String[] args) {
		// Start the simulation
		new Simulation().runCSV();
	}
}