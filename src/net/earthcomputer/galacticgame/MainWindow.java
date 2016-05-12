package net.earthcomputer.galacticgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.earthcomputer.galacticgame.Level.LevelObject;
import net.earthcomputer.galacticgame.gui.Gui;
import net.earthcomputer.galacticgame.gui.GuiCompleteGame;
import net.earthcomputer.galacticgame.gui.GuiPauseMenu;
import net.earthcomputer.galacticgame.object.GameObject;
import net.earthcomputer.galacticgame.object.ObjectTypes;
import net.earthcomputer.galacticgame.util.AlwaysTruePredicate;
import net.earthcomputer.galacticgame.util.GameObjectCreator;
import net.earthcomputer.galacticgame.util.Images;
import net.earthcomputer.galacticgame.util.InstanceOfPredicate;
import net.earthcomputer.galacticgame.util.Keyboard;
import net.earthcomputer.galacticgame.util.Predicate;
import net.earthcomputer.galacticgame.util.Profile;
import net.earthcomputer.galacticgame.util.Profiles;
import net.earthcomputer.galacticgame.util.SoundManager;

public class MainWindow
{
	
	private static final Dimension PREFERRED_SIZE = new Dimension(640, 480);
	
	private static final BufferedImage PAUSE_BUTTON = Images.loadImage("gui/pause");
	
	private final JFrame theFrame;
	private CustomContentPane contentPane;
	
	private List<GameObject> objects = Collections.synchronizedList(new ArrayList<GameObject>());
	private List<IUpdateListener> updateListeners = Collections.synchronizedList(new ArrayList<IUpdateListener>());
	private Queue<Runnable> runLater = new ConcurrentLinkedQueue<Runnable>();
	
	private boolean paused = false;
	
	private Level currentLevel;
	private int currentLevelIndex;
	private Profile currentProfile;
	private boolean[] starsObtained = new boolean[3];
	private Gui openGui;
	
	private Random rand = new Random();
	private int musicCooldown = rand.nextInt(350) + 450;
	
	public MainWindow()
	{
		theFrame = new JFrame(
			GalacticGame.randomGenTitle(GalacticGame.GAME_VERSION.hashCode() + 31 * GalacticGame.GAME_NAME.hashCode())
				+ " (" + GalacticGame.GAME_NAME + " " + GalacticGame.GAME_VERSION + ")");
				
		theFrame.setContentPane(contentPane = new CustomContentPane());
		contentPane.setPreferredSize(PREFERRED_SIZE);
		theFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				GalacticGame.getInstance().shutdown();
			}
		});
		contentPane.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(openGui == null)
				{
					if(e.getButton() == MouseEvent.BUTTON1)
					{
						if(e.getX() >= 2 && e.getY() >= 2 && e.getX() < 2 + PAUSE_BUTTON.getWidth()
							&& e.getY() < 2 + PAUSE_BUTTON.getHeight())
						{
							openGui(new GuiPauseMenu());
						}
					}
				}
				else
				{
					openGui.mousePressed(e.getX(), e.getY(), e.getButton());
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				if(openGui != null) openGui.mouseReleased(e.getX(), e.getY(), e.getButton());
			}
			
		});
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if(openGui != null) openGui.mouseScrolled((float) e.getPreciseWheelRotation());
			}
			
		});
		contentPane.addKeyListener(Keyboard.instance());
		contentPane.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e)
			{
			}
			
			@Override
			public void focusLost(FocusEvent e)
			{
				Keyboard.clearKeys();
			}
		});
		contentPane.requestFocusInWindow();
		theFrame.setResizable(false);
		theFrame.pack();
		theFrame.setLocationRelativeTo(null);
		contentPane.requestFocus();
		theFrame.setVisible(true);
	}
	
	public void disposeWindow()
	{
		theFrame.dispose();
	}
	
	public GameObject addObject(double x, double y, int id)
	{
		return addObject(x, y, ObjectTypes.getCreatorById(id));
	}
	
	public <T extends GameObject> T addObject(double x, double y, GameObjectCreator<T> creator)
	{
		final T instance = creator.create(x, y);
		if(instance != null)
		{
			runLater(new Runnable() {
				@Override
				public void run()
				{
					objects.add(instance);
					instance.onAdded();
				}
			});
			if(instance instanceof IUpdateListener) addUpdateListener((IUpdateListener) instance);
		}
		return instance;
	}
	
	public void removeObject(final GameObject object)
	{
		runLater(new Runnable() {
			@Override
			public void run()
			{
				objects.remove(object);
				object.onRemoved();
			}
		});
		if(object instanceof IUpdateListener) removeUpdateListener((IUpdateListener) object);
	}
	
	public void addUpdateListener(final IUpdateListener updateListener)
	{
		runLater(new Runnable() {
			@Override
			public void run()
			{
				updateListeners.add(updateListener);
			}
		});
	}
	
	public void removeUpdateListener(final IUpdateListener updateListener)
	{
		runLater(new Runnable() {
			@Override
			public void run()
			{
				updateListeners.remove(updateListener);
			}
		});
	}
	
	public boolean loadLevel(int id)
	{
		Level level;
		try
		{
			level = Levels.loadLevel(id);
		}
		catch (Exception e)
		{
			return false;
		}
		
		Arrays.fill(starsObtained, false);
		currentLevelIndex = id;
		loadLevel(level);
		return true;
	}
	
	private void loadLevel(Level level)
	{
		runLater(new Runnable() {
			@Override
			public void run()
			{
				synchronized(objects)
				{
					objects.clear();
				}
				synchronized(updateListeners)
				{
					updateListeners.clear();
				}
			}
		});
		
		for(LevelObject object : level.objects)
		{
			addObject(object.x, object.y, object.id);
		}
		
		this.currentLevel = level;
	}
	
	public void restartLevel(boolean fail)
	{
		if(fail)
		{
			SoundManager.playSound("fail");
		}
		loadLevel(currentLevel);
	}
	
	public void completeLevel()
	{
		if(currentLevelIndex == currentProfile.getCurrentLevel())
		{
			currentProfile.completeLevel();
		}
		
		for(int i = 0; i < 3; i++)
		{
			if(starsObtained[i] && !currentProfile.isStarObtained(currentLevelIndex, i))
			{
				currentProfile.obtainStar(currentLevelIndex, i);
			}
		}
		
		try
		{
			Profiles.saveProfiles();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred while saving to profiles file.",
				GalacticGame.GAME_NAME, JOptionPane.ERROR_MESSAGE);
		}
		
		if(currentLevelIndex == Levels.getLevelCount() - 1)
		{
			completeGame();
		}
		else
		{
			loadLevel(currentLevelIndex + 1);
		}
	}
	
	public void completeGame()
	{
		openGui(new GuiCompleteGame());
	}
	
	public void completeStar(int index)
	{
		starsObtained[index] = true;
	}
	
	public Profile getProfile()
	{
		return currentProfile;
	}
	
	public void setProfile(Profile profile)
	{
		this.currentProfile = profile;
	}
	
	public int getCurrentLevelIndex()
	{
		return currentLevelIndex;
	}
	
	public void redraw()
	{
		theFrame.repaint();
	}
	
	public void updateTick()
	{
		musicCooldown--;
		if(musicCooldown == 0)
		{
			SoundManager.playSound("music", new LineListener() {
				
				@Override
				public void update(LineEvent e)
				{
					if(e.getType() != LineEvent.Type.STOP){ return; }
					musicCooldown = rand.nextInt(350) + 450;
				}
				
			});
		}
		
		Keyboard.updateTick();
		
		if(!paused)
		{
			synchronized(updateListeners)
			{
				for(IUpdateListener updateListener : updateListeners)
				{
					updateListener.update();
				}
			}
		}
		
		if(openGui != null) openGui.updateTick();
		
		redraw();
		
		synchronized(objects)
		{
			Collections.sort(objects, new Comparator<GameObject>() {
				@Override
				public int compare(GameObject first, GameObject second)
				{
					return Integer.compare(second.getDepth(), first.getDepth());
				}
			});
		}
		
		synchronized(updateListeners)
		{
			Collections.sort(updateListeners, new Comparator<IUpdateListener>() {
				@Override
				public int compare(IUpdateListener first, IUpdateListener second)
				{
					if(first instanceof GameObject)
					{
						if(second instanceof GameObject)
						{
							return Integer.compare(((GameObject) second).getDepth(), ((GameObject) first).getDepth());
						}
						else
						{
							return -1;
						}
					}
					else if(second instanceof GameObject)
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
			});
		}
		
		synchronized(runLater)
		{
			while(!runLater.isEmpty())
			{
				runLater.poll().run();
			}
		}
	}
	
	public void runLater(Runnable task)
	{
		synchronized(runLater)
		{
			runLater.offer(task);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GameObject> List<T> listObjects(Class<T> clazz)
	{
		return (List<T>) listObjects(new InstanceOfPredicate<GameObject>(clazz));
	}
	
	public List<GameObject> listObjects(Predicate<GameObject> predicate)
	{
		List<GameObject> objectsFound = new ArrayList<GameObject>();
		synchronized(objects)
		{
			for(GameObject object : objects)
			{
				if(predicate.apply(object))
				{
					objectsFound.add(object);
				}
			}
		}
		return objectsFound;
	}
	
	public List<GameObject> getObjectsThatCollideWith(GameObject object)
	{
		return getObjectsThatCollideWith(object, new AlwaysTruePredicate<GameObject>());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GameObject> List<T> getObjectsThatCollideWith(GameObject object, Class<T> type)
	{
		return (List<T>) getObjectsThatCollideWith(object, new InstanceOfPredicate<GameObject>(type));
	}
	
	public List<GameObject> getObjectsThatCollideWith(final GameObject object, final Predicate<GameObject> filter)
	{
		return listObjects(new Predicate<GameObject>() {
			@Override
			public boolean apply(GameObject input)
			{
				return filter.apply(input) && object.isCollidedWith(input);
			}
		});
	}
	
	public List<GameObject> getObjectsThatCollideWith(Shape shape)
	{
		return getObjectsThatCollideWith(shape, new AlwaysTruePredicate<GameObject>());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GameObject> List<T> getObjectsThatCollideWith(Shape shape, Class<T> type)
	{
		return (List<T>) getObjectsThatCollideWith(shape, new InstanceOfPredicate<GameObject>(type));
	}
	
	public List<GameObject> getObjectsThatCollideWith(final Shape shape, final Predicate<GameObject> filter)
	{
		return listObjects(new Predicate<GameObject>() {
			@Override
			public boolean apply(GameObject input)
			{
				return filter.apply(input) && input.isCollidedWith(shape);
			}
		});
	}
	
	public boolean isObjectCollidedWith(GameObject object, Class<? extends GameObject> type)
	{
		return !getObjectsThatCollideWith(object, type).isEmpty();
	}
	
	public boolean isObjectCollidedWith(GameObject object, Predicate<GameObject> filter)
	{
		return !getObjectsThatCollideWith(object, filter).isEmpty();
	}
	
	public boolean isShapeCollidedWith(Shape shape, Class<? extends GameObject> type)
	{
		return !getObjectsThatCollideWith(shape, type).isEmpty();
	}
	
	public boolean isShapeCollidedWith(Shape shape, Predicate<GameObject> filter)
	{
		return !getObjectsThatCollideWith(shape, filter).isEmpty();
	}
	
	public int getWidth()
	{
		return currentLevel.width;
	}
	
	public int getHeight()
	{
		return currentLevel.height;
	}
	
	public void openGui(final Gui gui)
	{
		runLater(new Runnable() {
			@Override
			public void run()
			{
				openGuiDangerously(gui);
			}
		});
	}
	
	private void openGuiDangerously(Gui gui)
	{
		if(openGui != null) openGui.onClosed();
		
		this.openGui = gui;
		
		if(gui == null)
		{
			this.paused = false;
		}
		else
		{
			this.paused = gui.pausesGame();
			gui.validate(contentPane.getWidth(), contentPane.getHeight());
		}
	}
	
	public void closeGui()
	{
		openGui(null);
	}
	
	public Point getMouseLocation()
	{
		Point mouseLocation = new Point(MouseInfo.getPointerInfo().getLocation());
		Point compLocation = contentPane.getLocationOnScreen();
		mouseLocation.x -= compLocation.x;
		mouseLocation.y -= compLocation.y;
		return mouseLocation;
	}
	
	private class CustomContentPane extends JPanel
	{
		
		private static final long serialVersionUID = -5888940429070142635L;
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			if(openGui == null || openGui.shouldDrawLevelBackground())
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				
				synchronized(objects)
				{
					for(GameObject object : objects)
					{
						object.draw(g);
					}
				}
			}
			
			if(openGui == null)
			{
				g.drawImage(PAUSE_BUTTON, 2, 2, null);
			}
			else
			{
				openGui.drawScreen(g);
			}
		}
		
	}
	
}
