package net.earthcomputer.galacticgame;

import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.earthcomputer.galacticgame.gui.GuiMainMenu;
import net.earthcomputer.galacticgame.object.ObjectTypes;
import net.earthcomputer.galacticgame.util.Keyboard;
import net.earthcomputer.galacticgame.util.Profiles;
import net.earthcomputer.galacticgame.util.SoundManager;

public class GalacticGame implements Thread.UncaughtExceptionHandler
{
	
	public static final String GAME_NAME = "Galactic Game";
	public static final String GAME_VERSION = "1.0 Beta";
	public static final int TICKRATE = 30;
	private static final int MILLIS_PER_TICK = 1000 / TICKRATE;
	public static final int FRAMERATE = TICKRATE;
	
	/** The singleton instance */
	private static GalacticGame INSTANCE;
	
	private MainWindow theWindow;
	private boolean runningLoop = false;
	
	private long timeSlept = 0;
	
	public static void main(String[] args)
	{
		INSTANCE = new GalacticGame();
		INSTANCE.startGame();
	}
	
	public static GalacticGame getInstance()
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
		
		Keyboard.registerKeyBindings();
		
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
		
		while(runningLoop)
		{
			long startTick = System.currentTimeMillis(), timeToSleep;
			
			timeSlept = 0;
			
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
			else if(-timeToSleep > timeSlept)
			{
				System.out.println(
					"One tick took longer than expected: we're behind by " + (-timeToSleep) + " milliseconds!");
			}
		}
	}
	
	/** Called to end the game */
	public void shutdown()
	{
		runningLoop = false;
		SoundManager.closeAllSounds();
		theWindow.disposeWindow();
	}
	
	/** Returns the main window */
	public MainWindow getWindow()
	{
		return theWindow;
	}
	
	public void sleep(long millis)
	{
		timeSlept += millis;
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
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
