package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.githubgame.geom.collision.MaskEllipse;

public class ExitObject extends GameObject
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
	
}
