package net.earthcomputer.stepfish.gui;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import net.earthcomputer.stepfish.Stepfish;
import net.earthcomputer.stepfish.util.Profiles;

public class GuiSelectName extends GuiScrollable
{
	
	public GuiSelectName(Gui prevGui)
	{
		super(prevGui, 1);
	}
	
	@Override
	public void init()
	{
		List<String> freeProfileNames = Profiles.getAvailableProfileNameList();
		if(freeProfileNames.isEmpty())
		{
			window.openGui(new GuiMainMenu());
		}
		else
		{
			setContentHeight(10 + freeProfileNames.size() * 80);
		}
		
		super.init();
		
		for(int i = 0; i < freeProfileNames.size(); i++)
		{
			buttonList.add(new ProfileButton(freeProfileNames.get(i), 20, 20 + i * 80, 500, 50) {
				@Override
				public void onPressed()
				{
					window.setProfile(Profiles.createProfile(getText()));
					try
					{
						Profiles.saveProfiles();
					}
					catch (IOException e)
					{
						System.err.println("Error occurred saving profiles after adding new profile");
						JOptionPane.showMessageDialog(null,
							"An error occurred saving your new profile to long-term memory", Stepfish.GAME_NAME,
							JOptionPane.ERROR_MESSAGE);
					}
					window.loadLevel(0);
					window.closeGui();
				}
			});
		}
		staticButtonList.add(new Button("back", 530, 10) {
			@Override
			public void onPressed()
			{
				window.openGui(new GuiMainMenu());
			}
		});
	}
	
}
