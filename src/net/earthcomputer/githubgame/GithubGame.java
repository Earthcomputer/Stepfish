package net.earthcomputer.githubgame;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class. We still need to decide what the game is about :,(
 * 
 * @author Earthcomputer
 *
 */
public class GithubGame implements Thread.UncaughtExceptionHandler {

	public static final String GAME_NAME = "Github Game";
	public static final String GAME_VERSION = "0.1 Alpha";
	public static final int FRAMERATE = 60;
	private static final int MILLIS_PER_FRAME = 1000 / FRAMERATE;
	public static final int TICKRATE = 30;
	private static final int MILLIS_PER_TICK = 1000 / TICKRATE;
	public static final Map<Integer, Class<? extends GameObject>> objectTypes = new HashMap<Integer, Class<? extends GameObject>>();

	/**
	 * The singleton instance
	 */
	private static GithubGame INSTANCE;

	private MainWindow theWindow;
	private boolean runningLoop = false;

	public static void main(String[] args) {
		INSTANCE = new GithubGame();
		INSTANCE.startGame();
	}

	public static GithubGame getInstance() {
		return INSTANCE;
	}

	/**
	 * Called when the game starts
	 */
	private void startGame() {
		Thread.setDefaultUncaughtExceptionHandler(this);
		runningLoop = true;
		theWindow = new MainWindow();

		objectTypes.put(0, PlayerObject.class);
		
		registerKeyBinding("moveLeft", KeyEvent.VK_LEFT);
		registerKeyBinding("moveRight", KeyEvent.VK_RIGHT);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (runningLoop) {
					long startFrame = System.currentTimeMillis(), timeToSleep;

					theWindow.redraw();

					timeToSleep = MILLIS_PER_FRAME - (System.currentTimeMillis() - startFrame);
					if (timeToSleep > 0) {
						try {
							Thread.sleep(timeToSleep);
						} catch (InterruptedException e) {
							// Moving to the next frame doesn't hurt
						}
					}
				}
			}
		}, "Drawing Thread").start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (runningLoop) {
					long startTick = System.currentTimeMillis(), timeToSleep;

					theWindow.updateTick();

					timeToSleep = MILLIS_PER_TICK - (System.currentTimeMillis() - startTick);
					if (timeToSleep > 0) {
						try {
							Thread.sleep(timeToSleep);
						} catch (InterruptedException e) {
							throw new RuntimeException("Ticking thread interrupted");
						}
					}
				}
			}
		}, "Ticking Thread").start();

		theWindow.addObject(20, 20, PlayerObject.class);
	}

	/**
	 * Called to end the game
	 */
	public void shutdown() {
		runningLoop = false;
		theWindow.disposeWindow();
	}

	/**
	 * Returns the main window
	 */
	public MainWindow getWindow() {
		return theWindow;
	}

	public void registerKeyBinding(String name, int key) {
		theWindow.registerKeyBinding(name, key);
	}
	
	public boolean isKeyDown(String name) {
		return theWindow.isKeyDown(name);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(GAME_NAME + " has caught an uncaught exception in thread \"" + t.getName() + "\"");
		System.err.println("Game version: " + GAME_VERSION);
		e.printStackTrace();
		shutdown();
	}

}
