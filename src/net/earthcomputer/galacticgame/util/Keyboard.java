package net.earthcomputer.galacticgame.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Keyboard extends KeyAdapter
{
	
	private static final Keyboard INSTANCE = new Keyboard();
	
	private final Map<Integer, Set<String>> keyBindingsCodeToName = new HashMap<Integer, Set<String>>();
	private final Map<String, Set<Integer>> keyBindingsNameToCode = new HashMap<String, Set<Integer>>();
	private final Set<Integer> keysPressed = new HashSet<Integer>();
	private final Set<Integer> keysReleased = new HashSet<Integer>();
	private final Set<Integer> keysDown = new HashSet<Integer>();
	private final Set<Integer> pendingKeysPressed = new HashSet<Integer>();
	private final Set<Integer> pendingKeysReleased = new HashSet<Integer>();
	private boolean enableRepeatEvents = false;
	
	private final Object SYNC_LOCK = new Object();
	
	private Keyboard()
	{
	}
	
	public static Keyboard instance()
	{
		return INSTANCE;
	}
	
	public static void registerKeyBindings()
	{
		bindKey(KeyEvent.VK_LEFT, "moveLeft");
		bindKey(KeyEvent.VK_RIGHT, "moveRight");
		bindKey(KeyEvent.VK_SPACE, "jump");
		bindKey(KeyEvent.VK_UP, "jump");
		bindKey(KeyEvent.VK_ESCAPE, "closeGui");
		bindKey(KeyEvent.VK_UP, "scrollUp");
		bindKey(KeyEvent.VK_DOWN, "scrollDown");
	}
	
	public static void bindKey(int keyCode, String name)
	{
		INSTANCE.doBindKey(keyCode, name);
	}
	
	private void doBindKey(int keyCode, String name)
	{
		if(!keyBindingsCodeToName.containsKey(keyCode))
		{
			keyBindingsCodeToName.put(keyCode, new HashSet<String>());
		}
		keyBindingsCodeToName.get(keyCode).add(name);
		
		if(!keyBindingsNameToCode.containsKey(name))
		{
			keyBindingsNameToCode.put(name, new HashSet<Integer>());
		}
		keyBindingsNameToCode.get(name).add(keyCode);
	}
	
	public static boolean isKeyPressed(String keyBinding)
	{
		return INSTANCE.doIsKeyPressed(keyBinding);
	}
	
	private boolean doIsKeyPressed(String keyBinding)
	{
		Set<Integer> possibleKeys = keyBindingsNameToCode.get(keyBinding);
		if(possibleKeys == null){ return false; }
		synchronized(SYNC_LOCK)
		{
			for(Integer keyPressed : keysPressed)
			{
				if(possibleKeys.contains(keyPressed)){ return true; }
			}
		}
		return false;
	}
	
	public static boolean isKeyReleased(String keyBinding)
	{
		return INSTANCE.doIsKeyReleased(keyBinding);
	}
	
	private boolean doIsKeyReleased(String keyBinding)
	{
		Set<Integer> possibleKeys = keyBindingsNameToCode.get(keyBinding);
		if(possibleKeys == null){ return false; }
		synchronized(SYNC_LOCK)
		{
			for(Integer keyReleased : keysReleased)
			{
				if(possibleKeys.contains(keyReleased)){ return true; }
			}
		}
		return false;
	}
	
	public static boolean isKeyDown(String keyBinding)
	{
		return INSTANCE.doIsKeyDown(keyBinding);
	}
	
	private boolean doIsKeyDown(String keyBinding)
	{
		Set<Integer> possibleKeys = keyBindingsNameToCode.get(keyBinding);
		if(possibleKeys == null){ return false; }
		synchronized(SYNC_LOCK)
		{
			for(Integer keyDown : keysDown)
			{
				if(possibleKeys.contains(keyDown)){ return true; }
			}
		}
		return false;
	}
	
	public static void pressKey(int keyCode)
	{
		INSTANCE.doPressKey(keyCode);
	}
	
	private void doPressKey(int keyCode)
	{
		synchronized(SYNC_LOCK)
		{
			if(enableRepeatEvents || !keysDown.contains(keyCode)) pendingKeysPressed.add(keyCode);
		}
	}
	
	public static void pressKey(String keyBinding)
	{
		INSTANCE.doPressKey(keyBinding);
	}
	
	private void doPressKey(String keyBinding)
	{
		if(keyBindingsNameToCode.containsKey(keyBinding))
		{
			for(Integer key : keyBindingsNameToCode.get(keyBinding))
			{
				doPressKey(key);
			}
		}
	}
	
	public static void releaseKey(int keyCode)
	{
		INSTANCE.doReleaseKey(keyCode);
	}
	
	private void doReleaseKey(int keyCode)
	{
		synchronized(SYNC_LOCK)
		{
			pendingKeysReleased.add(keyCode);
		}
	}
	
	public static void releaseKey(String keyBinding)
	{
		INSTANCE.doReleaseKey(keyBinding);
	}
	
	private void doReleaseKey(String keyBinding)
	{
		if(keyBindingsNameToCode.containsKey(keyBinding))
		{
			for(Integer key : keyBindingsNameToCode.get(keyBinding))
			{
				doReleaseKey(key);
			}
		}
	}
	
	public static void clearKeys()
	{
		INSTANCE.doClearKeys();
	}
	
	private void doClearKeys()
	{
		synchronized(SYNC_LOCK)
		{
			pendingKeysReleased.addAll(keysDown);
		}
	}
	
	public static void enableRepeatEvents()
	{
		INSTANCE.doEnableRepeatEvents();
	}
	
	private void doEnableRepeatEvents()
	{
		synchronized(SYNC_LOCK)
		{
			enableRepeatEvents = true;
		}
	}
	
	public static void disableRepeatEvents()
	{
		INSTANCE.doDisableRepeatEvents();
	}
	
	private void doDisableRepeatEvents()
	{
		synchronized(SYNC_LOCK)
		{
			enableRepeatEvents = false;
		}
	}
	
	public static void updateTick()
	{
		INSTANCE.doUpdateTick();
	}
	
	private void doUpdateTick()
	{
		synchronized(SYNC_LOCK)
		{
			keysPressed.clear();
			for(Integer key : pendingKeysPressed)
			{
				keysPressed.add(key);
				keysDown.add(key);
			}
			pendingKeysPressed.clear();
			
			keysReleased.clear();
			for(Integer key : pendingKeysReleased)
			{
				keysReleased.add(key);
				keysDown.remove(key);
			}
			pendingKeysReleased.clear();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		doPressKey(event.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent event)
	{
		doReleaseKey(event.getKeyCode());
	}
}
