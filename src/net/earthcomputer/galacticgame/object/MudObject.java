package net.earthcomputer.galacticgame.object;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;

public class MudObject extends GameObject
{
	
	public MudObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.setColor(Color.RED.darker().darker());
		g.fillRect((int) getX(), (int) getY(), 16, 16);
	}
	
}
