package net.earthcomputer.githubgame;

import java.awt.Graphics;

import net.earthcomputer.githubgame.geom.Velocity;

public class PlayerObject extends PhysicsObject {

	private EnumPlayerState state;
	private int gravityId;

	public PlayerObject(float x, float y) {
		super(x, y);
		changeState(EnumPlayerState.AIR);
	}

	public void changeState(EnumPlayerState newState) {
		this.state = newState;
		if (newState.hasGravity()) {
			gravityId = addGravity(Velocity.createFromSpeedAndDirection(1, 90));
		} else {
			removeGravity(gravityId);
		}
	}

	@Override
	public void update() {
		super.update();

		if (GithubGame.getInstance().isKeyDown("moveLeft")) {
			setXPos(getXPos() - 10);
		}

		if (GithubGame.getInstance().isKeyDown("moveRight")) {
			setXPos(getXPos() + 10);
		}

		if (getYPos() >= GithubGame.getInstance().getWindow().getHeight())
			accelerateY(-30);
	}

	@Override
	public void draw(Graphics g) {
		g.fillRect((int) getXPos(), (int) getYPos(), 20, 20);
	}

}
