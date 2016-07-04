package net.earthcomputer.stepfish.object;

import java.awt.Color;

public enum EnumElement
{
	EARTH(Color.GREEN.darker(), "earth"), WATER(Color.BLUE, "water"), AIR(Color.CYAN.darker(),
		"air"), FIRE(Color.ORANGE, "fire");
		
	private final Color color;
	private final String name;
	
	private EnumElement(Color color, String name)
	{
		this.color = color;
		this.name = name;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public String getName()
	{
		return name;
	}
	
	public EnumElement nextElement()
	{
		EnumElement[] values = values();
		return values[(ordinal() + 1) % values.length];
	}
}
