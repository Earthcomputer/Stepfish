package net.earthcomputer.galacticgame.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class SoundManager {

	private static boolean isUnix;
	private static boolean mplayerExists;
	private static boolean hasWarnedNoMPlayer = false;

	static {
		String os = System.getProperty("os.name").toLowerCase();
		isUnix = os.contains("nix") || os.contains("nux") || os.contains("aix");

		if (isUnix) {
			try {
				Process process = Runtime.getRuntime().exec("which mplayer-nogui");
				mplayerExists = process.waitFor() == 0;
			} catch (IOException e) {
				mplayerExists = false;
			} catch (InterruptedException e) {
				mplayerExists = false;
			}
		}
	}

	private SoundManager() {
	}

	private static final Map<String, SoundPlayer> sounds = new HashMap<String, SoundPlayer>();

	public static void playSound(String name) {
		playSound(name, null);
	}

	public static void playSound(String name, Runnable onStopped) {
		// Try the cache first
		SoundPlayer sound = sounds.get(name);

		if (sound == null) {
			byte[] soundBytes = null;
			try {
				// Read the sound to a byte array
				InputStream input = new BufferedInputStream(
						SoundManager.class.getResourceAsStream("/sounds/" + name + ".wav"));
				ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192];
				int bytesRead;
				while ((bytesRead = input.read(buffer)) != -1) {
					bytesOut.write(buffer, 0, bytesRead);
				}
				soundBytes = bytesOut.toByteArray();

				// Try the Java audio system first
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundBytes));
				DataLine.Info info = new DataLine.Info(Clip.class, audioInput.getFormat());
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioInput);
				sound = new ClipSoundPlayer(clip);
			} catch (LineUnavailableException e) {
				if (isUnix) {
					// Unix workaround
					if (mplayerExists) {
						assert soundBytes != null : "Should never be null, LineUnavailableException should never be thrown before it is set";
						try {
							sound = new MPlayerSoundPlayer(new ByteArrayInputStream(soundBytes));
						} catch (IOException e1) {
							e1.printStackTrace();
							sound = new DummySoundPlayer();
						}
					} else {
						// Use a dummy sound player, there is no other
						// alternative
						if (!hasWarnedNoMPlayer) {
							JOptionPane.showMessageDialog(null,
									"The program mplayer-nogui is not installed on this computer, unable to play sounds",
									"Warning", JOptionPane.WARNING_MESSAGE);
							hasWarnedNoMPlayer = true;
						}
						sound = new DummySoundPlayer();
					}
				} else {
					sound = new DummySoundPlayer();
				}
			} catch (UnsupportedAudioFileException e) {
				throw new UnableToPlaySoundException(name, e);
			} catch (IOException e) {
				throw new UnableToPlaySoundException(name, e);
			}
			sounds.put(name, sound);
		}

		sound.play(name, onStopped);
	}

	public static void closeAllSounds() {
		for (SoundPlayer sound : sounds.values()) {
			sound.close();
		}
		sounds.clear();
	}

	private static interface SoundPlayer extends AutoCloseable {
		void play(String name, Runnable onStopped);

		@Override
		void close();
	}

	private static class ClipSoundPlayer implements SoundPlayer {
		private Clip clip;
		private LineListener customListener;

		public ClipSoundPlayer(Clip clip) {
			this.clip = clip;
		}

		@Override
		public void play(String name, final Runnable onStopped) {
			if (clip.isRunning())
				clip.stop();
			if (customListener != null) {
				clip.removeLineListener(customListener);
			}
			if (onStopped != null) {
				customListener = new LineListener() {
					@Override
					public void update(LineEvent event) {
						if (event.getType() == LineEvent.Type.STOP) {
							onStopped.run();
						}
					}
				};
				clip.addLineListener(customListener);
			} else {
				customListener = null;
			}
			clip.setFramePosition(0);
			clip.start();
		}

		@Override
		public void close() {
			if (clip.isRunning())
				clip.stop();
			clip.drain();
			clip.close();
		}
	}

	private static class MPlayerSoundPlayer implements SoundPlayer {
		private static int nextId = 0;

		private File sound;
		private Process process;

		public MPlayerSoundPlayer(InputStream sound) throws IOException {
			assert mplayerExists;
			this.sound = File.createTempFile("stepfish_sound_" + (nextId++), "wav");
			this.sound.deleteOnExit();
			Files.copy(sound, this.sound.toPath());
		}

		@Override
		public void play(final String name, final Runnable onStopped) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (process != null) {
							process = Runtime.getRuntime().exec("mplayer-nogui \"" + sound.getAbsolutePath() + "\"");
							process.waitFor();
							synchronized (MPlayerSoundPlayer.this) {
								onStopped.run();
								process = null;
							}
						}
					} catch (IOException e) {
						throw new UnableToPlaySoundException(name, e);
					} catch (InterruptedException e) {
						throw new UnableToPlaySoundException(name, e);
					}
				}
			});
			thread.start();
		}

		@Override
		public void close() {
			synchronized (this) {
				if (process != null) {
					process.destroy();
				}
			}
		}
	}

	private static class DummySoundPlayer implements SoundPlayer {
		@Override
		public void play(String name, Runnable onStopped) {
			// nop
		}

		@Override
		public void close() {
			// nop
		}
	}

	public static class UnableToPlaySoundException extends RuntimeException {
		private static final long serialVersionUID = 7241237428136778819L;

		public UnableToPlaySoundException(String sound, Throwable cause) {
			super(sound, cause);
		}

		public UnableToPlaySoundException(String sound) {
			super(sound);
		}
	}

}
