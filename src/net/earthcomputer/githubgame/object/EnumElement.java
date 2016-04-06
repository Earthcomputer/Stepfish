package net.earthcomputer.githubgame.object;

import java.awt.Color;

public enum EnumElement
{
	EARTH(new Color(165, 42, 42)), WATER(Color.BLUE), AIR(Color.CYAN), FIRE(Color.RED);
	
	private final Color color;
	
	private EnumElement(Color color)
	{
		this.color = color;
	}
	
	public Color getColor()
	{
		return color;
	}
}
