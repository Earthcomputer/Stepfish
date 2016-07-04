package net.earthcomputer.stepfish.geom;

/** Represents a position
 * 
 * @author Earthcomputer */
public class Pos
{
	private double x;
	private double y;
	
	/** Constructs a position from x- and y-co-ordinates */
	public Pos(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/** Gets the x-position of this position */
	public double getX()
	{
		return x;
	}
	
	/** Gets the y-position of this position */
	public double getY()
	{
		return y;
	}
	
	/** Sets the x-position of this position */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/** Sets the y-position of this position */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/** Adds the co-ordinates of the other position to the co-ordinates of this position */
	public Pos add(Pos other)
	{
		return add(other.x, other.y);
	}
	
	/** Adds the given co-ordinates to the co-ordinates of this position */
	public Pos add(double x, double y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	
	/** Creates an unmodifiable position */
	public static Pos unmodifiablePos(Pos pos)
	{
		return new UnmodifiablePos(pos);
	}
	
	/** Constructs a modifiable position that is a copy of the given position */
	public static Pos copyOf(Pos pos)
	{
		return new Pos(pos.x, pos.y);
	}
	
	@Override
	public String toString()
	{
		return String.format("(%f, %f)", x, y);
	}
	
	private static class UnmodifiablePos extends Pos
	{
		
		public UnmodifiablePos(Pos pos)
		{
			super(pos.x, pos.y);
		}
		
		@Override
		public void setX(double x)
		{
			throw new UnsupportedOperationException("Cannot set the x-position of an unmodifiable position");
		}
		
		@Override
		public void setY(double y)
		{
			throw new UnsupportedOperationException("Cannot set the y-position of an unmodifiable position");
		}
		
		@Override
		public Pos add(double x, double y)
		{
			throw new UnsupportedOperationException("Cannot change the position of an unmodifiable position");
		}
		
	}
}
