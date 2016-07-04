package net.earthcomputer.stepfish.gui;

import java.awt.Graphics;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import net.earthcomputer.stepfish.Stepfish;
import net.earthcomputer.stepfish.util.Profile;
import net.earthcomputer.stepfish.util.Profiles;

public class GuiSelectProfile extends GuiScrollable
{
	
	public GuiSelectProfile(Gui prevGui)
	{
		super(prevGui, 10 + Profiles.getProfileCount() * 80);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		List<String> profileNames = Profiles.getTakenProfileNameList();
		
		for(int i = 0; i < profileNames.size(); i++)
		{
			final String profileName = profileNames.get(i);
			buttonList.add(new ProfileButton(profileName, 20, 20 + 80 * i, 400, 50) {
				@Override
				public void onPressed()
				{
					window.setProfile(Profiles.getProfileByName(getText()));
					window.openGui(new GuiSelectLevel(GuiSelectProfile.this));
				}
			});
			buttonList.add(new Button("delete", 430, 29 + 80 * i) {
				@Override
				public void onPressed()
				{
					window.runLater(new Runnable() {
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
									"An error occurred saving this action to long-term memory", Stepfish.GAME_NAME,
									JOptionPane.ERROR_MESSAGE);
								return;
							}
							setContentHeight(getContentHeight() - 80);
							if(Profiles.getProfileCount() == 0)
							{
								window.openGui(new GuiMainMenu());
							}
							else
							{
								validate(width, height);
							}
						}
					});
				}
			});
			buttonList.add(new Button("score", 470, 29 + 80 * i) {
				private final Profile profile = Profiles.getProfileByName(profileName);
				
				@Override
				protected void onPressed()
				{
					window.openGui(new GuiStarsObtained(GuiSelectProfile.this, profile));
				}
				
				@Override
				public void draw(int mouseX, int mouseY, Graphics g)
				{
					super.draw(mouseX, mouseY, g);
					FontManager.drawPlainString(g, String.valueOf(profile.getTotalStarsObtained()),
						getX() + getWidth() + 10, getY() + 22);
				}
				
			});
		}
		
		staticButtonList.add(new Button("back", 565, 10) {
			@Override
			public void onPressed()
			{
				window.openGui(new GuiMainMenu());
			}
		});
	}
	
}
