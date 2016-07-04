package net.earthcomputer.stepfish.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.earthcomputer.stepfish.Stepfish;
import net.earthcomputer.stepfish.geom.collision.CollisionMask;
import net.earthcomputer.stepfish.object.GameObject;
import net.earthcomputer.stepfish.object.PlayerObject;
import net.earthcomputer.stepfish.util.SoundManager;

public class GuiFail extends Gui
{
	
	private int ticksExisted = 0;
	private PlayerObject player;
	private GameObject cause;
	
	public GuiFail(PlayerObject player, GameObject cause)
	{
		this.player = player;
		this.cause = cause;
	}
	
	@Override
	public void drawMiddleLayer(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.RED);
		
		g2.draw(player.getCollisionMask().getGlobalShape());
		if(cause != null)
		{
			CollisionMask<?> mask = cause.getCollisionMask();
			if(mask != null)
			{
				g2.draw(mask.getGlobalShape());
			}
		}
		
	}
	
	@Override
	public void updateTick()
	{
		super.updateTick();
		ticksExisted++;
		if(ticksExisted > Stepfish.TICKRATE / 2)
		{
			window.closeGui();
			SoundManager.playSound("fail");
			window.restartLevel();
		}
	}
	
	@Override
	public boolean shouldDrawLevelBackground()
	{
		return true;
	}
	
}
