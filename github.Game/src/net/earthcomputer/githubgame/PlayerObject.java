package net.earthcomputer.githubgame;

import java.awt.Graphics;

import net.earthcomputer.githubgame.geom.Velocity;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;

public class PlayerObject extends PhysicsObject {

	private EnumPlayerState state;
	private Velocity downwardsGravity;
	private int downwardsGravityId;

	public PlayerObject(float x, float y) {
		super(x, y);
		downwardsGravity = Velocity.createFromSpeedAndDirection(0, 90);
		downwardsGravityId = addGravity(downwardsGravity);
		changeState(EnumPlayerState.AIR);
		setCollisionMask(new MaskRectangle(20, 20));
	}

	public void changeState(EnumPlayerState newState) {
		this.state = newState;
		if (newState.hasGravity()) {
			downwardsGravity.setSpeed(1);
		} else {
			downwardsGravity.setSpeed(0);
		}
		updateGravity(downwardsGravityId, downwardsGravity);
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
