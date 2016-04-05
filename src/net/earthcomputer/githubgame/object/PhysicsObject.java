package net.earthcomputer.githubgame.object;

import java.util.HashMap;
import java.util.Map;

import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.Velocity;

public class PhysicsObject extends GameObject implements IUpdateListener {

	private Velocity velocity;
	private Map<Integer, Velocity> gravities = new HashMap<Integer, Velocity>();
	private int nextGravityId = 0;

	public PhysicsObject(double x, double y) {
		super(x, y);
		this.velocity = Velocity.createFromXAndYComponents(0, 0);
	}

	public Velocity getVelocity() {
		return Velocity.unmodifiableVelocity(velocity);
	}

	public float getSpeed() {
		return velocity.getSpeed();
	}

	public float getDirection() {
		return velocity.getDirection();
	}

	public float getXVelocity() {
		return velocity.getXComponent();
	}

	public float getYVelocity() {
		return velocity.getYComponent();
	}

	public void setVelocity(Velocity vel) {
		this.velocity = Velocity.copyOf(vel);
	}

	public void setSpeed(float speed) {
		velocity.setSpeed(speed);
	}

	public void setDirection(float direction) {
		velocity.setDirection(direction);
	}

	public void setXVelocity(float xvel) {
		velocity.setXComponent(xvel);
	}

	public void setYVelocity(float yvel) {
		velocity.setYComponent(yvel);
	}

	public void accelerate(float amt) {
		velocity.accelerate(amt);
	}

	public void turn(float amt) {
		velocity.turn(amt);
	}

	public void accelerateX(float amt) {
		velocity.accelerateX(amt);
	}

	public void accelerateY(float amt) {
		velocity.accelerateY(amt);
	}

	/**
	 * Adds gravity to this object
	 * 
	 * @param gravity
	 * @return The unique gravity id to refer to this gravity later on
	 */
	public int addGravity(Velocity gravity) {
		gravities.put(nextGravityId, Velocity.copyOf(gravity));
		return nextGravityId++;
	}

	public void updateGravity(int gravityId, Velocity newGravity) {
		if (!gravities.containsKey(gravityId)) {
			throw new IllegalArgumentException(
					"No gravity is registered to this object with the id " + gravityId + ", cannot update");
		}
		gravities.put(gravityId, Velocity.copyOf(newGravity));
	}

	public void removeGravity(int gravityId) {
		gravities.remove(gravityId);
	}

	@Override
	public void update() {
		move(velocity.getXComponent(), velocity.getYComponent());

		for (Velocity gravity : gravities.values()) {
			velocity.accelerate(gravity);
		}
	}

}
