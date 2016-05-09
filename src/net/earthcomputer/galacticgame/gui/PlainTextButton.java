package net.earthcomputer.galacticgame.gui;

import java.awt.Color;
import java.awt.Graphics;

public abstract class PlainTextButton extends TextButton
{
	
	public PlainTextButton(String text, int x, int y, int width, int height)
	{
		super(text, x, y, width, height);
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
		
		FontManager.drawPlainString(g, getText(),
			getX() + getWidth() / 2 - FontManager.getPlainStringWidth(g, getText()) / 2, getY() + 35);
	}
	
}
