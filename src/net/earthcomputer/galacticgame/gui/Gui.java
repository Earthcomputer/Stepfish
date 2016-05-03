package net.earthcomputer.galacticgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.earthcomputer.galacticgame.GalacticGame;
import net.earthcomputer.galacticgame.MainWindow;
import net.earthcomputer.galacticgame.util.Keyboard;

public abstract class Gui
{
	
	protected GalacticGame githubGame;
	protected MainWindow window;
	protected int width;
	protected int height;
	
	protected List<Button> buttonList = Collections.synchronizedList(new ArrayList<Button>());
	
	protected Gui()
	{
		githubGame = GalacticGame.getInstance();
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
		
		Point mousePos = GalacticGame.getInstance().getWindow().getMouseLocation();
		
		synchronized(buttonList)
		{
			for(Button button : buttonList)
			{
				button.draw(mousePos.x, mousePos.y, g);
			}
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
		synchronized(buttonList)
		{
			for(Button button1 : buttonList)
			{
				button1.mousePressed(x, y, button);
			}
		}
	}
	
	public void mouseReleased(int x, int y, int button)
	{
	}
	
	public void mouseScrolled(float amt)
	{
	}
	
	public void updateTick()
	{
		if(Keyboard.isKeyPressed("closeGui")) window.closeGui();
	}
	
}
