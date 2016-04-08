package net.earthcomputer.githubgame.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import net.earthcomputer.githubgame.geom.Pos;
import net.earthcomputer.githubgame.geom.Velocity;
import net.earthcomputer.githubgame.geom.collision.MaskRectangle;
import net.earthcomputer.githubgame.util.Predicate;

public class PlayerObject extends PhysicsObject
{
	
	private EnumPlayerState state;
	private EnumElement element;
	private Velocity downwardsGravity;
	private int downwardsGravityId;
	
	private Predicate<GameObject> wallCollisionPredicate = new Predicate<GameObject>() {
		
		@Override
		public boolean apply(GameObject input)
		{
			if(!(input instanceof WallObject)) return false;
			EnumElement wallElement = ((WallObject) input).getElement();
			return wallElement == null || wallElement == element;
		}
		
	};
	
	public PlayerObject(double x, double y)
	{
		super(x, y);
		downwardsGravity = Velocity.createFromSpeedAndDirection(0, 90);
		downwardsGravityId = addGravity(downwardsGravity);
		changeState(EnumPlayerState.AIR);
		element = EnumElement.EARTH;
		setCollisionMask(new MaskRectangle(16, 16));
		setDepth(-1000);
	}
	
	public void changeState(EnumPlayerState newState)
	{
		this.state = newState;
		if(newState.hasGravity())
		{
			downwardsGravity.setSpeed(1);
		}
		else
		{
			downwardsGravity.setSpeed(0);
			setYVelocity(0);
		}
		updateGravity(downwardsGravityId, downwardsGravity);
	}
	
	public EnumElement getElement()
	{
		return element;
	}
	
	public void setElement(EnumElement element)
	{
		this.element = element;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(getYVelocity() > 10) setYVelocity(10);
		
		if(githubGame.isKeyDown("moveLeft"))
		{
			if(getXVelocity() > -7) setXVelocity(Math.max(getXVelocity() - 2, -7));
		}
		else
		{
			if(getXVelocity() < 0) setXVelocity(Math.min(getXVelocity() + 5, 0));
		}
		
		if(githubGame.isKeyDown("moveRight"))
		{
			if(getXVelocity() < 7) setXVelocity(Math.min(getXVelocity() + 2, 7));
		}
		else
		{
			if(getXVelocity() > 0) setXVelocity(Math.max(getXVelocity() - 5, 0));
		}
		
		if(githubGame.isKeyDown("jump") && state.needsSupport())
		{
			changeState(EnumPlayerState.AIR);
			accelerateY(-8);
			move(0, -1);
		}
		
		if(window.isObjectCollidedWith(this, wallCollisionPredicate))
		{
			Pos prevPos = getPreviousPos();
			Pos pos = getPos();
			double movingX = pos.getX() - prevPos.getX();
			double movingY = pos.getY() - prevPos.getY();
			double absDX = Math.abs(movingX);
			double absDY = Math.abs(movingY);
			setPos(prevPos);
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
			
			Rectangle2D.Double rect = new Rectangle2D.Double(prevPos.getX(), prevPos.getY(), 16, 16);
			
			for(int i = 0; i < amtToMove; i++)
			{
				// Try moving diagonally first
				rect.x += movingX;
				rect.y += movingY;
				if(window.isShapeCollidedWith(rect, wallCollisionPredicate))
				{
					// Try moving vertically by negating x
					rect.x -= movingX;
					if(window.isShapeCollidedWith(rect, wallCollisionPredicate))
					{
						// Try moving horizontally by un-negating x and negating y
						rect.x += movingX;
						rect.y -= movingY;
						if(window.isShapeCollidedWith(rect, wallCollisionPredicate))
						{
							// Can't move anywhere, re-negate x
							rect.x -= movingX;
							
							// Make both coords whole
							rect.x = movingX > 0 ? Math.ceil(rect.x) : Math.floor(rect.x);
							rect.y = movingY > 0 ? Math.ceil(rect.y) : Math.floor(rect.y);
							// Stop moving
							setSpeed(0);
						}
						else
						{
							// Successfully moved horizontally, make y-coord whole
							rect.y = movingY > 0 ? Math.ceil(rect.y) : Math.floor(rect.y);
							// Stop vertical speed
							setYVelocity(0);
						}
					}
					else
					{
						// Successfully moved vertically, make x-coord whole
						rect.x = movingX > 0 ? Math.ceil(rect.x) : Math.floor(rect.x);
						// Stop horizontal speed
						setXVelocity(0);
					}
				}
			}
			setPos(new Pos(rect.x, rect.y));
		}
		
		if(window.isShapeCollidedWith(new Line2D.Double(getX() + 1, getY() + 16, getX() + 15, getY() + 16),
			wallCollisionPredicate))
		{
			if(!state.needsSupport()) changeState(EnumPlayerState.STAND);
		}
		else
		{
			if(state.needsSupport()) changeState(EnumPlayerState.AIR);
		}
		
		if(getY() >= window.getHeight()) window.restartLevel();
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		g.setColor(element.getColor());
		g.fillRect(x, y, 16, 16);
		g.setColor(Color.WHITE);
		g.fillRect(x + 8, y + 3, 2, 2);
		g.fillRect(x + 12, y + 3, 2, 2);
		g.fillRect(x + 8, y + 10, 8, 2);
	}
	
}
