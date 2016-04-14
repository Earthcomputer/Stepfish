package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;

import javax.imageio.ImageIO;

public class GuiPauseMenu extends Gui
{
	
	private static final BufferedImage resumeImage;
	
	static
	{
		try
		{
			resumeImage = ImageIO
				.read(new BufferedInputStream(GuiPauseMenu.class.getResourceAsStream("/textures/resume.png")));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
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
	}
	
	@Override
	public void drawScreen(Graphics g)
	{
		super.drawScreen(g);
		g.drawString("Paused!", 20, 20);
	}
	
	@Override
	public boolean shouldDrawLevelBackground()
	{
		return true;
	}
	
}
