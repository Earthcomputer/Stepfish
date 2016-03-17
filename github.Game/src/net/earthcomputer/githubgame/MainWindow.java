package net.earthcomputer.githubgame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MainWindow {

	private static final Dimension PREFERRED_SIZE = new Dimension(640, 480);

	private final JFrame theFrame;
	private CustomContentPane contentPane;

	private List<GameObject> objects = new ArrayList<GameObject>();
	private List<IUpdateListener> updateListeners = new ArrayList<IUpdateListener>();
	private Set<String> keysDown = new HashSet<String>();

	public MainWindow() {
		theFrame = new JFrame(GithubGame.GAME_NAME + " " + GithubGame.GAME_VERSION);

		theFrame.setContentPane(contentPane = new CustomContentPane());
		contentPane.setPreferredSize(PREFERRED_SIZE);
		theFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GithubGame.getInstance().shutdown();
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				keysDown.clear();
			}
		});
		theFrame.pack();
		theFrame.setLocationRelativeTo(null);
		theFrame.setVisible(true);
	}

	public void disposeWindow() {
		theFrame.dispose();
	}

	public void addObject(GameObject object) {
		objects.add(object);
		if (object instanceof IUpdateListener)
			addUpdateListener((IUpdateListener) object);
	}

	public void removeObject(GameObject object) {
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

	public void redraw() {
		theFrame.repaint();
	}

	public void updateTick() {
		for (int i = updateListeners.size() - 1; i >= 0; i--) {
			updateListeners.get(i).update();
		}
	}

	public int getWidth() {
		return contentPane.getSize().width;
	}

	public int getHeight() {
		return contentPane.getSize().height;
	}

	public void registerKeyBinding(final String name, int key) {
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(key, 0), name + "_pressed");
		contentPane.getActionMap().put(name + "_pressed", new AbstractAction() {
			private static final long serialVersionUID = -753513252932687926L;

			@Override
			public void actionPerformed(ActionEvent e) {
				keysDown.add(name);
			}
		});
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(key, 0, true), name + "_released");
		contentPane.getActionMap().put(name + "_released", new AbstractAction() {
			private static final long serialVersionUID = -3429603800043274550L;

			@Override
			public void actionPerformed(ActionEvent e) {
				keysDown.remove(name);
			}
		});
	}

	public boolean isKeyDown(String name) {
		return keysDown.contains(name);
	}

	private class CustomContentPane extends JPanel {

		private static final long serialVersionUID = -5888940429070142635L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int i = objects.size() - 1; i >= 0; i--) {
				objects.get(i).draw(g);
			}
		}

	}

}
