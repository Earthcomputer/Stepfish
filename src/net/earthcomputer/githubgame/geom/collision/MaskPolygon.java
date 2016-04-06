package net.earthcomputer.githubgame.geom.collision;

import java.awt.Polygon;

/** An implementation of a collision mask in the shape of a polygon
 * 
 * @author Earthcomputer */
public class MaskPolygon extends CollisionMask<Polygon>
{
	
	public MaskPolygon(int[] xpoints, int[] ypoints, int npoints)
	{
		this(new Polygon(xpoints, ypoints, npoints));
	}
	
	public MaskPolygon(Polygon polygon)
	{
		super(polygon);
	}
	
	@Override
	protected Polygon translate(Polygon shape, double x, double y)
	{
		for(int i = 0; i < shape.npoints; i++)
		{
			shape.xpoints[i] += x;
			shape.ypoints[i] += y;
		}
		return shape;
	}
	
	@Override
	protected Polygon copy(Polygon shape)
	{
		return new Polygon(shape.xpoints.clone(), shape.ypoints.clone(), shape.npoints);
	}
	
}
