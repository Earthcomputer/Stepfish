package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.MainWindow;

public abstract class Gui
{
	
	protected GithubGame githubGame;
	protected MainWindow window;
	protected int width;
	protected int height;
	
	protected Gui()
	{
		githubGame = GithubGame.getInstance();
		window = githubGame.getWindow();
	}
	
	protected void init()
	{
	}
	
	public void onClosed()
	{
	}
	
	public void validate(int width, int height)
	{
		this.width = width;
		this.height = height;
		init();
	}
	
	public void drawScreen(Graphics g)
	{
		g.drawRect(0, 0, width, height);
	}
	
	public boolean shouldDrawLevelBackground()
	{
		return false;
	}
	
	public boolean pausesGame()
	{
		return true;
	}
	
	public void mousePressed(int x, int y, int button)
	{
	}
	
	public void mouseReleased(int x, int y, int button)
	{
	}
	
	public void updateTick()
	{
		if(githubGame.isKeyPressed("closeGui")) window.closeGui();
	}
	
}
