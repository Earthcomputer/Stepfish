package net.earthcomputer.githubgame.util;

import java.util.HashSet;
import java.util.Set;

public class Keyboard
{
	private static final Set<String> keysPressed = new HashSet<String>();
	private static final Set<String> keysReleased = new HashSet<String>();
	private static final Set<String> keysDown = new HashSet<String>();
	private static final Set<String> pendingKeysPressed = new HashSet<String>();
	private static final Set<String> pendingKeysReleased = new HashSet<String>();
	private static boolean enableRepeatEvents = false;
	
	private static final Object SYNC_LOCK = new Object();
	
	private Keyboard()
	{
	}
	
	public static boolean isKeyPressed(String keyBinding)
	{
		synchronized(SYNC_LOCK)
		{
			return keysPressed.contains(keyBinding);
		}
	}
	
	public static boolean isKeyReleased(String keyBinding)
	{
		synchronized(SYNC_LOCK)
		{
			return keysReleased.contains(keyBinding);
		}
	}
	
	public static boolean isKeyDown(String keyBinding)
	{
		synchronized(SYNC_LOCK)
		{
			return keysDown.contains(keyBinding);
		}
	}
	
	public static void pressKey(String keyBinding)
	{
		synchronized(SYNC_LOCK)
		{
			if(enableRepeatEvents || !keysDown.contains(keyBinding)) pendingKeysPressed.add(keyBinding);
		}
	}
	
	public static void releaseKey(String keyBinding)
	{
		synchronized(SYNC_LOCK)
		{
			pendingKeysReleased.add(keyBinding);
		}
	}
	
	public static void clearKeys()
	{
		synchronized(SYNC_LOCK)
		{
			pendingKeysReleased.addAll(keysDown);
		}
	}
	
	public static void enableRepeatEvents()
	{
		synchronized(SYNC_LOCK)
		{
			enableRepeatEvents = true;
		}
	}
	
	public static void disableRepeatEvents()
	{
		synchronized(SYNC_LOCK)
		{
			enableRepeatEvents = false;
		}
	}
	
	public static void updateTick()
	{
		synchronized(SYNC_LOCK)
		{
			keysPressed.clear();
			for(String keyBinding : pendingKeysPressed)
			{
				keysPressed.add(keyBinding);
				keysDown.add(keyBinding);
			}
			pendingKeysPressed.clear();
			
			keysReleased.clear();
			for(String keyBinding : pendingKeysReleased)
			{
				keysReleased.add(keyBinding);
				keysDown.remove(keyBinding);
			}
			pendingKeysReleased.clear();
		}
	}
}
