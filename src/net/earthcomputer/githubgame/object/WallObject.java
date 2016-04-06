package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class WallObject extends GameObject
{
	
	private EnumElement element;
	
	public WallObject(double x, double y)
	{
		this(x, y, null);
	}
	
	public WallObject(double x, double y, EnumElement element)
	{
		super(x, y);
		this.element = element;
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		if(element == null)
			g.setColor(Color.WHITE);
		else g.setColor(element.getColor());
		
		g.fillRect((int) getX(), (int) getY(), 16, 16);
	}
	
}
