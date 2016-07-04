package net.earthcomputer.stepfish.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.earthcomputer.stepfish.util.Images;

public abstract class ProfileButton extends PlainTextButton
{
	
	private static final BufferedImage profilePic = Images.loadImage("gui/profile_pic");
	
	public ProfileButton(String text, int x, int y, int width, int height)
	{
		super(text, x, y, width, height);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, Graphics g)
	{
		super.draw(mouseX, mouseY, g);
		int imageX = getWidth() / 2 - FontManager.getPlainStringWidth(g, getText()) / 2 - profilePic.getWidth() - 10;
		int imageY = getHeight() / 2 - profilePic.getHeight() / 2;
		g.drawImage(profilePic, getX() + imageX, getY() + imageY, null);
	}
	
}
