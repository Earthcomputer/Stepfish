package net.earthcomputer.stepfish.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.stepfish.geom.collision.MaskRectangle;
import net.earthcomputer.stepfish.util.Images;

public class MudObject extends GameObject
{
	
	private static final BufferedImage texture = Images.loadImage("object/mud");
	
	public MudObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(texture, (int) getX(), (int) getY(), null);
	}
	
}
