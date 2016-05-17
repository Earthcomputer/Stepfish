package net.earthcomputer.galacticgame.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import net.earthcomputer.galacticgame.util.Images;

public class GuiHelp extends Gui
{
	
	private static final int[] JUMP_HEIGHTS = new int[17];
	
	static
	{
		int yvel = -8;
		int currentJumpHeight = 0;
		for(int i = 0; i < JUMP_HEIGHTS.length; i++)
		{
			JUMP_HEIGHTS[i] = currentJumpHeight;
			currentJumpHeight += yvel;
			yvel++;
		}
	}
	
	private static final BufferedImage playerStand = Images.loadImage("object/player_earth_stand");
	private static final BufferedImage[] playerWalkRight = new BufferedImage[] {
			Images.loadImage("object/player_earth_walk_0"), Images.loadImage("object/player_earth_walk_1") };
	private static final BufferedImage[] playerWalkLeft = new BufferedImage[2];
	private static final BufferedImage star = Images.loadImage("object/star");
	
	static
	{
		for(int i = 0; i < playerWalkLeft.length; i++)
		{
			AffineTransform scaler = AffineTransform.getScaleInstance(-1, 1);
			scaler.translate(-playerWalkRight[i].getWidth(), 0);
			AffineTransformOp imageScaler = new AffineTransformOp(scaler, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			playerWalkLeft[i] = imageScaler.filter(playerWalkRight[i], null);
		}
	}
	
	private static final BufferedImage playerAir = Images.loadImage("object/player_earth_jump");
	private static final int TICKS_PER_FRAME = 5;
	
	private static final String KEY_SPACE = KeyEvent.getKeyText(KeyEvent.VK_SPACE);
	private static final String KEY_UP = KeyEvent.getKeyText(KeyEvent.VK_UP);
	private static final String KEY_W = KeyEvent.getKeyText(KeyEvent.VK_W);
	private static final String KEY_LEFT = KeyEvent.getKeyText(KeyEvent.VK_LEFT);
	private static final String KEY_A = KeyEvent.getKeyText(KeyEvent.VK_A);
	private static final String KEY_RIGHT = KeyEvent.getKeyText(KeyEvent.VK_RIGHT);
	private static final String KEY_D = KeyEvent.getKeyText(KeyEvent.VK_D);
	
	private int ticksExisted = 0;
	
	@Override
	public void init()
	{
		buttonList.add(new Button("back", 2, 2) {
			@Override
			protected void onPressed()
			{
				window.openGui(new GuiMainMenu());
			}
		});
	}
	
	@Override
	public void drawMiddleLayer(Graphics g)
	{
		g.setColor(Color.WHITE);
		if((ticksExisted + 26) % 64 <= 32)
		{
			g.drawImage(star, 100, 102, null);
		}
		if(ticksExisted % 64 <= 16)
		{
			int ticksSinceStartJump = ticksExisted % 32;
			g.drawImage(playerAir, 100, 150 + JUMP_HEIGHTS[ticksSinceStartJump], null);
		}
		else
		{
			g.drawImage(playerStand, 100, 150, null);
		}
		FontManager.drawPlainString(g, KEY_SPACE + " / " + KEY_UP + " / " + KEY_W, 150, 150);
		
		g.clipRect(76, 200, 64, 16);
		g.drawImage(playerWalkLeft[(ticksExisted / TICKS_PER_FRAME) % 2], 140 - (ticksExisted % 40) * 3, 200, null);
		g.setClip(null);
		FontManager.drawPlainString(g, KEY_LEFT + " / " + KEY_A, 150, 215);
		
		g.clipRect(76, 250, 64, 16);
		g.drawImage(playerWalkRight[(ticksExisted / TICKS_PER_FRAME) % 2], 60 + (ticksExisted % 40) * 3, 250, null);
		g.setClip(null);
		FontManager.drawPlainString(g, KEY_RIGHT + " / " + KEY_D, 150, 265);
	}
	
	@Override
	public void updateTick()
	{
		super.updateTick();
		ticksExisted++;
	}
	
}
