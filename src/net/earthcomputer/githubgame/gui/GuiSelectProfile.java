package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import net.earthcomputer.githubgame.util.Images;
import net.earthcomputer.githubgame.util.Profiles;

public class GuiSelectProfile extends GuiScrollable
{
	
	private static final BufferedImage profilePic = Images.loadImage("profile_pic");
	
	public GuiSelectProfile(Gui prevGui)
	{
		super(prevGui, 10 + Profiles.getProfileCount() * 80);
	}
	
	@Override
	public void init()
	{
		List<String> profileNames = Profiles.getProfileNameList();
		
		for(int i = 0; i < profileNames.size(); i++)
		{
			buttonList.add(new TextButton(profileNames.get(i), 20, 20 + 80 * i, 500, 50) {
				@Override
				public void onPressed()
				{
					window.setProfile(Profiles.getProfileByName(getText()));
					window.openGui(new GuiSelectLevel(GuiSelectProfile.this));
				}
			});
			staticButtonList.add(new Button("back", 530, 80) {
				@Override
				public void onPressed()
				{
					window.openGui(new GuiMainMenu());
				}
			});
		}
	}
	
	@Override
	public void drawScreen(Graphics g)
	{
		super.drawScreen(g);
		g.drawImage(profilePic, 530, 10, null);
	}
	
}
