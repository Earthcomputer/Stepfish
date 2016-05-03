package net.earthcomputer.githubgame.object;

import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class EnemyBlockerObject extends GameObject
{
	
	public EnemyBlockerObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
}
