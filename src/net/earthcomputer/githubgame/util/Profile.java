package net.earthcomputer.githubgame.util;

import java.util.ArrayList;

public final class Profile
{
	final String name;
	int currentLevel;
	ArrayList<Integer> starsObtained;
	int totalStarsObtained;
	
	Profile(String name, int currentLevel, int[] starsObtained, int totalStarsObtained)
	{
		this.name = name;
		this.currentLevel = currentLevel;
		this.starsObtained = new ArrayList<Integer>(starsObtained.length);
		for(int i = 0; i < starsObtained.length; i++)
		{
			this.starsObtained.add(starsObtained[i]);
		}
		this.totalStarsObtained = totalStarsObtained;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getCurrentLevel()
	{
		return currentLevel;
	}
	
	public void completeLevel()
	{
		currentLevel++;
		starsObtained.add(0);
	}
	
	public boolean isStarObtained(int levelIndex, int starIndex)
	{
		int mask = 1 << starIndex;
		return (starsObtained.get(levelIndex) & mask) != 0;
	}
	
	public void obtainStar(int levelIndex, int starIndex)
	{
		int mask = 1 << starIndex;
		starsObtained.set(levelIndex, starsObtained.get(levelIndex) | mask);
		totalStarsObtained++;
	}
	
	public int getTotalStarsObtained()
	{
		return totalStarsObtained;
	}
}
