package net.earthcomputer.githubgame;

import javax.swing.JComponent;

import net.earthcomputer.githubgame.geom.Pos;

public abstract class GameComponent extends JComponent {

	private static final long serialVersionUID = 4039393389062942365L;

	private Pos pos;

	public GameComponent(float x, float y) {
		this.pos = new Pos(x, y);
		setLocation((int) x, (int) y);
	}

	public Pos getPos() {
		return pos;
	}

	public void setPos(Pos pos) {
		this.pos = pos;
	}

}
