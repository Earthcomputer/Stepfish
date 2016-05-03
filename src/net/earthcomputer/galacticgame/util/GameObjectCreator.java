package net.earthcomputer.galacticgame.util;

import net.earthcomputer.galacticgame.object.GameObject;

public interface GameObjectCreator<T extends GameObject>
{
	
	T create(double x, double y);
	
}
