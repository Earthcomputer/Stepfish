package net.earthcomputer.githubgame.object;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.earthcomputer.githubgame.GithubGame;
import net.earthcomputer.githubgame.IUpdateListener;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class StarObject extends GameObject implements IUpdateListener
{
	
	public static final BufferedImage STAR_TEXTURE;
	
	static
	{
		try
		{
			STAR_TEXTURE = ImageIO
				.read(new BufferedInputStream(StarObject.class.getResourceAsStream("/textures/star.png")));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public StarObject(double x, double y)
	{
		super(x, y);
		setCollisionMask(new MaskRectangle(16, 16));
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(STAR_TEXTURE, (int) getX(), (int) getY(), null);
	}
	
	@Override
	public void update()
	{
		List<GameObject> collisionsHere = GithubGame.getInstance().getWindow().getObjectsThatCollideWith(this);
		for(GameObject collision : collisionsHere)
		{
			if(collision instanceof PlayerObject)
			{
				GithubGame.getInstance().getWindow().removeObject(this);
				break;
			}
		}
	}
	
}
