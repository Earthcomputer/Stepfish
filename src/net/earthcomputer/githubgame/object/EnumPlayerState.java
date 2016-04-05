package net.earthcomputer.githubgame.object;

public enum EnumPlayerState {
	
	STAND(false), WALK(false), AIR(true);
	
	private final boolean gravity;
	
	private EnumPlayerState(boolean gravity) {
		this.gravity = gravity;
	}
	
	public boolean hasGravity() {
		return gravity;
	}
	
}
