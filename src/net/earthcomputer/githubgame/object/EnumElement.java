package net.earthcomputer.githubgame.object;

import java.awt.Color;

public enum EnumElement
{
	EARTH(Color.GREEN.darker()), WATER(Color.BLUE), AIR(Color.CYAN.darker()), FIRE(Color.ORANGE);
	
	private final Color color;
	
	private EnumElement(Color color)
	{
		this.color = color;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public EnumElement nextElement()
	{
		EnumElement[] values = values();
		return values[(ordinal() + 1) % values.length];
	}
}
