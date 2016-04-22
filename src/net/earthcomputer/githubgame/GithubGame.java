package net.earthcomputer.githubgame;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.earthcomputer.githubgame.gui.GuiMainMenu;
import net.earthcomputer.githubgame.object.ObjectTypes;
import net.earthcomputer.githubgame.util.Profiles;

/** The main class. We still need to decide what the game is about :,(
 * 
 * @author Earthcomputer */
public class GithubGame implements Thread.UncaughtExceptionHandler
{
	
	public static final String GAME_NAME = "Galactic Game";
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
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			// Non-fatal error
			e.printStackTrace();
		}
		
		runningLoop = true;
		theWindow = new MainWindow();
		
		ObjectTypes.registerTypes();
		
		registerKeyBinding("moveLeft", KeyEvent.VK_LEFT);
		registerKeyBinding("moveRight", KeyEvent.VK_RIGHT);
		registerKeyBinding("jump", KeyEvent.VK_SPACE);
		registerKeyBinding("closeGui", KeyEvent.VK_ESCAPE);
		registerKeyBinding("scrollUp", KeyEvent.VK_UP);
		registerKeyBinding("scrollDown", KeyEvent.VK_DOWN);
		
		try
		{
			if(!Profiles.loadProfiles())
			{
				System.err.println("Profiles file had invalid format");
				JOptionPane.showMessageDialog(null,
					"The profiles file was found to have an invalid format. Try updating the game.", GAME_NAME,
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred while reading from profiles file", GAME_NAME,
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		theWindow.openGui(new GuiMainMenu());
		
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
	
	public static String randomGenTitle(int seed)
	{
		Random rand = new Random(seed);
		char[] chars = new char[16];
		for(int i = 0; i < chars.length; i++)
		{
			int ascii = rand.nextInt(52);
			if(ascii < 26)
			{
				ascii += 'A';
			}
			else
			{
				ascii -= 26;
				ascii += 'a';
			}
			chars[i] = (char) ascii;
		}
		return new String(chars);
	}
	
}
