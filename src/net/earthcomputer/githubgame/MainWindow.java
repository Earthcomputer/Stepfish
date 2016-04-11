package net.earthcomputer.githubgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.earthcomputer.githubgame.Level.LevelObject;
import net.earthcomputer.githubgame.object.GameObject;
import net.earthcomputer.githubgame.object.ObjectTypes;
import net.earthcomputer.githubgame.util.GameObjectCreator;
import net.earthcomputer.githubgame.util.InstanceOfPredicate;
import net.earthcomputer.githubgame.util.Predicate;

public class MainWindow
{
	
	private static final Dimension PREFERRED_SIZE = new Dimension(640, 480);
	
	private final JFrame theFrame;
	private CustomContentPane contentPane;
	
	private List<GameObject> objects = Collections.synchronizedList(new ArrayList<GameObject>());
	private Set<GameObject> objectsToAdd = Collections.synchronizedSet(new HashSet<GameObject>());
	private Set<GameObject> objectsToRemove = Collections.synchronizedSet(new HashSet<GameObject>());
	private List<IUpdateListener> updateListeners = Collections.synchronizedList(new ArrayList<IUpdateListener>());
	private Set<IUpdateListener> updateListenersToAdd = Collections.synchronizedSet(new HashSet<IUpdateListener>());
	private Set<IUpdateListener> updateListenersToRemove = Collections.synchronizedSet(new HashSet<IUpdateListener>());
	private Set<String> keysDown = new HashSet<String>();
	
	private Level currentLevel;
	
	public MainWindow()
	{
		theFrame = new JFrame(GithubGame.GAME_NAME + " " + GithubGame.GAME_VERSION);
		
		theFrame.setContentPane(contentPane = new CustomContentPane());
		contentPane.setPreferredSize(PREFERRED_SIZE);
		theFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				GithubGame.getInstance().shutdown();
			}
			
			@Override
			public void windowLostFocus(WindowEvent e)
			{
				keysDown.clear();
			}
		});
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
		T instance = creator.create(x, y);
		if(instance != null)
		{
			objectsToAdd.add(instance);
			if(instance instanceof IUpdateListener) addUpdateListener((IUpdateListener) instance);
		}
		return instance;
	}
	
	public void removeObject(GameObject object)
	{
		objectsToRemove.add(object);
		if(object instanceof IUpdateListener) removeUpdateListener((IUpdateListener) object);
	}
	
	public void addUpdateListener(IUpdateListener updateListener)
	{
		updateListenersToAdd.add(updateListener);
	}
	
	public void removeUpdateListener(IUpdateListener updateListener)
	{
		updateListenersToRemove.add(updateListener);
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
		
		loadLevel(level);
		return true;
	}
	
	public boolean loadLevel(String name)
	{
		Level level;
		try
		{
			level = Levels.loadLevel(name);
		}
		catch (Exception e)
		{
			return false;
		}
		
		loadLevel(level);
		return true;
	}
	
	public void loadLevel(Level level)
	{
		objectsToRemove.addAll(objects);
		updateListenersToRemove.addAll(updateListeners);
		
		for(LevelObject object : level.objects)
		{
			addObject(object.x, object.y, object.id);
		}
		
		this.currentLevel = level;
	}
	
	public void restartLevel()
	{
		loadLevel(currentLevel);
	}
	
	public void completeLevel()
	{
		JOptionPane.showMessageDialog(null, "Well Done! You completed the test level!", GithubGame.GAME_NAME,
			JOptionPane.INFORMATION_MESSAGE);
		restartLevel();
	}
	
	public void redraw()
	{
		theFrame.repaint();
	}
	
	public void updateTick()
	{
		synchronized(updateListeners)
		{
			for(IUpdateListener updateListener : updateListeners)
			{
				updateListener.update();
			}
		}
		
		redraw();
		
		synchronized(objectsToRemove)
		{
			for(GameObject object : objectsToRemove)
			{
				objects.remove(object);
			}
		}
		synchronized(objectsToAdd)
		{
			for(GameObject object : objectsToAdd)
			{
				objects.add(object);
			}
		}
		objectsToRemove.clear();
		objectsToAdd.clear();
		synchronized(updateListenersToRemove)
		{
			for(IUpdateListener updateListener : updateListenersToRemove)
			{
				updateListeners.remove(updateListener);
			}
		}
		synchronized(updateListenersToAdd)
		{
			for(IUpdateListener updateListener : updateListenersToAdd)
			{
				updateListeners.add(updateListener);
			}
		}
		updateListenersToRemove.clear();
		updateListenersToAdd.clear();
		
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
	
	public List<GameObject> getObjectsThatCollideWith(final Shape shape)
	{
		return listObjects(new Predicate<GameObject>() {
			
			@Override
			public boolean apply(GameObject input)
			{
				return input.isCollidedWith(shape);
			}
			
		});
	}
	
	public List<GameObject> getObjectsThatCollideWith(final GameObject object)
	{
		return listObjects(new Predicate<GameObject>() {
			
			@Override
			public boolean apply(GameObject input)
			{
				return input.isCollidedWith(object);
			}
			
		});
	}
	
	public boolean isObjectCollidedWith(GameObject object, final Class<? extends GameObject> clazz)
	{
		return isObjectCollidedWith(object, new InstanceOfPredicate<GameObject>(clazz));
	}
	
	public boolean isObjectCollidedWith(GameObject object, Predicate<GameObject> predicate)
	{
		List<GameObject> objects = getObjectsThatCollideWith(object);
		for(GameObject object1 : objects)
		{
			if(predicate.apply(object1)) return true;
		}
		return false;
	}
	
	public boolean isShapeCollidedWith(Shape shape, final Class<? extends GameObject> clazz)
	{
		return isShapeCollidedWith(shape, new InstanceOfPredicate<GameObject>(clazz));
	}
	
	public boolean isShapeCollidedWith(Shape shape, Predicate<GameObject> predicate)
	{
		List<GameObject> objects = getObjectsThatCollideWith(shape);
		for(GameObject object1 : objects)
		{
			if(predicate.apply(object1)) return true;
		}
		return false;
	}
	
	public double getTaxicabDistanceBetween(GameObject object1, GameObject object2)
	{
		return Math.abs(object1.getX() - object2.getX()) + Math.abs(object1.getY() - object2.getY());
	}
	
	public int getWidth()
	{
		return currentLevel.width;
	}
	
	public int getHeight()
	{
		return currentLevel.height;
	}
	
	public void registerKeyBinding(final String name, int key)
	{
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(key, 0), name + "_pressed");
		contentPane.getActionMap().put(name + "_pressed", new AbstractAction() {
			private static final long serialVersionUID = -753513252932687926L;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				keysDown.add(name);
			}
		});
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(key, 0, true), name + "_released");
		contentPane.getActionMap().put(name + "_released", new AbstractAction() {
			private static final long serialVersionUID = -3429603800043274550L;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				keysDown.remove(name);
			}
		});
	}
	
	public boolean isKeyDown(String name)
	{
		return keysDown.contains(name);
	}
	
	private class CustomContentPane extends JPanel
	{
		
		private static final long serialVersionUID = -5888940429070142635L;
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
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
		
	}
	
}
