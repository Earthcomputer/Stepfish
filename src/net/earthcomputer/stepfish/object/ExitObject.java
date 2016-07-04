package net.earthcomputer.stepfish.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.stepfish.IUpdateListener;
import net.earthcomputer.stepfish.geom.collision.MaskEllipse;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.SoundManager;

public class ExitObject extends GameObject implements IUpdateListener
{
	
	private static final BufferedImage texture = Images.loadImage("object/exit");
	
	private int ticksExisted = 0;
	
	public ExitObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskEllipse(16, 16));
		setDepth(-1);
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(texture, (int) getX(), (int) (getY() + 4 * Math.sin((double) ticksExisted / 7)), null);
	}
	
	@Override
	public void update()
	{
		ticksExisted++;
		if(window.isObjectCollidedWith(this, PlayerObject.class))
		{
			SoundManager.playSound("exit");
			window.completeLevel();
		}
	}
	
}
