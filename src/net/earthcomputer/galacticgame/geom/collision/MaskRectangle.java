package net.earthcomputer.galacticgame.geom.collision;

import java.awt.geom.Rectangle2D;

/** An implementation of a collision mask in the shape of a rectangle
 * 
 * @author Earthcomputer */
public class MaskRectangle extends CollisionMask<Rectangle2D>
{
	
	public MaskRectangle(double width, double height)
	{
		this(0, 0, width, height);
	}
	
	public MaskRectangle(double x, double y, double width, double height)
	{
		this(new Rectangle2D.Double(x, y, width, height));
	}
	
	/** Constructs a rectangle collision mask. Co-ordinates are local */
	public MaskRectangle(Rectangle2D rect)
	{
		super(rect);
	}
	
	@Override
	protected Rectangle2D translate(Rectangle2D shape, double x, double y)
	{
		return new Rectangle2D.Double(shape.getX() + x, shape.getY() + y, shape.getWidth(), shape.getHeight());
	}
	
	@Override
	protected Rectangle2D copy(Rectangle2D shape)
	{
		return shape.getBounds2D();
	}
	
}
