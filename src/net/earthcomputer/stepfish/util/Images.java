package net.earthcomputer.stepfish.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Images
{
	
	private static final Map<String, BufferedImage> cache = new HashMap<String, BufferedImage>();
	
	private Images()
	{
	}
	
	public static BufferedImage loadImage(String key)
	{
		if(cache.containsKey(key)){ return cache.get(key); }
		
		BufferedImage image = null;
		
		InputStream resource = Images.class.getResourceAsStream(String.format("/textures/%s.png", key));
		if(resource != null)
		{
			try
			{
				image = ImageIO.read(new BufferedInputStream(resource));
			}
			catch (IOException e)
			{
				// Image is still null, this is handled a little further down
			}
		}
		
		cache.put(key, image);
		
		if(image == null)
		{
			System.err.printf("Unable to load image %s\n", key);
		}
		
		return image;
	}
	
}
