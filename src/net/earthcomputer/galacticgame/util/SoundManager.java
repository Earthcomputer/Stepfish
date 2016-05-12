package net.earthcomputer.galacticgame.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager
{
	
	private SoundManager()
	{
	}
	
	private static final Map<String, SoundPlayer> sounds = new HashMap<String, SoundPlayer>();
	
	public static void playSound(String name)
	{
		playSound(name, null);
	}
	
	public static void playSound(String name, LineListener listener)
	{
		SoundPlayer sound = sounds.get(name);
		if(sound == null)
		{
			Clip clip;
			try
			{
				clip = AudioSystem.getClip();
				AudioInputStream audioInput = AudioSystem
					.getAudioInputStream(SoundManager.class.getResourceAsStream("/sounds/" + name + ".wav"));
				clip.open(audioInput);
			}
			catch (LineUnavailableException e)
			{
				throw new UnableToPlaySoundException(name, e);
			}
			catch (UnsupportedAudioFileException e)
			{
				throw new UnableToPlaySoundException(name, e);
			}
			catch (IOException e)
			{
				throw new UnableToPlaySoundException(name, e);
			}
			sound = new SoundPlayer(clip);
			sounds.put(name, sound);
		}
		sound.play(listener);
	}
	
	public static void closeAllSounds()
	{
		for(SoundPlayer sound : sounds.values())
		{
			sound.close();
		}
		sounds.clear();
	}
	
	private static class SoundPlayer implements AutoCloseable
	{
		private Clip clip;
		private LineListener customListener;
		
		public SoundPlayer(Clip clip)
		{
			this.clip = clip;
		}
		
		public void play(LineListener customListener)
		{
			if(clip.isRunning()) clip.stop();
			if(this.customListener != null)
			{
				clip.removeLineListener(this.customListener);
			}
			this.customListener = customListener;
			if(customListener != null)
			{
				clip.addLineListener(customListener);
			}
			clip.setFramePosition(0);
			clip.start();
		}
		
		@Override
		public void close()
		{
			if(clip.isRunning()) clip.stop();
			clip.drain();
			clip.close();
		}
	}
	
	public static class UnableToPlaySoundException extends RuntimeException
	{
		private static final long serialVersionUID = 7241237428136778819L;
		
		public UnableToPlaySoundException(String sound, Throwable cause)
		{
			super(sound, cause);
		}
		
		public UnableToPlaySoundException(String sound)
		{
			super(sound);
		}
	}
	
}
