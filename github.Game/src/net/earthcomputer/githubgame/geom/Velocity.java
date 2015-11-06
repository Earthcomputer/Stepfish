package net.earthcomputer.githubgame.geom;

/**
 * Represents a combined speed and direction
 * 
 * @author Earthcomputer
 *
 */
public class Velocity {
	private float speed;
	/**
	 * Direction in degrees. 0 degrees vertically upwards, increases clockwise
	 */
	private float direction;
	private float xvel;
	private float yvel;

	private Velocity() {
	}

	public static Velocity createFromSpeedAndDirection(float speed, float direction) {
		Velocity vel = new Velocity();

		if (speed < 0) {
			speed = -speed;
			direction += 180;
		}
		vel.speed = speed;
		direction = vel.directionInBounds(direction);
		vel.direction = direction;

		// cos dir = xvel / speed => xvel = speed cos dir
		vel.xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		// sin dir = yven / speed => yvel = speed sin dir
		vel.yvel = (float) (speed * Math.sin(Math.toRadians(direction)));

		return vel;
	}

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

	public float getSpeed() {
		return speed;
	}

	public float getDirection() {
		return direction;
	}

	public float getXComponent() {
		return xvel;
	}

	public float getYComponent() {
		return yvel;
	}

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

	public void setDirection(float direction) {
		direction = directionInBounds(direction);
		this.direction = direction;

		xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		yvel = (float) (speed * Math.sin(Math.toRadians(direction)));
	}

	public void setXComponent(float xvel) {
		this.xvel = xvel;

		speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if (direction < 0)
			direction += 360;
	}

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
