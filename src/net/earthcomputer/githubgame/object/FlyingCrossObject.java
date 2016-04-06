package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class FlyingCrossObject extends GameObject
{
	
	private int frames = 0;
	
	public FlyingCrossObject(double x, double y)
	{
		super(x, y);
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
	
}
