package net.earthcomputer.githubgame.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedInputStream;

public class FontManager
{
	private static final Font galacticFont;
	
	static
	{
		try
		{
			galacticFont = Font
				.createFont(Font.TRUETYPE_FONT,
					new BufferedInputStream(
						FontManager.class.getResourceAsStream("/misc/standard_galactic_alphabet.ttf")))
				.deriveFont(Font.PLAIN, 14);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private FontManager()
	{
	}
	
	private static String convertStringToDrawable(String nonPrintable)
	{
		char[] charArray = nonPrintable.toCharArray();
		for(int i = 0; i < charArray.length; i++)
		{
			char c = charArray[i];
			while(c < 'A')
			{
				c += 26;
			}
			if(c > 'Z')
			{
				if(c >= 'a' && c <= 'z')
				{
					// Convert to upper case
					c -= ('a' - 'A');
				}
				else
				{
					c = (char) (((c - 'A') % 26) + 'A');
				}
			}
			charArray[i] = c;
		}
		return new String(charArray);
	}
	
	public static int getStringWidth(Graphics g, String text)
	{
		return g.getFontMetrics(galacticFont).stringWidth(convertStringToDrawable(text));
		
	}
	
	public static void drawString(Graphics g, String text, int x, int y)
	{
		g.setFont(galacticFont);
		g.drawString(convertStringToDrawable(text), x, y);
	}
	
}
