package net.earthcomputer.galacticgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import net.earthcomputer.galacticgame.util.Images;

public class GuiPauseMenu extends Gui
{
	
	private static final BufferedImage resumeImage = Images.loadImage("gui/resume");
	private static final BufferedImage restartImage = Images.loadImage("gui/restart");
	private static final BufferedImage homeImage = Images.loadImage("gui/home");
	
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
				window.restartLevel(false);
			}
		});
		this.buttonList
			.add(new Button(homeImage, resumeX + homeImage.getWidth() + 20, height / 2 - homeImage.getHeight() / 2) {
				@Override
				protected void onPressed()
				{
					window.setNoLevel();
					window.openGui(new GuiMainMenu());
				}
			});
	}
	
	@Override
	protected void drawMiddleLayer(Graphics g)
	{
		Rectangle clipBounds = g.getClipBounds();
		g.setColor(new Color(128, 128, 128, 128));
		g.fillRect((int) clipBounds.getX(), (int) clipBounds.getY(), (int) clipBounds.getWidth(),
			(int) clipBounds.getHeight());
	}
	
	@Override
	public boolean shouldDrawLevelBackground()
	{
		return true;
	}
	
}
