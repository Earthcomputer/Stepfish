package net.earthcomputer.stepfish.object;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import net.earthcomputer.stepfish.geom.Velocity;
import net.earthcomputer.stepfish.geom.collision.MaskRectangle;
import net.earthcomputer.stepfish.geom.collision.MoveToContactHelper;
import net.earthcomputer.stepfish.gui.GuiPauseMenu;
import net.earthcomputer.stepfish.util.Images;
import net.earthcomputer.stepfish.util.Keyboard;
import net.earthcomputer.stepfish.util.Predicate;
import net.earthcomputer.stepfish.util.SoundManager;

public class PlayerObject extends PhysicsObject
{
	
	private static final int TICKS_PER_FRAME = 5;
	private static final EnumMap<EnumElement, EnumMap<EnumPlayerState, EnumMap<EnumFacing, BufferedImage[]>>> textures = new EnumMap<EnumElement, EnumMap<EnumPlayerState, EnumMap<EnumFacing, BufferedImage[]>>>(
		EnumElement.class);
		
	static
	{
		for(EnumElement element : EnumElement.values())
		{
			textures.put(element,
				new EnumMap<EnumPlayerState, EnumMap<EnumFacing, BufferedImage[]>>(EnumPlayerState.class));
			for(EnumPlayerState state : EnumPlayerState.values())
			{
				textures.get(element).put(state, new EnumMap<EnumFacing, BufferedImage[]>(EnumFacing.class));
				BufferedImage[] frames;
				// Not animated
				if(state != EnumPlayerState.WALK && element != EnumElement.FIRE)
				{
					frames = new BufferedImage[] { Images
						.loadImage(String.format("object/player_%s_%s", element.getName(), state.getName())) };
				}
				// Animated
				else
				{
					frames = new BufferedImage[2];
					for(int i = 0; i < frames.length; i++)
					{
						frames[i] = Images
							.loadImage(String.format("object/player_%s_%s_%d", element.getName(), state.getName(), i));
					}
				}
				
				for(EnumFacing facing : EnumFacing.values())
				{
					BufferedImage[] actualFrames = new BufferedImage[frames.length];
					for(int i = 0; i < frames.length; i++)
					{
						AffineTransform scaler = AffineTransform.getScaleInstance(facing.getScale(), 1);
						if(facing.getScale() == -1)
						{
							scaler.translate(-frames[i].getWidth(), 0);
						}
						AffineTransformOp imageScaler = new AffineTransformOp(scaler,
							AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
						actualFrames[i] = imageScaler.filter(frames[i], null);
					}
					textures.get(element).get(state).put(facing, actualFrames);
				}
			}
		}
	}
	
	private int ticksExisted = 0;
	
	private EnumPlayerState state;
	private EnumElement element;
	private EnumFacing facing = EnumFacing.RIGHT;
	
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
		ticksExisted++;
		
		// Physics
		super.update();
		
		// Terminal velocity
		if(getYVelocity() > 10) setYVelocity(10);
		
		// Move to contact with solids
		if(window.isObjectCollidedWith(this, wallCollisionPredicate))
		{
			moveToContactHelper.moveToContact();
		}
		
		// land if there is a solid below, fall if there isn't
		if(window.isShapeCollidedWith(new Line2D.Double(getX() + 1, getY() + 16, getX() + 15, getY() + 16),
			wallCollisionPredicate))
		{
			if(!state.needsSupport())
			{
				if(getY() > getPreviousPos().getY())
				{
					SoundManager.playSound("jump_land");
				}
				if(getXVelocity() == 0)
				{
					changeState(EnumPlayerState.STAND);
				}
				else
				{
					changeState(EnumPlayerState.WALK);
				}
			}
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
		
		if(Keyboard.isKeyDown("moveLeft") ^ Keyboard.isKeyDown("moveRight"))
		{
			if(state == EnumPlayerState.STAND)
			{
				changeState(EnumPlayerState.WALK);
			}
			
			if(Keyboard.isKeyDown("moveLeft"))
			{
				facing = EnumFacing.LEFT;
				setXVelocity(Math.max(getXVelocity() - 2, -7));
			}
			else
			{
				facing = EnumFacing.RIGHT;
				setXVelocity(Math.min(getXVelocity() + 2, 7));
			}
		}
		else
		{
			if(state == EnumPlayerState.WALK)
			{
				changeState(EnumPlayerState.STAND);
			}
			if(getXVelocity() < 0)
			{
				setXVelocity(Math.min(getXVelocity() + 5, 0));
			}
			else
			{
				setXVelocity(Math.max(getXVelocity() - 5, 0));
			}
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
		if(getY() >= window.getHeight()) window.failLevel(this);
		
		// Footsteps
		if(state == EnumPlayerState.WALK && ticksExisted % TICKS_PER_FRAME == 0
			&& this.getPreviousPos().getX() != getX())
		{
			SoundManager.playSound("footstep");
		}
	}
	
	@Override
	public void draw(Graphics g)
	{
		int x = (int) getX();
		int y = (int) getY();
		BufferedImage[] frames = textures.get(element).get(state).get(facing);
		g.drawImage(frames[(ticksExisted / TICKS_PER_FRAME) % frames.length], x, y, null);
	}
	
}
