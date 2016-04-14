package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import net.earthcomputer.githubgame.util.Images;

public abstract class Button
{
	
	private BufferedImage image;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public Button(String image, int x, int y)
	{
		this(Images.loadImage(image), x, y);
	}
	
	public Button(BufferedImage image, int x, int y)
	{
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(image, x, y, null);
	}
	
	public boolean mousePressed(int x, int y, int button)
	{
		if(button == MouseEvent.BUTTON1)
		{
			if(x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height)
			{
				onPressed();
				return true;
			}
		}
		return false;
	}
	
	protected abstract void onPressed();
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
}
