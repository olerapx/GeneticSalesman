package genetic.models.chromosome;

import java.util.Comparator;

import genetic.models.function.ObjectiveFunction;

public class FunctionComparator implements Comparator <Chromosome>
{
	private ObjectiveFunction function;
	
	public FunctionComparator (ObjectiveFunction function)
	{
		this.function = function;
	}
	
	public int compare(Chromosome c1, Chromosome c2)
	{		
		return (Double.compare(function.calculate(c1), function.calculate(c2)));
	}
}
