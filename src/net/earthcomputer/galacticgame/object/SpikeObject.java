package net.earthcomputer.galacticgame.object;

import java.awt.Graphics;

import net.earthcomputer.galacticgame.GalacticGame;
import net.earthcomputer.galacticgame.IUpdateListener;
import net.earthcomputer.galacticgame.geom.collision.MaskPolygon;
import net.earthcomputer.galacticgame.util.Predicate;

public class SpikeObject extends GameObject implements IUpdateListener
{
	
	private static final int SWITCH_RATE = GalacticGame.TICKRATE;
	private EnumElement element;
	private int ticksUntilSwitch;
	
	public SpikeObject(double x, double y, EnumElement element, Boolean switching)
	{
		this(x, y, element, switching.booleanValue());
	}
	
	public SpikeObject(double x, double y, EnumElement element, boolean switching)
	{
		super(x, y);
		setCollisionMask(new MaskPolygon(new int[] { 0, 16, 8 }, new int[] { 16, 16, 4 }, 3));
		this.element = element;
		ticksUntilSwitch = switching ? SWITCH_RATE : -1;
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.setColor(element.getColor());
		g.fillPolygon(new int[] { x, x + 16, x + 8 }, new int[] { y + 16, y + 16, y + 4 }, 3);
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
		
		if(window.isObjectCollidedWith(this, new Predicate<GameObject>() {
			@Override
			public boolean apply(GameObject input)
			{
				return (input instanceof PlayerObject) && ((PlayerObject) input).getElement() != element;
			}
		}))
		{
			window.restartLevel();
		}
	}
	
	public EnumElement getElement()
	{
		return element;
	}
	
}
