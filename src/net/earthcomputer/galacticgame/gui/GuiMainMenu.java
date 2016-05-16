package net.earthcomputer.galacticgame.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.galacticgame.util.Images;
import net.earthcomputer.galacticgame.util.Keyboard;
import net.earthcomputer.galacticgame.util.Profiles;

public class GuiMainMenu extends Gui
{
	private static final BufferedImage logo = Images.loadImage("gui/logo");
	private static final BufferedImage play = Images.loadImage("gui/resume");
	private static final BufferedImage load = Images.loadImage("gui/select_profile");
	private static final BufferedImage help = Images.loadImage("gui/help");
	private static final BufferedImage quit = Images.loadImage("gui/quit");
	
	private static final Font creditsFont = new Font(Font.MONOSPACED, Font.BOLD, 24);
	
	@Override
	public void init()
	{
		this.buttonList.add(new Button(play, width / 2 - play.getWidth() - 20, 150) {
			private int frameCount = 0;
			
			@Override
			protected void onPressed()
			{
				window.openGui(new GuiSelectName(GuiMainMenu.this));
			}
			
			@Override
			public void draw(int mouseX, int mouseY, Graphics g)
			{
				int halfExtraSize = (int) (Math.sin((double) frameCount / 4) * 5);
				g.drawImage(getImage(), getX() - halfExtraSize, getY() - halfExtraSize,
					getWidth() + (halfExtraSize * 2), getHeight() + (halfExtraSize * 2), null);
				if(isHovered(mouseX, mouseY))
				{
					g.setColor(Color.WHITE);
					g.drawRect(getX() - halfExtraSize, getY() - halfExtraSize, getWidth() + (halfExtraSize * 2),
						getHeight() + (halfExtraSize * 2));
				}
				frameCount++;
			}
		});
		if(Profiles.getProfileCount() != 0)
		{
			this.buttonList.add(new Button(load, width / 2 + 20, 150) {
				@Override
				protected void onPressed()
				{
					window.openGui(new GuiSelectProfile(GuiMainMenu.this));
				}
			});
		}
		this.buttonList.add(new Button(help, width / 2 - help.getWidth() - 20, 300) {
			@Override
			protected void onPressed()
			{
				window.openGui(new GuiHelp());
			}
		});
		this.buttonList.add(new Button(quit, width / 2 + 20, 300) {
			@Override
			protected void onPressed()
			{
				githubGame.shutdown();
			}
		});
	}
	
	@Override
	public void drawMiddleLayer(Graphics g)
	{
		g.drawImage(logo, width / 2 - logo.getWidth() / 2, 20, null);
		g.setColor(Color.CYAN.darker().darker());
		g.setFont(creditsFont);
		g.drawString("Programmer + Sounds: Earthcomputer", 2, height - 30);
		g.drawString("Artist: Moses Burton", 2, height - 2);
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