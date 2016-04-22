package net.earthcomputer.githubgame.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import net.earthcomputer.githubgame.Levels;

public class GuiSelectLevel extends GuiScrollable
{
	
	public static final Font ENGLISH_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 24);
	
	public GuiSelectLevel(Gui prevGui)
	{
		super(prevGui, 10 + Levels.getLevelCount() * 80);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		for(int i = 0; i < Levels.getLevelCount(); i++)
		{
			final int levelId = i;
			buttonList.add(new TextButton(Levels.getNameById(i), 20, 20 + 80 * i, 500, 50) {
				@Override
				public void onPressed()
				{
					window.loadLevel(levelId);
					window.closeGui();
				}
				
				@Override
				public void draw(int mouseX, int mouseY, Graphics g)
				{
					super.draw(mouseX, mouseY, g);
					g.setFont(ENGLISH_FONT);
					g.drawString((levelId + 1) + ".", getX() + 20, getY() + 35);
					if(levelId > window.getProfile().getCurrentLevel())
					{
						g.setColor(new Color(1f, 0f, 0f, 0.3f));
						g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 10, 10);
					}
				}
				
				@Override
				public boolean isHovered(int mouseX, int mouseY)
				{
					return window.getProfile().getCurrentLevel() >= levelId && super.isHovered(mouseX, mouseY);
				}
			});
		}
	}
	
}
