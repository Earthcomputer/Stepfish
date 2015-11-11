package net.earthcomputer.githubgame.geom;

/**
 * Represents a combined speed and direction
 * 
 * @author Earthcomputer
 *
 */
public class Velocity {
	private float speed;
	private float direction;
	private float xvel;
	private float yvel;

	private Velocity() {
	}

	/**
	 * Creates a {@link Velocity} from a speed and a direction
	 * 
	 * @see #setSpeed(float)
	 * @see #setDirection(float)
	 */
	public static Velocity createFromSpeedAndDirection(float speed, float direction) {
		Velocity vel = new Velocity();

		if (speed < 0) {
			speed = -speed;
			direction += 180;
		}
		vel.speed = speed;
		direction = vel.directionInBounds(direction);
		vel.direction = direction;

		vel.xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		vel.yvel = (float) (speed * Math.sin(Math.toRadians(direction)));

		return vel;
	}

	/**
	 * Creates a {@link Velocity} from horizontal and vertical velocities
	 * 
	 * @see #setXComponent(float)
	 * @see #setYComponent(float)
	 */
	public static Velocity createFromXAndYComponents(float xvel, float yvel) {
		Velocity vel = new Velocity();

		vel.speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		vel.direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if (vel.direction < 0)
			vel.direction += 360;

		vel.xvel = xvel;
		vel.yvel = yvel;

		return vel;
	}

	/**
	 * Gets the velocity's speed
	 * 
	 * @see #setSpeed(float)
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Gets the velocity's direction. 0 is horizontally to the right and
	 * increases clockwise. The direction has a range of 0, inclusive, and 360,
	 * exclusive
	 * 
	 * @see #setDirection(float)
	 */
	public float getDirection() {
		return direction;
	}

	/**
	 * Gets the horizontal velocity in this velocity. A negative value means the
	 * object is moving left, and a positive value means the object is moving
	 * right
	 * 
	 * @see #setXComponent(float)
	 */
	public float getXComponent() {
		return xvel;
	}

	/**
	 * Gets the vertical velocity in this velocity. A negative value means the
	 * object is moving up, and a positive value means the object is moving down
	 * 
	 * @see #setYComponent(float)
	 */
	public float getYComponent() {
		return yvel;
	}

	/**
	 * Sets this velocity's speed
	 * 
	 * @see #getSpeed()
	 */
	public void setSpeed(float speed) {
		if (speed < 0) {
			speed = -speed;
			direction += 180;
		}
		this.speed = speed;
		direction = directionInBounds(direction);

		xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		yvel = (float) (speed * Math.sin(Math.toRadians(direction)));
	}

	/**
	 * Sets this velocity's direction. For more information, see
	 * {@link #getDirection()}
	 * 
	 * @see #getDirection()
	 */
	public void setDirection(float direction) {
		direction = directionInBounds(direction);
		this.direction = direction;

		xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		yvel = (float) (speed * Math.sin(Math.toRadians(direction)));
	}

	/**
	 * Sets the horizontal velocity in this velocity. For more information, see
	 * {@link #getXComponent()}
	 * 
	 * @see #getXComponent()
	 */
	public void setXComponent(float xvel) {
		this.xvel = xvel;

		speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if (direction < 0)
			direction += 360;
	}

	/**
	 * Sets the vertical velocity in this velocity. For more information, see
	 * {@link #getYComponent()}
	 * 
	 * @see #getYComponent()
	 */
	public void setYComponent(float yvel) {
		this.yvel = yvel;

		speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if (direction < 0)
			direction += 360;
	}

	private float directionInBounds(float direction) {
		while (direction >= 360)
			direction -= 360;
		while (direction < 0)
			direction += 360;
		return direction;
	}
}
