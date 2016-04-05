package net.earthcomputer.githubgame.util;

import net.earthcomputer.githubgame.object.GameObject;

public interface GameObjectCreator<T extends GameObject>
{
	
	T create(double x, double y);
	
}
