package net.earthcomputer.galacticgame.object;

import java.awt.Graphics;
import java.util.List;

import net.earthcomputer.galacticgame.IUpdateListener;
import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;

public class ElementSwitcherObject extends GameObject implements IUpdateListener
{
	
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
		g.setColor(EnumElement.EARTH.getColor());
		g.fillPolygon(new int[] { x, x + 16, x + 8 }, new int[] { y, y, y + 8 }, 3);
		g.setColor(EnumElement.WATER.getColor());
		g.fillPolygon(new int[] { x + 16, x + 16, x + 8 }, new int[] { y, y + 16, y + 8 }, 3);
		g.setColor(EnumElement.AIR.getColor());
		g.fillPolygon(new int[] { x + 16, x, x + 8 }, new int[] { y + 16, y + 16, y + 8 }, 3);
		g.setColor(EnumElement.FIRE.getColor());
		g.fillPolygon(new int[] { x, x, x + 8 }, new int[] { y + 16, y, y + 8 }, 3);
	}
	
	@Override
	public void update()
	{
		List<PlayerObject> collided = window.getObjectsThatCollideWith(this, PlayerObject.class);
		for(PlayerObject player : collided)
		{
			player.setElement(player.getElement().nextElement());
			window.removeObject(this);
			break;
		}
	}
	
}
