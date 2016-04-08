package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class FlyingCrossObject extends GameObject implements IUpdateListener
{
	private static final int MOVE_SPEED = 3;
	
	private int frames = 0;
	private double startingY;
	private double attackTargetY;
	private EnumState state;
	
	public FlyingCrossObject(double x, double y)
	{
		super(x, y);
		this.startingY = y;
		this.state = EnumState.MOVE;
		setCollisionMask(new MaskRectangle(1, 1, 14, 14));
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.setColor(Color.WHITE);
		if(frames % GithubGame.FRAMERATE < GithubGame.FRAMERATE / 2)
		{
			g.drawLine(x + 3, y + 3, x + 13, y + 13);
			g.drawLine(x + 3, y + 13, x + 13, y + 3);
		}
		else
		{
			g.drawLine(x + 8, y + 1, x + 8, y + 15);
			g.drawLine(x + 1, y + 8, x + 15, y + 8);
		}
		frames++;
	}
	
	@Override
	public void update()
	{
		switch(state)
		{
			case ATTACK_DOWN:
				if(getY() < attackTargetY)
				{
					setY(Math.min(getY() + MOVE_SPEED, attackTargetY));
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
					boolean canAttack = getY() < targetPlayer.getY() && Math.abs(getX() - targetPlayer.getX()) < 16;
					if(canAttack)
					{
						state = EnumState.ATTACK_DOWN;
						attackTargetY = targetPlayer.getY();
					}
					else
					{
						if(getX() < targetPlayer.getX())
						{
							setX(Math.min(getX() + MOVE_SPEED, targetPlayer.getX()));
						}
						else if(getX() > targetPlayer.getX())
						{
							setX(Math.max(getX() - MOVE_SPEED, targetPlayer.getX()));
						}
					}
				}
				
				break;
		}
		
		if(window.isObjectCollidedWith(this, PlayerObject.class)) window.restartLevel();
	}
	
	private static enum EnumState
	{
		ATTACK_DOWN, ATTACK_UP, MOVE;
	}
	
}
