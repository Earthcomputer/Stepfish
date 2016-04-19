package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.util.Images;
import net.earthcomputer.githubgame.util.Keyboard;

public class GuiMainMenu extends Gui
{
	private static final BufferedImage logo = Images.loadImage("logo");
	private static final BufferedImage play = Images.loadImage("resume");
	
	@Override
	public void init()
	{
		this.buttonList.add(new Button(play, width / 2 - play.getWidth() - 20, 150) {
			private int frameCount = 0;
			
			@Override
			protected void onPressed()
			{
				window.openGui(new GuiSelectName());
			}
			
			@Override
			public void draw(Graphics g)
			{
				int halfExtraSize = (int) (Math.sin((double) frameCount / 4) * 5);
				g.drawImage(getImage(), getX() - halfExtraSize, getY() - halfExtraSize,
					getWidth() + (halfExtraSize * 2), getHeight() + (halfExtraSize * 2), null);
				frameCount++;
			}
		});
	}
	
	@Override
	public void drawMiddleLayer(Graphics g)
	{
		g.drawImage(logo, width / 2 - logo.getWidth() / 2, 20, null);
	}
	
	@Override
	public void updateTick()
	{
		if(Keyboard.isKeyPressed("closeGui"))
		{
			githubGame.shutdown();
		}
	}
}