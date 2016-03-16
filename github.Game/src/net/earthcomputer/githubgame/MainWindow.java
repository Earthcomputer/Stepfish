package net.earthcomputer.githubgame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, List<IKeyListener>> keyListeners = new HashMap<String, List<IKeyListener>>();

	public MainWindow() {
		theFrame = new JFrame(GithubGame.GAME_NAME + " " + GithubGame.GAME_VERSION);

		theFrame.setContentPane(contentPane = new CustomContentPane());
		contentPane.setPreferredSize(PREFERRED_SIZE);
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

	public void addObject(GameObject object) {
		objects.add(object);
		if (object instanceof IUpdateListener)
			addUpdateListener((IUpdateListener) object);
		if (object instanceof IKeyListener)
			addKeyListener((IKeyListener) object);
	}

	public void removeObject(GameObject object) {
		objects.remove(object);
		if (object instanceof IUpdateListener)
			removeUpdateListener((IUpdateListener) object);
		if (object instanceof IKeyListener)
			removeKeyListener((IKeyListener) object);
	}

	public void addUpdateListener(IUpdateListener updateListener) {
		updateListeners.add(updateListener);
	}

	public void removeUpdateListener(IUpdateListener updateListener) {
		updateListeners.remove(updateListener);
	}

	public void addKeyListener(IKeyListener keyListener) {
		String[] listensTo = keyListener.getListensTo();
		for (String id : listensTo) {
			keyListeners.get(id).add(keyListener);
		}
	}

	public void removeKeyListener(IKeyListener keyListener) {
		String[] listensTo = keyListener.getListensTo();
		for (String id : listensTo) {
			keyListeners.get(id).remove(keyListener);
		}
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

	public void registerKeyBinding(final String id, String keyStroke) {
		contentPane.getInputMap().put(KeyStroke.getKeyStroke(keyStroke), id);
		contentPane.getActionMap().put(id, new AbstractAction() {
			private static final long serialVersionUID = -753513252932687926L;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<IKeyListener> listeners = keyListeners.get(id);
				for (int i = listeners.size() - 1; i >= 0; i--) {
					listeners.get(i).onKey(id);
				}
			}
		});
		keyListeners.put(id, new ArrayList<IKeyListener>());
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
