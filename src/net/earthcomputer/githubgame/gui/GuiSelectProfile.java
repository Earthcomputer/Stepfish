package net.earthcomputer.githubgame.gui;

import java.util.List;

import net.earthcomputer.githubgame.util.Profiles;

public class GuiSelectProfile extends GuiScrollable
{
	
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
		}
	}
	
}
