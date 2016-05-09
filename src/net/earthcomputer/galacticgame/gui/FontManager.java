package net.earthcomputer.galacticgame.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager
{
	private static final Font galacticFont;
	private static final Font englishFont = new Font(Font.MONOSPACED, Font.PLAIN, 24);
	
	static
	{
		try
		{
			Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
			attributes.put(TextAttribute.TRACKING, 0.5);
			galacticFont = Font
				.createFont(Font.TRUETYPE_FONT,
					new BufferedInputStream(
						FontManager.class.getResourceAsStream("/misc/standard_galactic_alphabet.ttf")))
				.deriveFont(Font.PLAIN, 24).deriveFont(attributes);
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
			while(c > ' ' && c < 'A')
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
	
	public static int getPlainStringWidth(Graphics g, String text)
	{
		return g.getFontMetrics(englishFont).stringWidth(text);
	}
	
	public static void drawPlainString(Graphics g, String text, int x, int y)
	{
		g.setFont(englishFont);
		g.drawString(text, x, y);
	}
	
}
