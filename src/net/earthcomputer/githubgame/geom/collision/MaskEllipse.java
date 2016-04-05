package net.earthcomputer.githubgame.geom.collision;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import net.earthcomputer.githubgame.geom.Pos;

/**
 * An implementation of the collision mask in the shape of an ellipse
 * 
 * @author Earthcomputer
 *
 */
public class MaskEllipse implements ICollisionMask {

	private Ellipse2D.Double ellipse;
	private Pos localPos;

	public MaskEllipse(Ellipse2D ellipse) {
		this.ellipse = new Ellipse2D.Double(ellipse.getX(), ellipse.getY(), ellipse.getWidth(),
				ellipse.getHeight());
		localPos = new Pos(this.ellipse.x, this.ellipse.y);
	}

	@Override
	public Shape getGlobalShape() {
		return ellipse;
	}

	@Override
	public void setLocalPosition(Pos pos) {
		localPos = Pos.copyOf(pos);
	}

	@Override
	public void setGlobalPosition(Pos pos) {
		pos = Pos.copyOf(pos);
		pos.add(localPos);
		ellipse.x = pos.getX();
		ellipse.y = pos.getY();
	}

	@Override
	public Pos getLocalPosition() {
		return localPos;
	}

	@Override
	public Pos getGlobalPosition() {
		return new Pos(ellipse.x, ellipse.y);
	}

}
