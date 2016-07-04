package net.earthcomputer.stepfish.object;

import net.earthcomputer.stepfish.IUpdateListener;
import net.earthcomputer.stepfish.geom.Pos;
import net.earthcomputer.stepfish.geom.Velocity;

public class PhysicsObject extends GameObject implements IUpdateListener
{
	
	private Pos posPrev;
	private Velocity velocity;
	private Velocity gravity;
	
	public PhysicsObject(double x, double y)
	{
		super(x, y);
		this.velocity = Velocity.createFromXAndYComponents(0, 0);
		this.gravity = Velocity.createFromXAndYComponents(0, 0);
	}
	
	public Velocity getVelocity()
	{
		return Velocity.unmodifiableVelocity(velocity);
	}
	
	public float getSpeed()
	{
		return velocity.getSpeed();
	}
	
	public float getDirection()
	{
		return velocity.getDirection();
	}
	
	public float getXVelocity()
	{
		return velocity.getXComponent();
	}
	
	public float getYVelocity()
	{
		return velocity.getYComponent();
	}
	
	public void setVelocity(Velocity vel)
	{
		this.velocity = Velocity.copyOf(vel);
	}
	
	public void setSpeed(float speed)
	{
		velocity.setSpeed(speed);
	}
	
	public void setDirection(float direction)
	{
		velocity.setDirection(direction);
	}
	
	public void setXVelocity(float xvel)
	{
		velocity.setXComponent(xvel);
	}
	
	public void setYVelocity(float yvel)
	{
		velocity.setYComponent(yvel);
	}
	
	public void accelerate(float amt)
	{
		velocity.accelerate(amt);
	}
	
	public void turn(float amt)
	{
		velocity.turn(amt);
	}
	
	public void accelerateX(float amt)
	{
		velocity.accelerateX(amt);
	}
	
	public void accelerateY(float amt)
	{
		velocity.accelerateY(amt);
	}
	
	/** Adds gravity to this object
	 * 
	 * @param gravity
	 * @return The unique gravity id to refer to this gravity later on */
	public void addGravity(Velocity gravity)
	{
		this.gravity.accelerate(gravity);
	}
	
	public void setGravity(Velocity gravity)
	{
		this.gravity.setSpeed(gravity.getSpeed());
		this.gravity.setDirection(gravity.getDirection());
	}
	
	public void removeGravity()
	{
		this.gravity.setSpeed(0);
	}
	
	public Velocity getGravity()
	{
		return Velocity.copyOf(gravity);
	}
	
	public Pos getPreviousPos()
	{
		return Pos.copyOf(posPrev);
	}
	
	@Override
	public void update()
	{
		posPrev = Pos.copyOf(getPos());
		
		move(velocity.getXComponent(), velocity.getYComponent());
		
		velocity.accelerate(gravity);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s{pos=[%f, %f], vel=[%f, %f]}", getClass().getSimpleName(), getX(), getY(),
			getXVelocity(), getYVelocity());
	}
	
}
