package net.earthcomputer.galacticgame.geom.collision;

import java.awt.Shape;

import net.earthcomputer.galacticgame.geom.Pos;

/** Represents a collision mask.
 * 
 * @author Earthcomputer */
public abstract class CollisionMask<S extends Shape>
{
	
	protected S localShape;
	protected S globalShape;
	protected Pos globalPos;
	
	public CollisionMask(S shape)
	{
		this.localShape = copy(shape);
		this.globalShape = copy(shape);
		this.globalPos = new Pos(0, 0);
	}
	
	public S getGlobalShape()
	{
		return copy(globalShape);
	}
	
	public void setGlobalPos(Pos pos)
	{
		this.globalShape = translate(this.globalShape, pos.getX() - this.globalPos.getX(),
			pos.getY() - this.globalPos.getY());
		this.globalPos = Pos.copyOf(pos);
	}
	
	public S getLocalShape()
	{
		return copy(localShape);
	}
	
	public void setLocalShape(S shape)
	{
		this.localShape = copy(shape);
		this.globalShape = copy(shape);
		this.globalShape = translate(this.globalShape, this.globalPos.getX(), this.globalPos.getY());
	}
	
	/** Translates the given shape. Depending on the shape's functionality, this method may modify the original shape or
	 * create a new one.
	 * @param shape
	 * @param x
	 * @param y
	 * @return */
	protected abstract S translate(S shape, double x, double y);
	
	protected abstract S copy(S shape);
	
}
