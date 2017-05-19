package blocky;

/**
 * Stores a 2D Cartesian position
 * 
 * @author Matthew Consterdine
 */
public class Position2D implements Cloneable {
	public final int x, y;
	
	public Position2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position2D add(Position2D other) {return add(other.x, other.y);}
	public Position2D add(int ox, int oy)   {return new Position2D(x + ox, y + oy);}

	public Position2D sub(Position2D other) {return sub(other.x, other.y);}
	public Position2D sub(int ox, int oy)   {return new Position2D(x - ox, y - oy);}

	public Position2D mul(Position2D other) {return mul(other.x, other.y);}
	public Position2D mul(int ox, int oy)   {return new Position2D(x * ox, y * oy);}

	public Position2D div(Position2D other) {return div(other.x, other.y);}
	public Position2D div(int ox, int oy)   {return new Position2D(x / ox, y / oy);}

	public Position2D mod(Position2D other) {return mod(other.x, other.y);}
	public Position2D mod(int ox, int oy)   {return new Position2D(x % ox, y % oy);}
	
	@Override
	public boolean equals(Object object) {
		return object.getClass()       == this.getClass() &&
			   ((Position2D) object).x == this.x &&
			   ((Position2D) object).y == this.y;
	}
	
	@Override
	public String toString() {
		return String.format("[%d, %d]", x, y);
	}
	
	@Override
	protected Position2D clone() {
		return new Position2D(x, y);
	}
}
