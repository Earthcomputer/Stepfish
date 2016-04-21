package net.earthcomputer.githubgame.gui;

import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.util.Profiles;

public class GuiSelectName extends GuiScrollable
{
	
	public GuiSelectName(Gui prevGui)
	{
		super(prevGui, 810);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		Random random = new Random();
		for(int i = 0; i < 10; i++)
		{
			buttonList.add(new TextButton(GithubGame.randomGenTitle(random.nextInt()), 20, 20 + i * 80, 500, 50) {
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
							"An error occurred saving your new profile to long-term memory", GithubGame.GAME_NAME,
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
