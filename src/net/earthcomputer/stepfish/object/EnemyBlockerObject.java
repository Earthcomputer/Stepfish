package net.earthcomputer.stepfish.object;

import net.earthcomputer.stepfish.geom.collision.MaskRectangle;

public class EnemyBlockerObject extends GameObject
{
	
	public EnemyBlockerObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
}
