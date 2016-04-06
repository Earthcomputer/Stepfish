package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.githubgame.GithubGame;
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
		
		if(getY() >= GithubGame.getInstance().getWindow().getHeight()) accelerateY(-30);
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
