package net.earthcomputer.githubgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import net.earthcomputer.githubgame.util.Keyboard;

public class GuiScrollable extends Gui
{
	private static final int SCROLL_BAR_WIDTH = 6;
	
	private Gui prevGui;
	private int amtScrolled;
	private int maxScroll;
	private int scrollBarTop;
	private int scrollBarBottom;
	private int scrollBarHeight;
	private int contentHeight;
	private int firstMouseY;
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
		g.setColor(Color.BLUE.darker());
		g.fillRect(width - SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH, height);
		g.setColor(Color.BLUE);
		g.fillRect(width - SCROLL_BAR_WIDTH, scrollBarTop, SCROLL_BAR_WIDTH, scrollBarHeight);
		g.setColor(Color.CYAN);
		g.drawRect(width - SCROLL_BAR_WIDTH, scrollBarTop, SCROLL_BAR_WIDTH, scrollBarHeight);
		
		g.translate(0, -amtScrolled);
		
		super.drawScreen(g);
	}
	
	@Override
	public void mousePressed(int x, int y, int button)
	{
		if(button == MouseEvent.BUTTON1)
		{
			firstScrollBarPos = amtScrolled;
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
				mousePressedInView(x, y, button);
			}
		}
	}
	
	protected void mousePressedInView(int x, int y, int button)
	{
	
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
