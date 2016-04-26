package net.earthcomputer.githubgame.object;

import java.util.HashMap;
import java.util.Map;

import net.earthcomputer.githubgame.util.GameObjectCreator;

public class ObjectTypes
{
	
	private static final Map<Integer, GameObjectCreator<?>> creatorsById = new HashMap<Integer, GameObjectCreator<?>>();
	
	private ObjectTypes()
	{
	}
	
	public static GameObjectCreator<?> getCreatorById(int id)
	{
		return creatorsById.get(id);
	}
	
	public static void registerTypes()
	{
		registerType(0, PlayerObject.class);
		registerType(1, WallObject.class);
		registerType(2, WallObject.class, EnumElement.EARTH);
		registerType(3, WallObject.class, EnumElement.WATER);
		registerType(4, WallObject.class, EnumElement.AIR);
		registerType(5, WallObject.class, EnumElement.FIRE);
		registerType(6, StarObject.class, 0);
		registerType(7, ElementSwitcherObject.class);
		registerType(8, FlyingCrossObject.class);
		registerType(9, SpikeObject.class, EnumElement.EARTH, true);
		registerType(10, ExitObject.class);
		registerType(11, StarObject.class, 1);
		registerType(12, StarObject.class, 2);
		registerType(13, SpikeObject.class, EnumElement.EARTH, false);
		registerType(14, SpikeObject.class, EnumElement.WATER, false);
		registerType(15, SpikeObject.class, EnumElement.AIR, false);
		registerType(16, SpikeObject.class, EnumElement.FIRE, false);
	}
	
	private static <T extends GameObject> void registerType(int id, final Class<T> clazz, Object... extraCtorArgs)
	{
		if(extraCtorArgs == null) extraCtorArgs = new Object[0];
		final Class<?>[] ctorArgsTypes = new Class<?>[extraCtorArgs.length + 2];
		ctorArgsTypes[0] = double.class;
		ctorArgsTypes[1] = double.class;
		for(int i = 0; i < extraCtorArgs.length; i++)
		{
			ctorArgsTypes[i + 2] = extraCtorArgs[i].getClass();
		}
		final Object[] workingExtraCtorArgs = extraCtorArgs;
		creatorsById.put(id, new GameObjectCreator<T>() {
			@Override
			public T create(double x, double y)
			{
				Object[] args = new Object[ctorArgsTypes.length];
				args[0] = x;
				args[1] = y;
				System.arraycopy(workingExtraCtorArgs, 0, args, 2, workingExtraCtorArgs.length);
				try
				{
					return clazz.getConstructor(ctorArgsTypes).newInstance(args);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
}
