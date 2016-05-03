package net.earthcomputer.galacticgame.util;

public class InstanceOfPredicate<T> implements Predicate<T>
{
	
	private final Class<? extends T> clazz;
	
	public InstanceOfPredicate(Class<? extends T> clazz)
	{
		this.clazz = clazz;
	}
	
	@Override
	public boolean apply(T input)
	{
		return clazz.isInstance(input);
	}
	
}
