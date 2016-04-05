package net.earthcomputer.githubgame.geom.collision;

import java.awt.Shape;

import net.earthcomputer.githubgame.geom.Pos;

/**
 * Represents a collision mask.
 * 
 * A global shape is the shape of the collision mask with the position relative
 * to the top left of the game area. The local shape is the shape of the
 * collision mask relative to the co-ordinates of the game object. See
 * {@link MaskRectangle} for an example of implementation.
 * 
 * @author Earthcomputer
 *
 */
public interface ICollisionMask {

	/**
	 * Returns the global mask of this mask
	 */
	Shape getGlobalShape();

	/**
	 * Sets the local position of this mask
	 */
	void setLocalPosition(Pos pos);

	/**
	 * Sets the global position of this mask
	 */
	void setGlobalPosition(Pos pos);

	/**
	 * Gets the local position of this mask
	 */
	Pos getLocalPosition();

	/**
	 * Gets the global position of this mask
	 */
	Pos getGlobalPosition();

}
