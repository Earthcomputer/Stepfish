package net.earthcomputer.githubgame.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class StarObject extends GameObject implements IUpdateListener
{
	
	public static final BufferedImage STAR_TEXTURE;
	
	static
	{
		try
		{
			STAR_TEXTURE = ImageIO
				.read(new BufferedInputStream(StarObject.class.getResourceAsStream("/textures/star.png")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
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
