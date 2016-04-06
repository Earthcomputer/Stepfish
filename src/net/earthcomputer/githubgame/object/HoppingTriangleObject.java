package net.earthcomputer.githubgame.object;

import java.awt.Graphics;

import net.earthcomputer.githubgame.geom.collision.MaskPolygon;

public class HoppingTriangleObject extends GameObject
{
	
	private EnumElement element;
	
	public HoppingTriangleObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskPolygon(new int[] { 0, 16, 8 }, new int[] { 16, 16, 4 }, 3));
		element = EnumElement.EARTH;
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.setColor(element.getColor());
		g.fillPolygon(new int[] { x, x + 16, x + 8 }, new int[] { y + 16, y + 16, y + 4 }, 3);
	}
	
}
