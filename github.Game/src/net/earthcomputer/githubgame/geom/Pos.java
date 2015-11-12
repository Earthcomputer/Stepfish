package net.earthcomputer.githubgame.geom;

/**
 * Represents a position
 * 
 * @author Earthcomputer
 *
 */
public class Pos {
	private float x;
	private float y;

	/**
	 * Constructs a position from x- and y-co-ordinates
	 */
	public Pos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x-position of this position
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the y-position of this position
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the x-position of this position
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the y-position of this position
	 */
	public void setY(float y) {
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

	/**
	 * Creates an unmodifiable position
	 */
	public static Pos unmodifiablePos(Pos pos) {
		return new UnmodifiablePos(pos);
	}

	private static class UnmodifiablePos extends Pos {

		public UnmodifiablePos(Pos pos) {
			super(pos.x, pos.y);
		}

		@Override
		public void setX(float x) {
			throw new UnsupportedOperationException("Cannot set the x-position of an unmodifiable position");
		}

		@Override
		public void setY(float y) {
			throw new UnsupportedOperationException("Cannot set the y-position of an unmodifiable position");
		}

	}
}
