package genetic.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import genetic.models.chromosome.Chromosome;
import genetic.models.chromosome.FunctionComparator;
import genetic.models.function.ObjectiveFunction;
import genetic.models.population.Population;

public class SelectionOperator
{
	private ObjectiveFunction function;
	
	public SelectionOperator (ObjectiveFunction function)
	{
		this.function = function;
	}
	
	public Population rouletteSelection(Population p, int count)
	{
		Validator.validateCountInPopulation(p, count);
		
		double total = 0.0;		
				
		for (Chromosome c: p.getChromosomes())
			total += function.calculate(c);
		
		List<Chromosome> res = new ArrayList<>();
		
		for (int j = 0; j<count; j++)
		{		
			double random = Math.random() * total;
			
			for (int i = 0; i< p.getLength(); i++)
			{
				random -= function.calculate(p.getChromosomes().get(i));
				if (random <= 0.00)
				{
					total -= function.calculate(p.getChromosomes().get(i));
					res.add(p.getChromosomes().get(i));
					p.getChromosomes().remove(i);
					
					break;
				}					
			}
		}
		
		return new Population(res);
	}
	
	public Population eliteSelection(Population p, int count)
	{
		Validator.validateCountInPopulation(p, count);
		
		List<Chromosome> chromosomes = new ArrayList<>(p.getChromosomes());
		
		chromosomes.sort(Collections.reverseOrder(new FunctionComparator(function)));

		return new Population(chromosomes.subList(0, count));
	}
	
	public Population tournamentSelection(Population p, int count)
	{
		Validator.validateCountInPopulation(p, count);
		
		Random rand = new Random();
		List<Chromosome> chromosomes = new ArrayList<>(p.getChromosomes());
		int randomCount = 0;
		
		do 
		{
			randomCount = rand.nextInt(chromosomes.size());
			if (randomCount == 0)
				randomCount = chromosomes.size()/2;			
		}
		while (count > randomCount);
		
		List<Chromosome> res = new ArrayList<>();
		
		for (int i=0; i<randomCount; i++)
		{
			int index = rand.nextInt(chromosomes.size());
			
			res.add(chromosomes.get(index));
			chromosomes.remove(index);
		}

		chromosomes.sort(Collections.reverseOrder(new FunctionComparator(function)));
		return new Population (res.subList(0, count));
	}
}
