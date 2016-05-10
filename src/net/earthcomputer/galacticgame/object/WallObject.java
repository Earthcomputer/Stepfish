package net.earthcomputer.galacticgame.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;
import net.earthcomputer.galacticgame.util.Images;

public class WallObject extends GameObject
{
	
	private EnumElement element;
	
	private static final BufferedImage plainTexture = Images.loadImage("object/wall");
	private static final EnumMap<EnumElement, BufferedImage> texturesByElement = new EnumMap<EnumElement, BufferedImage>(
		EnumElement.class);
		
	static
	{
		for(EnumElement element : EnumElement.values())
		{
			texturesByElement.put(element, Images.loadImage("object/wall_" + element.getName()));
		}
	}
	
	public WallObject(double x, double y)
	{
		this(x, y, null);
	}
	
	public WallObject(double x, double y, EnumElement element)
	{
		super(x, y);
		this.element = element;
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		if(element == null)
		{
			g.drawImage(plainTexture, x, y, null);
		}
		else
		{
			g.drawImage(texturesByElement.get(element), x, y, null);
		}
	}
	
	public EnumElement getElement()
	{
		return element;
	}
	
}
