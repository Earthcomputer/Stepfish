package net.earthcomputer.stepfish.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.earthcomputer.stepfish.Stepfish;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.Keyboard;

public class GuiScrollable extends Gui
{
	private static final BufferedImage background = Images.loadImage("gui/back_gui");
	
	private static final int SCROLL_BAR_WIDTH = 7;
	
	protected Gui prevGui;
	private int amtScrolled;
	private int maxScroll;
	private int scrollBarTop;
	private int scrollBarBottom;
	private int scrollBarHeight;
	private int contentHeight;
	private int firstMouseY = -1;
	private int firstScrollBarPos = -1;
	
	protected List<Button> staticButtonList = Collections.synchronizedList(new ArrayList<Button>());
	
	public GuiScrollable(Gui prevGui, int contentHeight)
	{
		this.prevGui = prevGui;
		this.contentHeight = contentHeight;
	}
	
	@Override
	protected void init()
	{
		recalcFields();
		staticButtonList.clear();
	}
	
	private void recalcFields()
	{
		maxScroll = contentHeight - height;
		if(maxScroll < 0) maxScroll = 0;
		if(amtScrolled > maxScroll) amtScrolled = maxScroll;
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
	
	public int getContentHeight()
	{
		return contentHeight;
	}
	
	public void setContentHeight(int contentHeight)
	{
		this.contentHeight = contentHeight;
		recalcFields();
	}
	
	@Override
	public void drawScreen(Graphics g)
	{
		if(!shouldDrawLevelBackground())
		{
			g.drawImage(background, 0, 0, width, height, null);
		}
		
		Point mousePos = Stepfish.getInstance().getWindow().getMouseLocation();
		
		synchronized(staticButtonList)
		{
			for(Button button : staticButtonList)
			{
				button.draw(mousePos.x, mousePos.y, g);
			}
		}
		
		g.translate(0, -amtScrolled);
		
		drawMiddleLayer(g);
		
		synchronized(buttonList)
		{
			for(Button button : buttonList)
			{
				button.draw(mousePos.x, mousePos.y + amtScrolled, g);
			}
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
		synchronized(staticButtonList)
		{
			for(Button button1 : staticButtonList)
			{
				if(button1.mousePressed(x, y, button)){ return; }
			}
		}
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
				mousePressedInView(x, y + amtScrolled, button);
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
			if(Keyboard.isKeyDown("scrollUp"))
			{
				scrollBy(-5);
			}
			if(Keyboard.isKeyDown("scrollDown"))
			{
				scrollBy(5);
			}
			if(firstScrollBarPos != -1)
			{
				int dy = window.getMouseLocation().y - firstMouseY;
				float multiplier = (float) height / scrollBarHeight;
				setScroll(firstScrollBarPos + (int) Math.ceil(dy * multiplier));
			}
		}
	}
}
