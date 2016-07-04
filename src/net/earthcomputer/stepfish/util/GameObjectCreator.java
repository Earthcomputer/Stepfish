package net.earthcomputer.stepfish.util;

import net.earthcomputer.stepfish.object.GameObject;

public interface GameObjectCreator<T extends GameObject>
{
	
	T create(double x, double y);
	
}
