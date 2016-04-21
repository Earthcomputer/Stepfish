package net.earthcomputer.githubgame.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.earthcomputer.githubgame.GithubGame;
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
			final String profileName = profileNames.get(i);
			buttonList.add(new TextButton(profileName, 20, 20 + 80 * i, 500, 50) {
				@Override
				public void onPressed()
				{
					window.setProfile(Profiles.getProfileByName(getText()));
					window.openGui(new GuiSelectLevel(GuiSelectProfile.this));
				}
			});
			buttonList.add(new Button("delete", 530, 29 + 80 * i) {
				@Override
				public void onPressed()
				{
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run()
						{
							Profiles.deleteProfile(profileName);
							try
							{
								Profiles.saveProfiles();
							}
							catch (IOException e)
							{
								System.err.println("Error saving deleted profile");
								JOptionPane.showMessageDialog(null,
									"An error occurred saving this action to long-term memory", GithubGame.GAME_NAME,
									JOptionPane.ERROR_MESSAGE);
								return;
							}
							setContentHeight(getContentHeight() - 80);
							validate(width, height);
							if(Profiles.getProfileCount() == 0)
							{
								window.openGui(new GuiMainMenu());
							}
						}
					});
				}
			});
		}
		staticButtonList.add(new Button("back", 565, 80) {
			@Override
			public void onPressed()
			{
				window.openGui(new GuiMainMenu());
			}
		});
	}
	
	@Override
	public void drawScreen(Graphics g)
	{
		super.drawScreen(g);
		g.drawImage(profilePic, 565, 10, null);
	}
	
}
