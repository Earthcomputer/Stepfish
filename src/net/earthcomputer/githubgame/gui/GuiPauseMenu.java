package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import net.earthcomputer.githubgame.util.Images;

public class GuiPauseMenu extends Gui
{
	
	private static final BufferedImage backgroundImage = Images.loadImage("pause_background");
	private static final BufferedImage resumeImage = Images.loadImage("resume");
	private static final BufferedImage restartImage = Images.loadImage("restart");
	
	@Override
	public void init()
	{
		this.buttonList.add(new Button(resumeImage, width / 2 - resumeImage.getWidth() / 2, 100) {
			@Override
			protected void onPressed()
			{
				window.closeGui();
			}
		});
		this.buttonList.add(new Button(restartImage, width / 2 - restartImage.getWidth() / 2, 250) {
			@Override
			protected void onPressed()
			{
				window.closeGui();
				window.restartLevel();
			}
		});
	}
	
	@Override
	protected void drawMiddleLayer(Graphics g)
	{
		Rectangle clipBounds = g.getClipBounds();
		int x = (int) clipBounds.getCenterX() - backgroundImage.getWidth() / 2;
		int y = (int) clipBounds.getCenterY() - backgroundImage.getHeight() / 2;
		g.drawImage(backgroundImage, x, y, null);
	}
	
	@Override
	public boolean shouldDrawLevelBackground()
	{
		return true;
	}
	
}
