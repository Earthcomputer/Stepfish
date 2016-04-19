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
	private static final BufferedImage homeImage = Images.loadImage("home");
	
	@Override
	public void init()
	{
		int resumeX = width / 2 - resumeImage.getWidth() / 2;
		this.buttonList.add(new Button(resumeImage, resumeX, height / 2 - resumeImage.getHeight() / 2) {
			@Override
			protected void onPressed()
			{
				window.closeGui();
			}
		});
		this.buttonList.add(new Button(restartImage, resumeX - restartImage.getWidth() - 20,
			height / 2 - restartImage.getHeight() / 2) {
			@Override
			protected void onPressed()
			{
				window.closeGui();
				window.restartLevel();
			}
		});
		this.buttonList
			.add(new Button(homeImage, resumeX + homeImage.getWidth() + 20, height / 2 - homeImage.getHeight() / 2) {
				@Override
				protected void onPressed()
				{
					window.openGui(new GuiMainMenu());
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
