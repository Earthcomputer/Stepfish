package net.earthcomputer.githubgame.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.earthcomputer.githubgame.GithubGame;

public class MainWindow {

	private static final Dimension PREFERRED_SIZE = new Dimension(640, 480);
	
	private final JFrame theFrame;

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

}
