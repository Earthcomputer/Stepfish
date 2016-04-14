package net.earthcomputer.githubgame.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;
import net.earthcomputer.githubgame.util.Images;

public class StarObject extends GameObject implements IUpdateListener
{
	
	public static final BufferedImage STAR_TEXTURE = Images.loadImage("star");
	
	public StarObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(STAR_TEXTURE, (int) getX(), (int) getY(), null);
	}
	
	@Override
	public void update()
	{
		if(window.isObjectCollidedWith(this, PlayerObject.class))
		{
			window.removeObject(this);
		}
	}
	
}
