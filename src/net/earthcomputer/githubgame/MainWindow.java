package net.earthcomputer.githubgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.earthcomputer.githubgame.object.GameObject;
import net.earthcomputer.githubgame.util.GameObjectCreator;

public class MainWindow
{
	
	private static final Dimension PREFERRED_SIZE = new Dimension(640, 480);
	private static final int CURRENT_LEVEL_VERSION = 0;
	
	private final JFrame theFrame;
	private CustomContentPane contentPane;
	
	private List<GameObject> objects = new ArrayList<GameObject>();
	private List<IUpdateListener> updateListeners = new ArrayList<IUpdateListener>();
	private Set<String> keysDown = new HashSet<String>();
	
	private int levelWidth;
	private int levelHeight;
	
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
		return addObject(x, y, GithubGame.objectCreatorsById.get(id));
	}
	
	public <T extends GameObject> T addObject(double x, double y, GameObjectCreator<T> creator)
	{
		T instance = creator.create(x, y);
		if(instance != null)
		{
			objects.add(instance);
			if(instance instanceof IUpdateListener) addUpdateListener((IUpdateListener) instance);
		}
		return instance;
	}
	
	public void removeObject(GameObject object)
	{
		objects.remove(object);
		if(object instanceof IUpdateListener) removeUpdateListener((IUpdateListener) object);
	}
	
	public void addUpdateListener(IUpdateListener updateListener)
	{
		updateListeners.add(updateListener);
	}
	
	public void removeUpdateListener(IUpdateListener updateListener)
	{
		updateListeners.remove(updateListener);
	}
	
	public boolean loadLevel(int levelId)
	{
		return loadLevel(GithubGame.levelNamesByIndex.get(levelId));
	}
	
	public boolean loadLevel(String levelName)
	{
		DataInputStream input = new DataInputStream(new BufferedInputStream(
			MainWindow.class.getResourceAsStream(String.format("/levels/%s.gglevel", levelName))));
			
		try
		{
			if(input.readInt() != 0x4748474D) return false;
			
			int version = input.readUnsignedByte();
			if(version > CURRENT_LEVEL_VERSION) return false;
			
			input.readUTF();
			
			int levelWidth = input.readUnsignedShort();
			int levelHeight = input.readUnsignedShort();
			
			int objectCount = input.readUnsignedShort();
			int[] xs = new int[objectCount];
			int[] ys = new int[objectCount];
			int[] ids = new int[objectCount];
			for(int i = 0; i < objectCount; i++)
			{
				xs[i] = input.readInt();
				ys[i] = input.readInt();
				ids[i] = input.readUnsignedShort();
			}
			
			this.levelWidth = levelWidth;
			this.levelHeight = levelHeight;
			this.objects.clear();
			this.updateListeners.clear();
			for(int i = 0; i < objectCount; i++)
			{
				this.addObject(xs[i], ys[i], ids[i]);
			}
		}
		catch (IOException e)
		{
			return false;
		}
		
		return true;
	}
	
	public void redraw()
	{
		theFrame.repaint();
	}
	
	public void updateTick()
	{
		for(IUpdateListener updateListener : new ArrayList<IUpdateListener>(updateListeners))
		{
			updateListener.update();
		}
		
		checkCollision();
	}
	
	private void checkCollision()
	{
		List<GameObject> objects = new ArrayList<GameObject>(this.objects);
		for(GameObject object1 : objects)
		{
			if(object1.receiveCollisionEvents())
			{
				for(GameObject object2 : objects)
				{
					if(object1 != object2)
					{
						object1.onCollidedWith(object2);
					}
				}
			}
		}
	}
	
	public int getWidth()
	{
		return contentPane.getSize().width;
	}
	
	public int getHeight()
	{
		return contentPane.getSize().height;
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
			
			for(GameObject object : new ArrayList<GameObject>(objects))
			{
				object.draw(g);
			}
		}
		
	}
	
}
