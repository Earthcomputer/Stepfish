package net.earthcomputer.galacticgame.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.galacticgame.IUpdateListener;
import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;
import net.earthcomputer.galacticgame.util.Images;
import net.earthcomputer.galacticgame.util.SoundManager;

public class ElementSwitcherObject extends GameObject implements IUpdateListener
{
	
	private static final BufferedImage[] texture = new BufferedImage[4];
	
	private static final int TICKS_PER_FRAME = 20;
	private int ticksExisted = 0;
	
	static
	{
		for(int i = 0; i < texture.length; i++)
		{
			texture[i] = Images.loadImage("object/element_switcher_" + i);
		}
	}
	
	public ElementSwitcherObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.drawImage(texture[(ticksExisted / TICKS_PER_FRAME) % 4], x, y, null);
	}
	
	@Override
	public void update()
	{
		ticksExisted++;
		
		if(window.isObjectCollidedWith(this, PlayerObject.class))
		{
			SoundManager.playSound("element_switcher");
			for(PlayerObject player : window.listObjects(PlayerObject.class))
			{
				player.setElement(player.getElement().nextElement());
			}
			window.removeObject(this);
		}
	}
	
}
