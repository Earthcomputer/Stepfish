package net.earthcomputer.githubgame.object;

public enum EnumPlayerState
{
	
	STAND(false, true), WALK(false, true), AIR(true, false);
	
	private final boolean gravity;
	private final boolean needsSupport;
	
	private EnumPlayerState(boolean gravity, boolean needsSupport)
	{
		this.gravity = gravity;
		this.needsSupport = needsSupport;
	}
	
	public boolean hasGravity()
	{
		return gravity;
	}
	
	public boolean needsSupport()
	{
		return needsSupport;
	}
	
}
