package blocky;


import java.util.Arrays;
import java.util.Random;

/**
 * Stores a Simulation State
 * 
 * @author Matthew Consterdine
 */
public class State implements Cloneable {
	public final char[][]   map;
	public final Position2D pos;

	public State(char[][] map, Position2D pos) {
		if (pos == null) throw new RuntimeException("Invalid position");
		
		this.map = map;
		this.pos = pos;
	}
	
	/**
	 * Clone the State
	 * @return The map
	 */
	public char[][] cloneMap() {
		// Make a deep copy
		char[][] copy = new char[map.length][map[0].length];
		
		for(int y = 0; y < map.length; y++)
			for(int x = 0; x < map[y].length; x++)
				copy[y][x] = map[y][x];
		
		return copy;
	}
	
	/**
	 * Check if a position is in the map
	 * @param pos The position
	 * @return True if it is
	 */
	public boolean isPositionInMap(Position2D pos) {
		return pos != null && 
			   pos.y >= 0 && pos.y < map.length &&
			   pos.x >= 0 && pos.x < map[pos.y].length;
	}
	
	/**
	 * Mutate a state a finite number of times
	 * @param number Number of mutations
	 * @return A new mutated state
	 */
	public State mutate(int number) {
		State output = this;
		
		Random random = new Random();
		
		for(int i = 0; i < number; i++) {
			int Δx = 0, Δy = 0;
			
			if(random.nextBoolean()) {
				Δx = random.nextBoolean() ? 1 : -1;
			} else {
				Δy = random.nextBoolean() ? 1 : -1;
			}
			
			output = output.swap(Δx, Δy);
		}
		
		return output;
	}
	
	/**
	 * Move the agent on the map
	 * @param Δx Delta X
	 * @param Δy Delta Y
	 * @return New state with moved agent
	 */
	protected State swap(int Δx, int Δy) {
		char[][]   map = this.cloneMap();
		Position2D now = new Position2D(clamp(pos.x + Δx, 0, map[0].length),
										clamp(pos.y + Δy, 0, map.length));
		
		// Swap characters
		char temp = map[now.y][now.x];
		map[now.y][now.x] = map[pos.y][pos.x];
		map[pos.y][pos.x] = temp;
		
		return new State(map, now);
	}
	
	/**
	 * Clamp a value between two values
	 * @param value The value
	 * @param min Inclusive
	 * @param max Exclusive
	 * @return Clamped Value
	 */
	protected int clamp(int value, int min, int max) {
		return value > min ? value < max ? value : max - 1 : min;
	}
	
	@Override
	public boolean equals(Object object) {
		// Basic checks
		if(object.getClass() != this.getClass()) return false;
		
		State that = (State) object; 
		
		if(!that.pos.equals(this.pos) ||
		   that.map.length    != this.map.length ||
		   that.map[0].length != this.map[0].length ||
		   !that.pos.equals(this.pos)) return false;
		
		// Deep map check
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++) {
				if(this.map[y][x] != that.map[y][x]) return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Convert a State to a string
	 * @return
	 */
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++)
				output.append(map[y][x] != ' ' ? map[y][x] : '␣');
			output.append("\n");
		}
		
		return output.toString();
	}
	
	/**
	 * Convert a string to a state
	 * @param input The string
	 * @return Hopefully a valid state
	 * @throws InvalidParameterException if the input is empty
	 */
	protected static State parseString(String input) {
		if(input == null || input.length() == 0)
			throw new RuntimeException("String can't be null or empty");
		
		String[] lines = input.split("\\r?\\n");
		char[][] map = new char[lines.length][
		    Arrays.stream(lines).mapToInt(s -> s.length()).max().getAsInt()];
		
		if(map[0].length == 0)
			throw new RuntimeException("String can't be empty");
		
		Position2D pos = null;
		
		for(int y = 0; y < lines.length; y++) {
			for(int x = 0; x < lines[y].length(); x++) {
				map[y][x] = lines[y].charAt(x);
				
				if(pos == null && map[y][x] == '☺')
					pos = new Position2D(x, y);
			}
		}
		
		return new State(map, pos);
	}
	
	@Override
	protected State clone() {
		return new State(cloneMap(), pos.clone());
	}
}
