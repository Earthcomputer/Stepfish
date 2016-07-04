package net.earthcomputer.stepfish.gui;

import java.awt.Color;
import java.awt.Graphics;

import net.earthcomputer.stepfish.Stepfish;
import net.earthcomputer.stepfish.util.Keyboard;

public class GuiCompleteGame extends Gui
{
	
	private int frameCount = 0;
	
	@Override
	public void init()
	{
		buttonList.add(new Button("done", 430, 350) {
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
		g.setColor(Color.getHSBColor((float) frameCount / (Stepfish.FRAMERATE * 6), 1f, 1f));
		String text = "You completed the game!";
		FontManager.drawString(g, text, width / 2 - FontManager.getStringWidth(g, text) / 2, height / 2 + 12);
		frameCount++;
	}
	
	@Override
	public void updateTick()
	{
		if(Keyboard.isKeyPressed("closeGui"))
		{
			window.openGui(new GuiMainMenu());
		}
	}
	
}
