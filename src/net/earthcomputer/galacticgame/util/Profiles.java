package net.earthcomputer.galacticgame.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.earthcomputer.galacticgame.GalacticGame;

public class Profiles
{
	
	private static final String[] PROFILE_NAMES = new String[] { "Zing", "Fylop", "Quizzle", "Blob", "Hija", "Dingdong",
			"Dr Doodledoo", "Yamfodeter", "Plaasm", "Eje", "Botemarkcomp", "Mrs Mtwtfss" };
			
	private static final int VERSION_OLD = 0;
	private static final int CURRENT_PROFILES_VERSION = 1;
	private static final List<Profile> profiles = new ArrayList<Profile>();
	
	private Profiles()
	{
	}
	
	private static File getProfilesFile() throws IOException
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
			Files.setAttribute(dir.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
		}
		
		dir = new File(dir, "games");
		dir = new File(dir, GalacticGame.GAME_NAME);
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
			
		try
		{
			if(input.readInt() != 0x5052464c){ return false; }
			
			int version = input.readUnsignedByte();
			if(version > CURRENT_PROFILES_VERSION)
			{
				return false;
			}
			else if(version == VERSION_OLD)
			{
				return loadProfilesOld(input);
			}
			else
			{
				return loadProfilesNew(input);
			}
		}
		finally
		{
			input.close();
		}
	}
	
	private static boolean loadProfilesOld(DataInputStream input) throws IOException
	{
		int profileCount;
		String[] profileNames;
		int[] levelsTheyreOn;
		int[][] starsObtained;
		int[] totalStarsObtained;
		
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
		
		profiles.clear();
		for(int i = 0; i < profileCount; i++)
		{
			profiles.add(new Profile(-i, profileNames[i], levelsTheyreOn[i], starsObtained[i], totalStarsObtained[i]));
		}
		return true;
	}
	
	private static boolean loadProfilesNew(DataInputStream input) throws IOException
	{
		int profileCount;
		int[] profileNameIds;
		String[] profileNames;
		int[] levelsTheyreOn;
		int[][] starsObtained;
		int[] totalStarsObtained;
		
		profileCount = input.readUnsignedByte();
		
		profileNameIds = new int[profileCount];
		profileNames = new String[profileCount];
		levelsTheyreOn = new int[profileCount];
		starsObtained = new int[profileCount][];
		totalStarsObtained = new int[profileCount];
		
		for(int profile = 0; profile < profileCount; profile++)
		{
			profileNameIds[profile] = input.readShort();
			if(profileNameIds[profile] < 0)
			{
				char[] nameChars = new char[16];
				for(int i = 0; i < nameChars.length; i++)
				{
					nameChars[i] = (char) input.readUnsignedByte();
				}
				profileNames[profile] = new String(nameChars);
			}
			else if(profileNameIds[profile] >= PROFILE_NAMES.length)
			{
				return false;
			}
			else
			{
				profileNames[profile] = PROFILE_NAMES[profileNameIds[profile]];
			}
			
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
		
		profiles.clear();
		for(int i = 0; i < profileCount; i++)
		{
			profiles.add(new Profile(profileNameIds[i], profileNames[i], levelsTheyreOn[i], starsObtained[i],
				totalStarsObtained[i]));
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
			output.writeShort(profile.nameId);
			if(profile.nameId < 0)
			{
				assert profile.name.length() == 16;
				for(char c : profile.name.toCharArray())
				{
					output.write(c);
				}
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
		int id = -1;
		for(int i = 0; i < PROFILE_NAMES.length; i++)
		{
			if(name.equals(PROFILE_NAMES[i]))
			{
				id = i;
				break;
			}
		}
		if(id == -1){ throw new IllegalArgumentException("The given name does not match any possible names"); }
		return createProfile(id);
	}
	
	public static Profile createProfile(int nameId)
	{
		Profile profile = new Profile(nameId, PROFILE_NAMES[nameId], 0, new int[0], 0);
		profiles.add(profile);
		return profile;
	}
	
	public static Profile deleteProfile(String name)
	{
		Profile removed = getProfileByName(name);
		profiles.remove(removed);
		return removed;
	}
	
	public static int getProfileCount()
	{
		return profiles.size();
	}
	
	public static List<String> getAvailableProfileNameList()
	{
		List<String> taken = getTakenProfileNameList();
		List<String> available = new ArrayList<String>();
		for(String name : PROFILE_NAMES)
		{
			if(!taken.contains(name))
			{
				available.add(name);
			}
		}
		return available;
	}
	
	public static List<String> getTakenProfileNameList()
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
