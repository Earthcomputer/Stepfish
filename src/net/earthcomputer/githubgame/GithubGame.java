package net.earthcomputer.githubgame;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.earthcomputer.githubgame.object.ElementSwitcherObject;
import net.earthcomputer.githubgame.object.EnumElement;
import net.earthcomputer.githubgame.object.ExitObject;
import net.earthcomputer.githubgame.object.FlyingCrossObject;
import net.earthcomputer.githubgame.object.GameObject;
import net.earthcomputer.githubgame.object.HoppingTriangleObject;
import net.earthcomputer.githubgame.object.PlayerObject;
import net.earthcomputer.githubgame.object.StarObject;
import net.earthcomputer.githubgame.object.WallObject;
import net.earthcomputer.githubgame.util.GameObjectCreator;

/** The main class. We still need to decide what the game is about :,(
 * 
 * @author Earthcomputer */
public class GithubGame implements Thread.UncaughtExceptionHandler
{
	
	public static final String GAME_NAME = "Github Game";
	public static final String GAME_VERSION = "0.1 Alpha";
	public static final int TICKRATE = 30;
	private static final int MILLIS_PER_TICK = 1000 / TICKRATE;
	public static final int FRAMERATE = TICKRATE;
	private static final int MILLIS_PER_FRAME = 1000 / FRAMERATE;
	public static final Map<Integer, GameObjectCreator<? extends GameObject>> objectCreatorsById = new HashMap<Integer, GameObjectCreator<? extends GameObject>>();
	public static final Map<Integer, String> levelNamesByIndex = new HashMap<Integer, String>();
	
	/** The singleton instance */
	private static GithubGame INSTANCE;
	
	private MainWindow theWindow;
	private boolean runningLoop = false;
	
	public static void main(String[] args)
	{
		INSTANCE = new GithubGame();
		INSTANCE.startGame();
	}
	
	public static GithubGame getInstance()
	{
		return INSTANCE;
	}
	
	/** Called when the game starts */
	private void startGame()
	{
		Thread.setDefaultUncaughtExceptionHandler(this);
		
		Scanner indexesScanner = new Scanner(
			new BufferedInputStream(GithubGame.class.getResourceAsStream("/levels/indexes")));
		while(indexesScanner.hasNextLine())
		{
			String line = indexesScanner.nextLine();
			int colonIndex = line.indexOf(':');
			if(colonIndex != -1)
			{
				String indexString = line.substring(0, colonIndex);
				String levelName = line.substring(colonIndex + 1);
				try
				{
					levelNamesByIndex.put(Integer.parseInt(indexString), levelName);
				}
				catch (NumberFormatException e)
				{
					// ignore invalid lines
				}
			}
		}
		indexesScanner.close();
		
		runningLoop = true;
		theWindow = new MainWindow();
		
		registerType(0, PlayerObject.class);
		registerType(1, WallObject.class);
		registerType(2, WallObject.class, EnumElement.EARTH);
		registerType(3, WallObject.class, EnumElement.WATER);
		registerType(4, WallObject.class, EnumElement.AIR);
		registerType(5, WallObject.class, EnumElement.FIRE);
		registerType(6, StarObject.class);
		registerType(7, ElementSwitcherObject.class);
		registerType(8, FlyingCrossObject.class);
		registerType(9, HoppingTriangleObject.class);
		registerType(10, ExitObject.class);
		
		registerKeyBinding("moveLeft", KeyEvent.VK_LEFT);
		registerKeyBinding("moveRight", KeyEvent.VK_RIGHT);
		
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				while(runningLoop)
				{
					long startTick = System.currentTimeMillis(), timeToSleep;
					
					theWindow.updateTick();
					
					timeToSleep = MILLIS_PER_TICK - (System.currentTimeMillis() - startTick);
					if(timeToSleep > 0)
					{
						try
						{
							Thread.sleep(timeToSleep);
						}
						catch (InterruptedException e)
						{
							throw new RuntimeException("Ticking thread interrupted");
						}
					}
				}
			}
		}, "Ticking Thread").start();
		
		theWindow.loadLevel(0);
	}
	
	/** Called to end the game */
	public void shutdown()
	{
		runningLoop = false;
		theWindow.disposeWindow();
	}
	
	/** Returns the main window */
	public MainWindow getWindow()
	{
		return theWindow;
	}
	
	public void registerKeyBinding(String name, int key)
	{
		theWindow.registerKeyBinding(name, key);
	}
	
	public boolean isKeyDown(String name)
	{
		return theWindow.isKeyDown(name);
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		System.err.println(GAME_NAME + " has caught an uncaught exception in thread \"" + t.getName() + "\"");
		System.err.println("Game version: " + GAME_VERSION);
		e.printStackTrace();
		shutdown();
	}
	
	private <T extends GameObject> void registerType(int id, final Class<T> clazz, Object... extraCtorArgs)
	{
		if(extraCtorArgs == null) extraCtorArgs = new Object[0];
		final Class<?>[] ctorArgsTypes = new Class<?>[extraCtorArgs.length + 2];
		ctorArgsTypes[0] = double.class;
		ctorArgsTypes[1] = double.class;
		for(int i = 0; i < extraCtorArgs.length; i++)
		{
			ctorArgsTypes[i + 2] = extraCtorArgs[i].getClass();
		}
		final Object[] workingExtraCtorArgs = extraCtorArgs;
		objectCreatorsById.put(id, new GameObjectCreator<T>() {
			@Override
			public T create(double x, double y)
			{
				Object[] args = new Object[ctorArgsTypes.length];
				args[0] = x;
				args[1] = y;
				System.arraycopy(workingExtraCtorArgs, 0, args, 2, workingExtraCtorArgs.length);
				try
				{
					return clazz.getConstructor(ctorArgsTypes).newInstance(args);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
}
