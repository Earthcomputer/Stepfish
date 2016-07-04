package net.earthcomputer.stepfish.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.stepfish.Levels;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.Profile;

public class GuiStarsObtained extends GuiScrollable
{
	
	private static final BufferedImage obtainedStar = Images.loadImage("gui/score");
	private static final BufferedImage missingStar = Images.loadImage("gui/star_not_obtained");
	
	private Profile profile;
	
	public GuiStarsObtained(Gui prevGui, Profile profile)
	{
		super(prevGui, 10 + Levels.getLevelCount() * 80);
		this.profile = profile;
	}
	
	@Override
	public void init()
	{
		super.init();
		staticButtonList.add(new Button("back", 565, 10) {
			@Override
			protected void onPressed()
			{
				window.openGui(prevGui);
			}
		});
	}
	
	@Override
	public void drawMiddleLayer(Graphics g)
	{
		g.setColor(Color.WHITE);
		for(int levelNo = 0; levelNo < Levels.getLevelCount(); levelNo++)
		{
			String str = (levelNo + 1) + ".";
			int x = 5;
			FontManager.drawPlainString(g, str, x, 60 + levelNo * 80);
			x += FontManager.getPlainStringWidth(g, str) + 10;
			str = Levels.getNameById(levelNo);
			FontManager.drawString(g, str, x, 60 + levelNo * 80);
			
			for(int starIdx = 0; starIdx < 3; starIdx++)
			{
				g.drawImage(profile.isStarObtained(levelNo, starIdx) ? obtainedStar : missingStar, 450 + 35 * starIdx,
					34 + levelNo * 80, null);
			}
		}
	}
	
}
