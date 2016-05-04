package net.earthcomputer.galacticgame.object;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.galacticgame.IUpdateListener;
import net.earthcomputer.galacticgame.geom.collision.MaskEllipse;

public class ExitObject extends GameObject implements IUpdateListener
{
	
	public ExitObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskEllipse(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.setColor(Color.PINK);
		g.fillOval((int) getX(), (int) getY(), 16, 16);
	}
	
	@Override
	public void update()
	{
		if(window.isObjectCollidedWith(this, PlayerObject.class))
		{
			window.completeLevel();
		}
	}
	
}