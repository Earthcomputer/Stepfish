package net.earthcomputer.githubgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.util.Keyboard;

public class GuiScrollable extends Gui
{
	private static final int SCROLL_BAR_WIDTH = 7;
	
	private Gui prevGui;
	private int amtScrolled;
	private int maxScroll;
	private int scrollBarTop;
	private int scrollBarBottom;
	private int scrollBarHeight;
	private int contentHeight;
	private int firstMouseY = -1;
	private int firstScrollBarPos = -1;
	
	public GuiScrollable(Gui prevGui, int contentHeight)
	{
		this.prevGui = prevGui;
		this.contentHeight = contentHeight;
	}
	
	@Override
	protected void init()
	{
		recalcFields();
	}
	
	private void recalcFields()
	{
		maxScroll = contentHeight - height;
		if(amtScrolled > maxScroll) amtScrolled = maxScroll;
		if(maxScroll < 0) maxScroll = 0;
		scrollBarHeight = height * height / contentHeight;
		if(scrollBarHeight > height) scrollBarHeight = height;
		scrollBarTop = (maxScroll == 0 ? 0 : (height - scrollBarHeight) * amtScrolled / maxScroll);
		scrollBarBottom = scrollBarTop + scrollBarHeight;
	}
	
	public void setScroll(int amtScrolled)
	{
		if(amtScrolled < 0) amtScrolled = 0;
		if(amtScrolled > maxScroll) amtScrolled = maxScroll;
		this.amtScrolled = amtScrolled;
		recalcFields();
	}
	
	public void scrollBy(int amt)
	{
		setScroll(amtScrolled + amt);
	}
	
	public int getAmountScrolled()
	{
		return amtScrolled;
	}
	
	@Override
	public void drawScreen(Graphics g)
	{
		if(!shouldDrawLevelBackground())
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
		}
		
		g.translate(0, -amtScrolled);
		
		drawMiddleLayer(g);
		
		Point mousePos = GithubGame.getInstance().getWindow().getMouseLocation();
		
		for(Button button : buttonList)
		{
			button.draw(mousePos.x, mousePos.y + amtScrolled, g);
		}
		
		g.translate(0, amtScrolled);
		
		g.setColor(Color.BLUE);
		g.fillRect(width - SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH, height);
		g.setColor(Color.CYAN.darker());
		g.fillRect(width - SCROLL_BAR_WIDTH, scrollBarTop, SCROLL_BAR_WIDTH - 1, scrollBarHeight);
		g.setColor(Color.CYAN);
		g.drawRect(width - SCROLL_BAR_WIDTH, scrollBarTop, SCROLL_BAR_WIDTH - 1, scrollBarHeight);
	}
	
	@Override
	public void mousePressed(int x, int y, int button)
	{
		if(button == MouseEvent.BUTTON1)
		{
			if(x >= width - SCROLL_BAR_WIDTH && x < width)
			{
				if(y < scrollBarTop || y >= scrollBarBottom)
				{
					setScroll((y - scrollBarHeight / 2) * height / scrollBarHeight);
				}
				firstScrollBarPos = amtScrolled;
				firstMouseY = y;
			}
			else
			{
				mousePressedInView(x, y - amtScrolled, button);
			}
		}
	}
	
	@Override
	public void mouseReleased(int x, int y, int button)
	{
		firstScrollBarPos = firstMouseY = -1;
	}
	
	@Override
	public void mouseScrolled(float amt)
	{
		scrollBy((int) (amt * 40));
	}
	
	protected void mousePressedInView(int x, int y, int button)
	{
		super.mousePressed(x, y, button);
	}
	
	@Override
	public void updateTick()
	{
		if(Keyboard.isKeyPressed("closeGui"))
		{
			window.openGui(prevGui);
		}
		else
		{
			if(firstScrollBarPos != -1)
			{
				int dy = window.getMouseLocation().y - firstMouseY;
				float multiplier = (float) height / scrollBarHeight;
				setScroll(firstScrollBarPos + (int) Math.ceil(dy * multiplier));
			}
		}
	}
}
