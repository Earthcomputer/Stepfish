package net.earthcomputer.stepfish.util;

public class AlwaysTruePredicate<T> implements Predicate<T>
{
	
	@Override
	public boolean apply(T input)
	{
		return true;
	}
	
}
