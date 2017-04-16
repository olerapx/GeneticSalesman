package genetic.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genetic.models.chromosome.Chromosome;
import genetic.models.chromosome.FunctionComparator;
import genetic.models.function.ObjectiveFunction;
import genetic.models.population.Population;

public class ReductionOperator
{
	private ObjectiveFunction function;
	
	public ReductionOperator (ObjectiveFunction function)
	{
		this.function = function;
	}
	
	public Population randomReduction(Population p, int count)
	{
		Validator.validateCountInPopulation(p, count);
	
		List<Chromosome> chromosomes = new ArrayList<>(p.getChromosomes());
		Random rand = new Random();
		
		for (int i=0; i<count; i++)
			chromosomes.remove(rand.nextInt(chromosomes.size()));
		
		return new Population (chromosomes);
	}
	
	public Population outstandingReduction(Population p, int count)
	{
		Validator.validateCountInPopulation(p, count);
		
		List<Chromosome> chromosomes = new ArrayList<>(p.getChromosomes());
		
		chromosomes.sort(new FunctionComparator(function));
		
		for (int i=0; i<count; i++)
		{
			if ((i % 2) == 0) chromosomes.remove(0);
			else chromosomes.remove(chromosomes.size()-1);
		}
		
		return new Population (chromosomes);
	}

	public Population hammingReduction(Population p, int count)
	{
		Validator.validateCountInPopulation(p, count);
		
		List<Chromosome> chromosomes = new ArrayList<>(p.getChromosomes());
		
		Random rand = new Random();
		
		for (int i=0; i< count; i++)
		{
			Chromosome c = chromosomes.get(rand.nextInt(chromosomes.size()));
			
			int removeIndex = 0;
			double minDistance = Double.MAX_VALUE;
			
			for (int j = 0;j < chromosomes.size(); j++)
			{
				if (j != i)
				{
					double dist = c.getHammingDistance(chromosomes.get(j));
					if (dist < minDistance)
					{
						minDistance = dist;
						removeIndex = j;
					}
				}
			}
			chromosomes.remove(removeIndex);
		}
		
		return new Population (chromosomes);
	}
}
