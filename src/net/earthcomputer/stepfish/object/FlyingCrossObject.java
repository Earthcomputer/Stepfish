package net.earthcomputer.stepfish.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import net.earthcomputer.stepfish.IUpdateListener;
import net.earthcomputer.stepfish.geom.collision.MaskRectangle;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.Predicate;

public class FlyingCrossObject extends GameObject implements IUpdateListener
{
	private static final BufferedImage[] texture = new BufferedImage[2];
	private static final int TICKS_PER_FRAME = 15;
	
	static
	{
		for(int i = 0; i < texture.length; i++)
		{
			texture[i] = Images.loadImage("object/flying_cross_" + i);
		}
	}
	
	private static final int MOVE_SPEED = 3;
	
	private static final Predicate<GameObject> WALL_COLLISION = new Predicate<GameObject>() {
		@Override
		public boolean apply(GameObject input)
		{
			return (input instanceof EnemyBlockerObject)
				|| ((input instanceof WallObject) && ((WallObject) input).getElement() == null);
		}
	};
	
	private int ticksExisted = 0;
	private double startingY;
	private double attackTargetY;
	private EnumState state;
	
	public FlyingCrossObject(double x, double y)
	{
		super(x, y);
		this.startingY = y;
		this.state = EnumState.MOVE;
		setCollisionMask(new MaskRectangle(1, 1, 14, 14));
		setDepth(-500);
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.drawImage(texture[(ticksExisted / TICKS_PER_FRAME) % 2], x, y, null);
	}
	
	@Override
	public void update()
	{
		ticksExisted++;
		switch(state)
		{
			case ATTACK_DOWN:
				if(getY() < attackTargetY)
				{
					setY(Math.min(getY() + MOVE_SPEED, attackTargetY));
					if(window.isObjectCollidedWith(this, WALL_COLLISION))
					{
						state = EnumState.ATTACK_UP;
					}
				}
				else
				{
					state = EnumState.ATTACK_UP;
				}
				break;
			case ATTACK_UP:
				if(getY() > startingY)
				{
					setY(Math.max(getY() - MOVE_SPEED, startingY));
				}
				else
				{
					state = EnumState.MOVE;
				}
				break;
			case MOVE:
				List<PlayerObject> players = window.listObjects(PlayerObject.class);
				PlayerObject targetPlayer = null;
				double closestDistanceSquared = Double.MAX_VALUE;
				for(PlayerObject player : players)
				{
					double dx = getX() - player.getX();
					double dy = getY() - player.getY();
					double distSquared = dx * dx + dy * dy;
					if(distSquared < 150 * 150 && distSquared < closestDistanceSquared)
					{
						targetPlayer = player;
						closestDistanceSquared = distSquared;
					}
				}
				
				if(targetPlayer != null)
				{
					boolean canAttack = getY() < targetPlayer.getY() && Math.abs(getX() - targetPlayer.getX()) < 12;
					if(canAttack)
					{
						state = EnumState.ATTACK_DOWN;
						attackTargetY = targetPlayer.getY();
					}
					else
					{
						double prevX = getX();
						if(getX() < targetPlayer.getX())
						{
							setX(Math.min(getX() + MOVE_SPEED, targetPlayer.getX()));
						}
						else if(getX() > targetPlayer.getX())
						{
							setX(Math.max(getX() - MOVE_SPEED, targetPlayer.getX()));
						}
						if(window.isObjectCollidedWith(this, WALL_COLLISION))
						{
							int diff = getX() < prevX ? -1 : 1;
							setX(prevX);
							while(!window.isObjectCollidedWith(this, WALL_COLLISION))
							{
								setX(getX() + diff);
							}
							setX(getX() - diff);
						}
					}
				}
				
				break;
		}
		
		Iterator<PlayerObject> collidedPlayers = window.getObjectsThatCollideWith(this, PlayerObject.class).iterator();
		if(collidedPlayers.hasNext())
		{
			window.failLevel(collidedPlayers.next(), this);
		}
	}
	
	private static enum EnumState
	{
		ATTACK_DOWN, ATTACK_UP, MOVE;
	}
	
}
