package net.earthcomputer.githubgame;

/**
 * The main class.
 * We still need to decide what the game is about :,(
 * 
 * @author Earthcomputer
 *
 */
public class GithubGame {
	
	public static final String GAME_NAME = "Github Game";
	public static final String GAME_VERSION = "0.1 Alpha";
	
	/**
	 * The singleton instance
	 */
	private static GithubGame INSTANCE;
	
	private MainWindow theWindow;
	
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
		theWindow = new MainWindow();
	}
	
	/**
	 * Called to end the game
	 */
	public void shutdown() {
		theWindow.disposeWindow();
	}
	
	/**
	 * Returns the main window
	 */
	public MainWindow getWindow() {
		return theWindow;
	}
	
}
