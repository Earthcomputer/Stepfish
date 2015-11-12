package net.earthcomputer.githubgame;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class MainWindow {

	private static final Dimension PREFERRED_SIZE = new Dimension(640, 480);

	private final JFrame theFrame;

	private List<GameComponent> objects = new ArrayList<GameComponent>();
	private List<IUpdateListener> updateListeners = new ArrayList<IUpdateListener>();

	public MainWindow() {
		theFrame = new JFrame();

		theFrame.getContentPane().setPreferredSize(PREFERRED_SIZE);
		// Dangerous, make sure to compensate with a simple API
		theFrame.getContentPane().setLayout(null);
		theFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GithubGame.getInstance().shutdown();
			}
		});
		theFrame.pack();
		theFrame.setLocationRelativeTo(null);
		theFrame.setVisible(true);
	}

	public void disposeWindow() {
		theFrame.dispose();
	}

	public void addObject(GameComponent object) {
		theFrame.getContentPane().add(object);
		objects.add(object);
		if (object instanceof IUpdateListener)
			addUpdateListener((IUpdateListener) object);
	}

	public void removeObject(GameComponent object) {
		theFrame.getContentPane().remove(object);
		objects.remove(object);
		if (object instanceof IUpdateListener)
			removeUpdateListener((IUpdateListener) object);
	}

	public void addUpdateListener(IUpdateListener updateListener) {
		updateListeners.add(updateListener);
	}

	public void removeUpdateListener(IUpdateListener updateListener) {
		updateListeners.remove(updateListener);
	}

}
