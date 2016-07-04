package net.earthcomputer.stepfish.geom.collision;

import java.awt.geom.Ellipse2D;

/** An implementation of the collision mask in the shape of an ellipse
 * 
 * @author Earthcomputer */
public class MaskEllipse extends CollisionMask<Ellipse2D>
{
	
	public MaskEllipse(double width, double height)
	{
		this(0, 0, width, height);
	}
	
	public MaskEllipse(double x, double y, double width, double height)
	{
		this(new Ellipse2D.Double(x, y, width, height));
	}
	
	public MaskEllipse(Ellipse2D ellipse)
	{
		super(ellipse);
	}
	
	@Override
	protected Ellipse2D translate(Ellipse2D shape, double x, double y)
	{
		if(x == 0 && y == 0) return shape;
		
		if(shape instanceof Ellipse2D.Double)
		{
			Ellipse2D.Double ellipse = (Ellipse2D.Double) shape;
			ellipse.x += x;
			ellipse.y += y;
			return ellipse;
		}
		
		return new Ellipse2D.Double(shape.getX() + x, shape.getY() + y, shape.getWidth(), shape.getHeight());
	}
	
	@Override
	protected Ellipse2D copy(Ellipse2D shape)
	{
		return new Ellipse2D.Double(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
	}
	
}
