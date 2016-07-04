package net.earthcomputer.stepfish.object;

public enum EnumFacing
{
	
	LEFT(-1), RIGHT(1);
	
	private final int scale;
	
	private EnumFacing(int scale)
	{
		this.scale = scale;
	}
	
	public int getScale()
	{
		return scale;
	}
	
}
