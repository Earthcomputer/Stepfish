package net.earthcomputer.githubgame.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.earthcomputer.githubgame.GithubGame;

public class Profiles
{
	
	private static final int CURRENT_PROFILES_VERSION = 0;
	private static final List<Profile> profiles = new ArrayList<Profile>();
	
	private Profiles()
	{
	}
	
	private static File getProfilesFile()
	{
		String dirName = System.getenv("appdata");
		File dir;
		if(dirName == null)
		{
			dirName = System.getProperty("user.home");
		}
		if(dirName == null)
		{
			dir = File.listRoots()[0];
		}
		else
		{
			dir = new File(dirName);
			if(!dir.isDirectory())
			{
				dir = File.listRoots()[0];
			}
		}
		
		dir = new File(dir, ".earthcomputer");
		if(!dir.exists())
		{
			dir.mkdir();
			// TODO: hide file
		}
		
		dir = new File(dir, "games");
		dir = new File(dir, GithubGame.GAME_NAME);
		dir.mkdirs();
		
		File profilesFile = new File(dir, "profiles.dat");
		return profilesFile;
	}
	
	public static boolean loadProfiles() throws IOException
	{
		File profilesFile = getProfilesFile();
		if(!profilesFile.exists())
		{
			profiles.clear();
			return true;
		}
		DataInputStream input = new DataInputStream(
			new GZIPInputStream(new BufferedInputStream(new FileInputStream(profilesFile))));
			
		int profileCount;
		String[] profileNames;
		int[] levelsTheyreOn;
		int[][] starsObtained;
		int[] totalStarsObtained;
		
		try
		{
			if(input.readInt() != 0x5052464c){ return false; }
			
			int version = input.readUnsignedByte();
			if(version > CURRENT_PROFILES_VERSION){ return false; }
			
			profileCount = input.readUnsignedByte();
			
			profileNames = new String[profileCount];
			levelsTheyreOn = new int[profileCount];
			starsObtained = new int[profileCount][];
			totalStarsObtained = new int[profileCount];
			
			for(int profile = 0; profile < profileCount; profile++)
			{
				char[] nameChars = new char[16];
				for(int i = 0; i < nameChars.length; i++)
				{
					nameChars[i] = (char) input.readUnsignedByte();
				}
				profileNames[profile] = new String(nameChars);
				
				int levelTheyreOn = input.readUnsignedByte();
				levelsTheyreOn[profile] = levelTheyreOn;
				
				int[] starsTheyveObtained = new int[levelTheyreOn];
				starsObtained[profile] = starsTheyveObtained;
				for(int i = 0; i < levelTheyreOn; i++)
				{
					starsTheyveObtained[i] = input.readUnsignedByte();
				}
				
				totalStarsObtained[profile] = input.readUnsignedShort();
			}
		}
		finally
		{
			input.close();
		}
		
		profiles.clear();
		for(int i = 0; i < profileCount; i++)
		{
			profiles.add(new Profile(profileNames[i], levelsTheyreOn[i], starsObtained[i], totalStarsObtained[i]));
		}
		
		return true;
	}
	
	public static void saveProfiles() throws IOException
	{
		File profilesFile = getProfilesFile();
		if(!profilesFile.exists())
		{
			profilesFile.createNewFile();
		}
		
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(profilesFile));
		DataOutputStream output = new DataOutputStream(new GZIPOutputStream(bufferedOutput));
		
		output.writeInt(0x5052464c);
		output.write(CURRENT_PROFILES_VERSION);
		
		output.write(profiles.size());
		for(Profile profile : profiles)
		{
			assert profile.name.length() == 16;
			for(char c : profile.name.toCharArray())
			{
				output.write(c);
			}
			output.write(profile.currentLevel);
			assert profile.starsObtained.size() == profile.currentLevel;
			for(int starsObtainedInLevel : profile.starsObtained)
			{
				output.write(starsObtainedInLevel);
			}
			output.writeChar(profile.totalStarsObtained);
		}
		bufferedOutput.flush();
		output.close();
	}
	
	public static Profile createProfile(String name)
	{
		Profile profile = new Profile(name, 0, new int[0], 0);
		profiles.add(profile);
		return profile;
	}
	
	public static int getProfileCount()
	{
		return profiles.size();
	}
	
	public static List<String> getProfileNameList()
	{
		List<String> names = new ArrayList<String>();
		for(Profile profile : profiles)
		{
			names.add(profile.getName());
		}
		return names;
	}
	
	public static Profile getProfileByName(String name)
	{
		for(Profile profile : profiles)
		{
			if(profile.getName().equals(name)){ return profile; }
		}
		return null;
	}
	
}
