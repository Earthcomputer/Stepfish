package net.earthcomputer.githubgame.object;

import java.awt.Graphics;
import java.util.List;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.collision.MaskPolygon;

public class SwitchingSpikeObject extends GameObject implements IUpdateListener
{
	
	private static final int SWITCH_RATE = GithubGame.TICKRATE;
	private EnumElement element;
	private int ticksUntilSwitch;
	
	public SwitchingSpikeObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskPolygon(new int[] { 0, 16, 8 }, new int[] { 16, 16, 4 }, 3));
		element = EnumElement.EARTH;
		ticksUntilSwitch = SWITCH_RATE;
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
		if(ticksUntilSwitch == 0)
		{
			element = element.nextElement();
			ticksUntilSwitch = SWITCH_RATE;
		}
		ticksUntilSwitch--;
		
		List<GameObject> collided = window.getObjectsThatCollideWith(this);
		for(GameObject object : collided)
		{
			if(object instanceof PlayerObject)
			{
				if(element != ((PlayerObject) object).getElement())
				{
					window.restartLevel();
					break;
				}
			}
		}
	}
	
}
