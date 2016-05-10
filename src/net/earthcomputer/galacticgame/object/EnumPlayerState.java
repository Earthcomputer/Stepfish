package net.earthcomputer.galacticgame.object;

public enum EnumPlayerState
{
	
	STAND(false, true, "stand"), WALK(false, true, "walk"), AIR(true, false, "jump");
	
	private final boolean gravity;
	private final boolean needsSupport;
	private final String name;
	
	private EnumPlayerState(boolean gravity, boolean needsSupport, String name)
	{
		this.gravity = gravity;
		this.needsSupport = needsSupport;
		this.name = name;
	}
	
	public boolean hasGravity()
	{
		return gravity;
	}
	
	public boolean needsSupport()
	{
		return needsSupport;
	}
	
	public String getName()
	{
		return name;
	}
	
}
