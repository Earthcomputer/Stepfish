package net.earthcomputer.galacticgame.object;

import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;

public class EnemyBlockerObject extends GameObject
{
	
	public EnemyBlockerObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
}
