package net.earthcomputer.githubgame.geom;

/**
 * Represents a position
 * 
 * @author Earthcomputer
 *
 */
public class Pos {
	/**
	 * The x-position of this position
	 */
	public float x;
	/**
	 * The y-position of this position
	 */
	public float y;

	/**
	 * Constructs a position from x- and y-co-ordinates
	 */
	public Pos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds the co-ordinates of the other position to the co-ordinates of this
	 * position
	 */
	public Pos add(Pos other) {
		return add(other.x, other.y);
	}

	/**
	 * Adds the given co-ordinates to the co-ordinates of this position
	 */
	public Pos add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
}
