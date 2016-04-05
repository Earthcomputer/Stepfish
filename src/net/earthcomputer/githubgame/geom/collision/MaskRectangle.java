package net.earthcomputer.githubgame.geom.collision;

import java.awt.Rectangle;
import java.awt.Shape;

import net.earthcomputer.githubgame.geom.Pos;

/**
 * 
 * An implementation of a collision mask in the shape of a rectangle
 * 
 * @author Earthcomputer
 *
 */
public class MaskRectangle implements ICollisionMask {

	private Rectangle rect;
	private Pos localPos;

	public MaskRectangle(int width, int height) {
		this(0, 0, width, height);
	}

	public MaskRectangle(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height));
	}

	/**
	 * Constructs a rectangle collision mask. Co-ordinates are local
	 */
	public MaskRectangle(Rectangle rect) {
		this.rect = rect;
		this.localPos = new Pos(rect.x, rect.y);
	}

	@Override
	public Shape getGlobalShape() {
		return rect;
	}

	@Override
	public void setGlobalPosition(Pos pos) {
		pos = Pos.copyOf(pos);
		pos.add(localPos);
		rect.x = (int) pos.getX();
		rect.y = (int) pos.getY();
	}

	@Override
	public void setLocalPosition(Pos pos) {
		localPos = Pos.copyOf(pos);
	}

	@Override
	public Pos getLocalPosition() {
		return localPos;
	}

	@Override
	public Pos getGlobalPosition() {
		return new Pos(rect.x, rect.y);
	}

}
