package net.earthcomputer.githubgame;

public class Level
{
	
	public final String name;
	public final int width;
	public final int height;
	public final LevelObject[] objects;
	
	public Level(String name, int width, int height, LevelObject[] objects)
	{
		this.width = width;
		this.height = height;
		this.name = name;
		this.objects = objects;
	}
	
	public static class LevelObject
	{
		
		public final int x;
		public final int y;
		public final int id;
		
		public LevelObject(int x, int y, int id)
		{
			this.x = x;
			this.y = y;
			this.id = id;
		}
		
	}
	
}
