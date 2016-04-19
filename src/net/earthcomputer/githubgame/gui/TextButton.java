package net.earthcomputer.githubgame.gui;

import java.awt.Color;
import java.awt.Graphics;

public abstract class TextButton extends Button
{
	
	private String text;
	
	public TextButton(String text, int x, int y, int width, int height)
	{
		super(x, y, width, height);
		this.text = text;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, Graphics g)
	{
		if(isHovered(mouseX, mouseY))
		{
			g.setColor(new Color(0.6f, 0.6f, 1f, 0.3f));
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 10, 10);
		}
		g.setColor(Color.WHITE);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 10, 10);
		
		FontManager.drawString(g, text, getX() + getWidth() / 2 - FontManager.getStringWidth(g, text) / 2, getY() + 35);
	}
	
	public String getText()
	{
		return text;
	}
	
}
