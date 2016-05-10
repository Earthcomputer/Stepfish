package net.earthcomputer.galacticgame.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.galacticgame.IUpdateListener;
import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;
import net.earthcomputer.galacticgame.util.Images;

public class StarObject extends GameObject implements IUpdateListener
{
	
	public static final BufferedImage STAR_TEXTURE = Images.loadImage("object/star");
	
	private int index;
	
	// Required for loading levels
	public StarObject(double x, double y, Integer index)
	{
		this(x, y, index.intValue());
	}
	
	public StarObject(double x, double y, int index)
	{
		super(x, y);
		this.index = index;
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void onAdded()
	{
		if(window.getProfile().isStarObtained(window.getCurrentLevelIndex(), index))
		{
			window.removeObject(this);
		}
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
			window.completeStar(index);
			window.removeObject(this);
		}
	}
	
}
