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
		Pos prevPos = theObject.getPreviousPos();
		Pos pos = theObject.getPos();
		double movingX = pos.getX() - prevPos.getX();
		double movingY = pos.getY() - prevPos.getY();
		double absDX = Math.abs(movingX);
		double absDY = Math.abs(movingY);
		theObject.setPos(prevPos);
		double amtToMove;
		if(absDX < absDY)
		{
			amtToMove = absDY;
			movingX /= absDY;
			movingY = Math.signum(movingY);
		}
		else
		{
			amtToMove = absDX;
			movingY /= absDX;
			movingX = Math.signum(movingX);
		}
		
		@SuppressWarnings("unchecked")
		CollisionMask<T> mask = (CollisionMask<T>) theObject.getCollisionMask();
		T rect = mask.getGlobalShape();
		double x = prevPos.getX();
		double y = prevPos.getY();
		
		for(int i = 0; i < amtToMove; i++)
		{
			// Try moving diagonally first
			rect = mask.translate(rect, movingX, movingY);
			x += movingX;
			y += movingY;
			if(window.isShapeCollidedWith(rect, collisionPredicate))
			{
				// Try moving vertically by negating x
				rect = mask.translate(rect, -movingX, 0);
				x -= movingX;
				if(window.isShapeCollidedWith(rect, collisionPredicate))
				{
					// Try moving horizontally by un-negating x and negating y
					rect = mask.translate(rect, movingX, -movingY);
					x += movingX;
					y -= movingY;
					if(window.isShapeCollidedWith(rect, collisionPredicate))
					{
						// Can't move anywhere, re-negate x
						rect = mask.translate(rect, -movingX, 0);
						x -= movingX;
						
						// Make both coords whole
						x = movingX > 0 ? Math.ceil(x) : Math.floor(x);
						y = movingY > 0 ? Math.ceil(y) : Math.floor(y);
						// Stop moving
						theObject.setSpeed(0);
					}
					else
					{
						// Successfully moved horizontally, make y-coord whole
						y = movingY > 0 ? Math.ceil(y) : Math.floor(y);
						// Stop vertical speed
						theObject.setYVelocity(0);
					}
				}
				else
				{
					// Successfully moved vertically, make x-coord whole
					x = movingX > 0 ? Math.ceil(x) : Math.floor(x);
					// Stop horizontal speed
					theObject.setXVelocity(0);
				}
			}
		}
		theObject.setPos(new Pos(x, y));
	}
	
}
