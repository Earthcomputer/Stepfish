package net.earthcomputer.githubgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.MainWindow;

public abstract class Gui
{
	
	protected GithubGame githubGame;
	protected MainWindow window;
	protected int width;
	protected int height;
	
	protected List<Button> buttonList = new ArrayList<Button>();
	
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
		
		this.buttonList.clear();
		
		init();
	}
	
	public void drawScreen(Graphics g)
	{
		if(!shouldDrawLevelBackground())
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
		}
		
		drawMiddleLayer(g);
		
		for(Button button : buttonList)
		{
			button.draw(g);
		}
	}
	
	protected void drawMiddleLayer(Graphics g)
	{
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
		for(Button button1 : buttonList)
		{
			button1.mousePressed(x, y, button);
		}
	}
	
	public void mouseReleased(int x, int y, int button)
	{
	}
	
	public void updateTick()
	{
		if(githubGame.isKeyPressed("closeGui")) window.closeGui();
	}
	
}
