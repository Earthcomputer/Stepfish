package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;

public class GuiPauseMenu extends Gui
{
	
	@Override
	public void drawScreen(Graphics g)
	{
		g.drawString("Paused!", 20, 20);
	}
	
	@Override
	public boolean shouldDrawLevelBackground()
	{
		return true;
	}
	
}
