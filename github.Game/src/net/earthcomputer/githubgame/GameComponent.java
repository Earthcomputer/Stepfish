package net.earthcomputer.githubgame;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import net.earthcomputer.githubgame.geom.Pos;
import net.earthcomputer.githubgame.geom.collision.ICollisionMask;

/**
 * Represents all objects in the game
 * 
 * @author Earthcomputer
 *
 */
public abstract class GameComponent extends JComponent {

	private static final long serialVersionUID = 4039393389062942365L;

	private Pos pos;
	private ICollisionMask collisionMask;
	private boolean canDoFastCollision;
	private boolean fastCollision = true;

	/**
	 * Constructs a game component with the given co-ordinates
	 */
	public GameComponent(float x, float y) {
		this.pos = new Pos(x, y);
		setLocation((int) x, (int) y);
		setSize(1, 1);
	}

	/**
	 * Gets the unmodifiable position of this game component. DO NOT USE
	 * {@link java.awt.Component#getLocation() getLocation()}
	 */
	public Pos getPos() {
		return Pos.unmodifiablePos(pos);
	}

	/**
	 * Gets the x-position of this component. DO NOT USE
	 * {@link JComponent#getX() getX()}
	 */
	public float getXPos() {
		return pos.getX();
	}

	/**
	 * Gets the y-position of this component. DO NOT USE
	 * {@link JComponent#getY() getY()}
	 */
	public float getYPos() {
		return pos.getY();
	}

	/**
	 * Sets the position of this component. DO NOT USE
	 * {@link java.awt.Component#setLocation(java.awt.Point) setLocation(Point)}
	 */
	public void setPos(Pos pos) {
		this.pos = Pos.copyOf(pos);
		setLocation((int) pos.getX(), (int) pos.getY());
		if (collisionMask != null)
			collisionMask.setGlobalPosition(pos);
	}

	/**
	 * Sets the x-position of this component. DO NOT USE
	 * {@link java.awt.Component#setLocation(java.awt.Point) setLocation(Point)}
	 */
	public void setXPos(float xpos) {
		pos.setX(xpos);
		setLocation((int) xpos, getY());
		if (collisionMask != null)
			collisionMask.setGlobalPosition(pos);
	}

	/**
	 * Sets the y-position of this component. DO NOT USE
	 * {@link java.awt.Component#setLocation(java.awt.Point) setLocation(Point)}
	 */
	public void setYPos(float ypos) {
		pos.setY(ypos);
		setLocation(getX(), (int) ypos);
		if (collisionMask != null)
			collisionMask.setGlobalPosition(pos);
	}

	/**
	 * Returns whether a collision mask has been set for this object
	 */
	public boolean hasCollisionMask() {
		return collisionMask != null;
	}

	/**
	 * Returns the collision mask of this object
	 */
	public ICollisionMask getCollisionMask() {
		return collisionMask;
	}

	/**
	 * Sets the collision mask of this object
	 */
	public void setCollisionMask(ICollisionMask mask) {
		canDoFastCollision = mask.getGlobalShape() instanceof Rectangle2D;
		collisionMask = mask;
		Rectangle bounds = mask.getGlobalShape().getBounds();
		setBounds(bounds);
		setSize(bounds.width, bounds.height);
		mask.setGlobalPosition(pos);
	}

	/**
	 * Sets whether this object is allowed to do fast collision.
	 * 
	 * Fast collision is when the method
	 * {@link java.awt.Shape#intersects(Rectangle2D)
	 * Shape.intersects(Rectangle2D)} is used to calculate collision when one of
	 * the collision masks uses a rectangle. This is faster, but can be less
	 * accurate
	 */
	protected void setDoFastCollision(boolean doFastCollision) {
		fastCollision = doFastCollision;
	}

	/**
	 * Returns whether this object is allowed to do fast collision. For a
	 * definition of fast collision, see {@link #setDoFastCollision(boolean)}
	 */
	protected boolean doesFastCollision() {
		return fastCollision;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	/**
	 * Called every frame to draw this component
	 */
	public void draw(Graphics g) {
	}

	/**
	 * Returns whether the given point is inside the object's collision mask
	 */
	public boolean isCollidedWith(Point2D other) {
		if (collisionMask == null || other == null)
			return false;
		return collisionMask.getGlobalShape().contains(other);
	}

	/**
	 * Returns whether the given shape intersects the object's collision mask
	 */
	public boolean isCollidedWith(Shape other) {
		if (collisionMask == null || other == null)
			return false;
		if (canDoFastCollision && fastCollision) {
			return other.intersects((Rectangle2D) collisionMask.getGlobalShape());
		} else if (other instanceof Rectangle2D && fastCollision) {
			return collisionMask.getGlobalShape().intersects((Rectangle2D) other);
		} else {
			Area area = new Area(collisionMask.getGlobalShape());
			area.intersect(new Area(other));
			return !area.isEmpty();
		}
	}

	/**
	 * Returns whether this object's collision mask intersects the other
	 * object's collision mask
	 */
	public boolean isCollidedWith(GameComponent other) {
		if (collisionMask == null || other == null || other.collisionMask == null)
			return false;
		boolean doFastCollision = fastCollision && other.fastCollision;
		if (canDoFastCollision && doFastCollision) {
			return other.collisionMask.getGlobalShape().intersects((Rectangle2D) collisionMask.getGlobalShape());
		} else if (other.canDoFastCollision && doFastCollision) {
			return collisionMask.getGlobalShape().intersects((Rectangle2D) other.collisionMask.getGlobalShape());
		} else {
			Area area = new Area(collisionMask.getGlobalShape());
			area.intersect(new Area(other.collisionMask.getGlobalShape()));
			return !area.isEmpty();
		}
	}

}
