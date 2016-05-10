package net.earthcomputer.galacticgame.geom.collision;

import java.awt.Shape;

import net.earthcomputer.galacticgame.GalacticGame;
import net.earthcomputer.galacticgame.MainWindow;
import net.earthcomputer.galacticgame.geom.Pos;
import net.earthcomputer.galacticgame.object.GameObject;
import net.earthcomputer.galacticgame.object.PhysicsObject;
import net.earthcomputer.galacticgame.util.InstanceOfPredicate;
import net.earthcomputer.galacticgame.util.Predicate;

public class MoveToContactHelper
{
	
	private final MainWindow window;
	private final PhysicsObject theObject;
	private final Predicate<GameObject> collisionPredicate;
	
	public MoveToContactHelper(PhysicsObject object, Class<? extends GameObject> collision)
	{
		this(object, new InstanceOfPredicate<GameObject>(collision));
	}
	
	public MoveToContactHelper(PhysicsObject object, Predicate<GameObject> collisionPredicate)
	{
		window = GalacticGame.getInstance().getWindow();
		theObject = object;
		this.collisionPredicate = collisionPredicate;
	}
	
	public <T extends Shape> void moveToContact()
	{
		/** The position we're moving to contact from */
		Pos prevPos = theObject.getPreviousPos();
		/** The position the object first collided with a wall */
		Pos pos = theObject.getPos();
		/** How the object has moved since the previous tick */
		double dx = pos.getX() - prevPos.getX();
		double dy = pos.getY() - prevPos.getY();
		/** The absolute distance in each direction the object has moved */
		double absDX = Math.abs(dx);
		double absDY = Math.abs(dy);
		/** The amount to move the object in each step in each direction */
		double movingX, movingY;
		/** The maximum amount of steps to take, acts as a safety net in case no collision is found */
		double amtToMove;
		
		if(absDX < absDY)
		{
			// We're moving mostly vertically, movingX is the fractional variable
			amtToMove = absDY;
			movingX = dx / absDY;
			movingY = Math.signum(dy);
		}
		else
		{
			// We're moving mostly horizontally, movingY is the fractional variable
			amtToMove = absDX;
			movingY = dy / absDX;
			movingX = Math.signum(dx);
		}
		
		@SuppressWarnings("unchecked")
		/** The object's collision mask, used to make transformations etc. */
		CollisionMask<T> mask = (CollisionMask<T>) theObject.getCollisionMask();
		/** A base collision shape from before any steps */
		T shapeStart = mask.translate(mask.getGlobalShape(), -dx, -dy);
		/** A shape to test collision with */
		T shapeTesting = null;
		/** The starting position */
		double xStart = prevPos.getX(), yStart = prevPos.getY();
		/** The number of steps taken in each direction */
		int stepsX = 0, stepsY = 0;
		/** Whether stopped moving in a particular direction because of a collision */
		boolean hitHorizontally = false, hitVertically = false;
		
		for(int step = 0; step < amtToMove; step++)
		{
			// Try move diagonally
			if(!hitHorizontally && !hitVertically)
			{
				shapeTesting = mask.translate(mask.copy(shapeStart), (stepsX + 1) * movingX, (stepsY + 1) * movingY);
			}
			if(!hitHorizontally && !hitVertically && !window.isShapeCollidedWith(shapeTesting, collisionPredicate))
			{
				// Successfully moved diagonally
				stepsX++;
				stepsY++;
			}
			else
			{
				// Try move vertically
				if(!hitVertically)
				{
					shapeTesting = mask.translate(mask.copy(shapeStart), stepsX * movingX, (stepsY + 1) * movingY);
				}
				if(!hitVertically && !window.isShapeCollidedWith(shapeTesting, collisionPredicate))
				{
					// Successfully moved vertically
					stepsY++;
					hitHorizontally = true;
				}
				else
				{
					// Try move horizontally
					if(!hitHorizontally)
					{
						shapeTesting = mask.translate(mask.copy(shapeStart), (stepsX + 1) * movingX, stepsY * movingY);
					}
					if(!hitHorizontally && !window.isShapeCollidedWith(shapeTesting, collisionPredicate))
					{
						// Successfully moved horizontally
						stepsX++;
						hitVertically = true;
					}
					else
					{
						// Couldn't move anywhere
						hitHorizontally = true;
						hitVertically = true;
						break;
					}
				}
			}
		}
		
		// Correction
		if(!hitHorizontally && !hitVertically)
		{
			// If we haven't hit anything, then there's no point doing anything
			return;
		}
		
		/** Destination positions */
		double destX = xStart + stepsX * movingX;
		double destY = yStart + stepsY * movingY;
		
		if(hitHorizontally)
		{
			// Align to grid horizontally
			destX = movingX > 0 ? Math.ceil(destX) : Math.floor(destX);
			// Update the object's x-position accordingly
			theObject.setX(destX);
			// Stop when hit
			theObject.setXVelocity(0);
		}
		
		if(hitVertically)
		{
			// Align to grid vertically
			destY = movingY > 0 ? Math.ceil(destY) : Math.floor(destY);
			// Update the object's y-position accordingly
			theObject.setY(destY);
			// Stop when hit
			theObject.setYVelocity(0);
		}
	}
	
}
