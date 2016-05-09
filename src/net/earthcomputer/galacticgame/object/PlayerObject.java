package net.earthcomputer.galacticgame.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

import net.earthcomputer.galacticgame.geom.Velocity;
import net.earthcomputer.galacticgame.geom.collision.MaskRectangle;
import net.earthcomputer.galacticgame.geom.collision.MoveToContactHelper;
import net.earthcomputer.galacticgame.gui.GuiPauseMenu;
import net.earthcomputer.galacticgame.util.Keyboard;
import net.earthcomputer.galacticgame.util.Predicate;

public class PlayerObject extends PhysicsObject
{
	
	private EnumPlayerState state;
	private EnumElement element;
	
	private Predicate<GameObject> wallCollisionPredicate = new Predicate<GameObject>() {
		
		@Override
		public boolean apply(GameObject input)
		{
			if(!(input instanceof WallObject)) return false;
			EnumElement wallElement = ((WallObject) input).getElement();
			return wallElement == null || wallElement == element;
		}
		
	};
	private MoveToContactHelper moveToContactHelper = new MoveToContactHelper(this, wallCollisionPredicate);
	
	public PlayerObject(double x, double y)
	{
		super(x, y);
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
			setGravity(Velocity.createFromSpeedAndDirection(1, 90));
		}
		else
		{
			removeGravity();
			setYVelocity(0);
		}
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
		// Physics
		super.update();
		
		// Terminal velocity
		if(getYVelocity() > 10) setYVelocity(10);
		
		// Move to contact with solids
		if(window.isObjectCollidedWith(this, wallCollisionPredicate))
		{
			moveToContactHelper.moveToContact();
		}
		
		// STAND if there is a solid below
		if(window.isShapeCollidedWith(new Line2D.Double(getX() + 1, getY() + 16, getX() + 15, getY() + 16),
			wallCollisionPredicate))
		{
			if(!state.needsSupport()) changeState(EnumPlayerState.STAND);
		}
		else
		{
			if(state.needsSupport()) changeState(EnumPlayerState.AIR);
		}
		
		// Keyboard input
		if(Keyboard.isKeyPressed("jump") && state.needsSupport())
		{
			changeState(EnumPlayerState.AIR);
			accelerateY(-8);
		}
		
		if(Keyboard.isKeyDown("moveLeft"))
		{
			if(getXVelocity() > -7) setXVelocity(Math.max(getXVelocity() - 2, -7));
		}
		else
		{
			if(getXVelocity() < 0) setXVelocity(Math.min(getXVelocity() + 5, 0));
		}
		
		if(Keyboard.isKeyDown("moveRight"))
		{
			if(getXVelocity() < 7) setXVelocity(Math.min(getXVelocity() + 2, 7));
		}
		else
		{
			if(getXVelocity() > 0) setXVelocity(Math.max(getXVelocity() - 5, 0));
		}
		
		if(Keyboard.isKeyPressed("closeGui"))
		{
			window.openGui(new GuiPauseMenu());
		}
		
		// Mud
		if(window.isObjectCollidedWith(this, MudObject.class))
		{
			if(getSpeed() > 1)
			{
				setSpeed(1);
			}
		}
		
		// Restart level if fallen off
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
