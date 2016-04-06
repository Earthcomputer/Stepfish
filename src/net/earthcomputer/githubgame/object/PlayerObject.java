package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.geom.Pos;
import net.earthcomputer.githubgame.geom.Velocity;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class PlayerObject extends PhysicsObject
{
	
	private EnumPlayerState state;
	private Velocity downwardsGravity;
	private int downwardsGravityId;
	
	public PlayerObject(double x, double y)
	{
		super(x, y);
		downwardsGravity = Velocity.createFromSpeedAndDirection(0, 90);
		downwardsGravityId = addGravity(downwardsGravity);
		changeState(EnumPlayerState.AIR);
		setCollisionMask(new MaskRectangle(16, 16));
		setDepth(-1000);
	}
	
	public void changeState(EnumPlayerState newState)
	{
		this.state = newState;
		if(newState.hasGravity())
		{
			downwardsGravity.setSpeed(1);
		}
		else
		{
			downwardsGravity.setSpeed(0);
		}
		updateGravity(downwardsGravityId, downwardsGravity);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(GithubGame.getInstance().isKeyDown("moveLeft"))
		{
			setX(getX() - 10);
		}
		
		if(GithubGame.getInstance().isKeyDown("moveRight"))
		{
			setX(getX() + 10);
		}
		
		if(GithubGame.getInstance().getWindow().isObjectCollidedWith(this, WallObject.class))
		{
			Pos prevPos = getPreviousPos();
			Pos pos = getPos();
			double movingX = pos.getX() - prevPos.getX();
			double movingY = pos.getY() - prevPos.getY();
			double absDX = Math.abs(movingX);
			double absDY = Math.abs(movingY);
			setPos(prevPos);
			double amtToMove;
			if(absDX < absDY)
			{
				amtToMove = absDY;
				movingX /= absDY;
				movingY = Math.signum(movingY);
			}
			else
			{
				amtToMove = absDX;
				movingY /= absDX;
				movingX = Math.signum(movingX);
			}
			
			Rectangle2D.Double rect = new Rectangle2D.Double(prevPos.getX(), prevPos.getY(), 16, 16);
			
			boolean success = false;
			for(int i = 0; i < amtToMove; i++)
			{
				rect.x += movingX;
				rect.y += movingY;
				if(GithubGame.getInstance().getWindow().isShapeCollidedWith(rect, WallObject.class))
				{
					success = true;
					break;
				}
			}
			if(!success) setPos(pos);
		}
		
		if(GithubGame.getInstance().getWindow()
			.isShapeCollidedWith(new Line2D.Double(getX(), getY() + 17, getX() + 16, getY() + 17), WallObject.class))
		{
			if(!state.needsSupport()) changeState(EnumPlayerState.STAND);
		}
		else
		{
			if(state.needsSupport()) changeState(EnumPlayerState.AIR);
		}
		
		if(getY() >= GithubGame.getInstance().getWindow().getHeight()) System.out.println("You died!");
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, 16, 16);
		g.setColor(Color.RED);
		g.fillRect(x + 8, y + 3, 2, 2);
		g.fillRect(x + 12, y + 3, 2, 2);
		g.fillRect(x + 8, y + 10, 8, 2);
	}
	
}
