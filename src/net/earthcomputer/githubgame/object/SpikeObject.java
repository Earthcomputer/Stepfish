package net.earthcomputer.githubgame.object;

import java.awt.Graphics;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.collision.MaskPolygon;

public class SpikeObject extends GameObject implements IUpdateListener
{
	
	private static final int SWITCH_RATE = GithubGame.TICKRATE;
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
	}
	
	public EnumElement getElement()
	{
		return element;
	}
	
}
