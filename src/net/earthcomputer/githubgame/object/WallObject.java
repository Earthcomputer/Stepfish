package net.earthcomputer.githubgame.object;

public class WallObject extends GameObject
{

	private EnumElement element;
	
	public WallObject(double x, double y)
	{
		this(x, y, null);
	}
	
	public WallObject(double x, double y, EnumElement element) {
		super(x, y);
		this.element = element;
	}

}
