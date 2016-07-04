package net.earthcomputer.stepfish.geom;

/** Represents a combined speed and direction
 * 
 * @author Earthcomputer */
public class Velocity
{
	private float speed;
	private float direction;
	private float xvel;
	private float yvel;
	
	private Velocity()
	{
	}
	
	/** Creates a {@link Velocity} from a speed and a direction
	 * 
	 * @see #setSpeed(float)
	 * @see #setDirection(float) */
	public static Velocity createFromSpeedAndDirection(float speed, float direction)
	{
		Velocity vel = new Velocity();
		
		if(speed < 0)
		{
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
	
	/** Creates a {@link Velocity} from horizontal and vertical velocities
	 * 
	 * @see #setXComponent(float)
	 * @see #setYComponent(float) */
	public static Velocity createFromXAndYComponents(float xvel, float yvel)
	{
		Velocity vel = new Velocity();
		
		vel.speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		vel.direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if(vel.direction < 0) vel.direction += 360;
		
		vel.xvel = xvel;
		vel.yvel = yvel;
		
		return vel;
	}
	
	/** Gets the velocity's speed
	 * 
	 * @see #setSpeed(float) */
	public float getSpeed()
	{
		return speed;
	}
	
	/** Gets the velocity's direction. 0 is horizontally to the right and increases clockwise. The direction has a range
	 * of 0, inclusive, and 360, exclusive
	 * 
	 * @see #setDirection(float) */
	public float getDirection()
	{
		return direction;
	}
	
	/** Gets the horizontal velocity in this velocity. A negative value means the object is moving left, and a positive
	 * value means the object is moving right
	 * 
	 * @see #setXComponent(float) */
	public float getXComponent()
	{
		return xvel;
	}
	
	/** Gets the vertical velocity in this velocity. A negative value means the object is moving up, and a positive
	 * value means the object is moving down
	 * 
	 * @see #setYComponent(float) */
	public float getYComponent()
	{
		return yvel;
	}
	
	/** Sets this velocity's speed
	 * 
	 * @see #getSpeed() */
	public void setSpeed(float speed)
	{
		if(speed < 0)
		{
			speed = -speed;
			direction += 180;
		}
		this.speed = speed;
		direction = directionInBounds(direction);
		
		xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		yvel = (float) (speed * Math.sin(Math.toRadians(direction)));
	}
	
	/** Sets this velocity's direction. For more information, see {@link #getDirection()}
	 * 
	 * @see #getDirection() */
	public void setDirection(float direction)
	{
		direction = directionInBounds(direction);
		this.direction = direction;
		
		xvel = (float) (speed * Math.cos(Math.toRadians(direction)));
		yvel = (float) (speed * Math.sin(Math.toRadians(direction)));
	}
	
	/** Sets the horizontal velocity in this velocity. For more information, see {@link #getXComponent()}
	 * 
	 * @see #getXComponent() */
	public void setXComponent(float xvel)
	{
		this.xvel = xvel;
		
		speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if(direction < 0) direction += 360;
	}
	
	/** Sets the vertical velocity in this velocity. For more information, see {@link #getYComponent()}
	 * 
	 * @see #getYComponent() */
	public void setYComponent(float yvel)
	{
		this.yvel = yvel;
		
		speed = (float) Math.sqrt(xvel * xvel + yvel * yvel);
		direction = (float) Math.toDegrees(Math.atan2(yvel, xvel));
		if(direction < 0) direction += 360;
	}
	
	public void accelerate(float amt)
	{
		setSpeed(speed + amt);
	}
	
	public void turn(float amt)
	{
		setDirection(direction + amt);
	}
	
	public void accelerateX(float amt)
	{
		setXComponent(xvel + amt);
	}
	
	public void accelerateY(float amt)
	{
		setYComponent(yvel + amt);
	}
	
	public void accelerate(Velocity gravity)
	{
		accelerateX(gravity.getXComponent());
		accelerateY(gravity.getYComponent());
	}
	
	public static Velocity unmodifiableVelocity(Velocity vel)
	{
		return new UnmodifiableVelocity(vel);
	}
	
	public static Velocity copyOf(Velocity vel)
	{
		return createFromSpeedAndDirection(vel.getSpeed(), vel.getDirection());
	}
	
	private float directionInBounds(float direction)
	{
		while(direction >= 360)
			direction -= 360;
		while(direction < 0)
			direction += 360;
		return direction;
	}
	
	private static class UnmodifiableVelocity extends Velocity
	{
		private boolean locked = false;
		
		private UnmodifiableVelocity(Velocity vel)
		{
			setSpeed(vel.getSpeed());
			setDirection(vel.getDirection());
			locked = true;
		}
		
		@Override
		public void setSpeed(float speed)
		{
			if(locked)
				throw new UnsupportedOperationException("Cannot set the speed of an unmodifiable velocity");
			else super.setSpeed(speed);
		}
		
		@Override
		public void setDirection(float direction)
		{
			if(locked)
				throw new UnsupportedOperationException("Cannot set the direction of an unmodifiable velocity");
			else super.setDirection(direction);
		}
		
		@Override
		public void setXComponent(float xvel)
		{
			if(locked)
				throw new UnsupportedOperationException("Cannot set the x-component of an unmodifiable velocity");
			else super.setXComponent(xvel);
		}
		
		@Override
		public void setYComponent(float yvel)
		{
			if(locked)
				throw new UnsupportedOperationException("Cannot set the y-component of an unmodifiable velocity");
			else super.setYComponent(yvel);
		}
	}
}
