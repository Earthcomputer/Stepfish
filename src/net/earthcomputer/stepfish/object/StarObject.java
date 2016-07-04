package net.earthcomputer.stepfish.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.stepfish.IUpdateListener;
import net.earthcomputer.stepfish.geom.collision.MaskRectangle;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.SoundManager;

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
			SoundManager.playSound("star");
			window.completeStar(index);
			window.removeObject(this);
		}
	}
	
}
