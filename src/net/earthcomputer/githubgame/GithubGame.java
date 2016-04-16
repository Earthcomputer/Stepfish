package net.earthcomputer.githubgame;

import java.awt.event.KeyEvent;

import net.earthcomputer.githubgame.object.ObjectTypes;

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
		
		runningLoop = true;
		theWindow = new MainWindow();
		
		ObjectTypes.registerTypes();
		
		registerKeyBinding("moveLeft", KeyEvent.VK_LEFT);
		registerKeyBinding("moveRight", KeyEvent.VK_RIGHT);
		registerKeyBinding("jump", KeyEvent.VK_SPACE);
		registerKeyBinding("closeGui", KeyEvent.VK_ESCAPE);
		
		theWindow.loadLevel(0);
		
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
	
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		System.err.println(GAME_NAME + " has caught an uncaught exception in thread \"" + t.getName() + "\"");
		System.err.println("Game version: " + GAME_VERSION);
		e.printStackTrace();
		shutdown();
	}
	
}
