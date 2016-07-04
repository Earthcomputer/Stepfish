package net.earthcomputer.stepfish.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Iterator;

import net.earthcomputer.stepfish.Stepfish;
import net.earthcomputer.stepfish.IUpdateListener;
import net.earthcomputer.stepfish.geom.collision.MaskPolygon;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.Predicate;

public class SpikeObject extends GameObject implements IUpdateListener
{
	
	private static final EnumMap<EnumElement, BufferedImage> textures = new EnumMap<EnumElement, BufferedImage>(
		EnumElement.class);
		
	static
	{
		for(EnumElement element : EnumElement.values())
		{
			textures.put(element, Images.loadImage("object/spike_" + element.getName()));
		}
	}
	
	private static final int SWITCH_RATE = Stepfish.TICKRATE;
	private EnumElement element;
	private int ticksUntilSwitch;
	
	public SpikeObject(double x, double y, EnumElement element, Boolean switching)
	{
		this(x, y, element, switching.booleanValue());
	}
	
	public SpikeObject(double x, double y, EnumElement element, boolean switching)
	{
		super(x, y);
		setCollisionMask(new MaskPolygon(new int[] { 0, 16, 8 }, new int[] { 16, 16, 0 }, 3));
		this.element = element;
		ticksUntilSwitch = switching ? SWITCH_RATE : -1;
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.drawImage(textures.get(element), x, y, null);
	}
	
	@Override
	public void update()
	{
		if(ticksUntilSwitch != -1)
		{
			if(ticksUntilSwitch == 0)
			{
				element = element.nextElement();
				ticksUntilSwitch = SWITCH_RATE;
			}
			ticksUntilSwitch--;
		}
		
		Iterator<GameObject> collidedPlayers = window.getObjectsThatCollideWith(this, new Predicate<GameObject>() {
			@Override
			public boolean apply(GameObject input)
			{
				return (input instanceof PlayerObject) && ((PlayerObject) input).getElement() != element;
			}
		}).iterator();
		if(collidedPlayers.hasNext())
		{
			window.failLevel((PlayerObject) collidedPlayers.next(), this);
		}
	}
	
	public EnumElement getElement()
	{
		return element;
	}
	
}
