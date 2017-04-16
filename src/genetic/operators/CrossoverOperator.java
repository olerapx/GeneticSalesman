package genetic.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import genetic.models.chromosome.Chromosome;
import genetic.models.gene.BinaryGene;
import genetic.models.gene.Gene;
import genetic.models.gene.NumericGene;
import genetic.models.population.Population;
import genetic.operators.cycle.Cycle;
import genetic.util.NumberGenerator;

public class CrossoverOperator 
{
	public static Population singlePointCrossover(Chromosome parent1, Chromosome parent2)
	{
		Validator.validateEqualChromosomeLength(parent1, parent2);
		Validator.validateCountInChromosome(parent1, 2);
		
		Random rand = new Random();
		
		int point = rand.nextInt(parent1.getLength()-1);
		
		List<Gene> v1 = new ArrayList<>();
		List<Gene> v2 = new ArrayList<>();
		
		v1.addAll(parent1.getGenes().subList(0, point+1));
		v2.addAll(parent2.getGenes().subList(0, point+1));
		
		v1.addAll(parent2.getGenes().subList(point+1, parent2.getLength()));
		v2.addAll(parent1.getGenes().subList(point+1, parent1.getLength()));
		
		return buildPopulation(v1, v2);
	}
		
	public static Population buildPopulation(List<Gene> v1, List<Gene> v2)
	{
		Chromosome c1 = new Chromosome (v1);
		Chromosome c2 = new Chromosome (v2);
				
		return new Population (Arrays.asList(c1, c2));
	}
	
	public static Population multiPointCrossover(Chromosome parent1, Chromosome parent2, int pointsCount)
	{
		Validator.validateEqualChromosomeLength(parent1, parent2);
		Validator.validateCountInChromosome(parent1, pointsCount);
				
		int[] points = NumberGenerator.generateUniqueRandomNumbers(0, parent1.getLength()-1, pointsCount);		
		Arrays.sort(points);
		
		List<Gene> v1 = new ArrayList<>();
		List<Gene> v2 = new ArrayList<>();
		
		int currPointIndex = 0;
		boolean parity = false;
				
		for (int i=0; i<parent1.getLength(); i++)
		{
			if (!parity)
			{
				v1.add(parent1.getGenes().get(i));
				v2.add(parent2.getGenes().get(i));	
			}
			else 
			{
				v2.add(parent1.getGenes().get(i));
				v1.add(parent2.getGenes().get(i));
			}
		
			if (i == points[currPointIndex])
			{
				parity = !parity;
				currPointIndex ++;
				if (currPointIndex >= points.length)
					currPointIndex = 0;
			}
		}
		
		return buildPopulation(v1, v2);
	}

	public static Population orderedCrossover(Chromosome parent1, Chromosome parent2)
	{
		Validator.validateEqualChromosomeLength(parent1, parent2);
		
		Random rand = new Random();
		
		int point = rand.nextInt(parent1.getLength()-1);
		
		List<Gene> v1 = new ArrayList<>();
		List<Gene> v2 = new ArrayList<>();
		
		v1.addAll(parent1.getGenes().subList(0, point+1));
		v2.addAll(parent2.getGenes().subList(0, point+1));
		
		for (int i=0; i< parent1.getLength(); i++)
		{
			if (!v1.contains(parent2.getGenes().get(i)) && v1.size() < parent1.getLength())
				v1.add(parent2.getGenes().get(i));
			
			if (!v2.contains(parent1.getGenes().get(i)) && v2.size() < parent1.getLength())
				v2.add(parent1.getGenes().get(i));	
			
			if (v1.size() == parent1.getLength() && v2.size() == parent2.getLength())
				break;
		}
		
		return buildPopulation(v1, v2);
	}
	
	public static Population partiallyRelevantCrossover(Chromosome parent1, Chromosome parent2)
	{
		Validator.validateEqualChromosomeLength(parent1, parent2);
		
		Random rand = new Random();
		
		int point = rand.nextInt(parent1.getLength()-1);
				
		List<Gene> v1 = new ArrayList<>(Collections.nCopies(parent1.getLength(), null));
		List<Gene> v2 = new ArrayList<>(Collections.nCopies(parent2.getLength(), null));
		
		for (int i=point+1; i<parent1.getLength(); i++)
		{
			v1.set(i, parent2.getGenes().get(i));
			v2.set(i, parent1.getGenes().get(i));
		}
		for (int i=0; i<=point; i++)
		{			
			v1.set(i, getNonRepeatingGene(parent1.getGenes(), point, v1, i));
			v2.set(i, getNonRepeatingGene(parent2.getGenes(), point, v2, i));	
		}
				
		return buildPopulation(v1, v2);
	}

	private static Gene getNonRepeatingGene(List<Gene> parent, int point, List<Gene> children, int i)
	{
		if (!children.contains(parent.get(i)))
			return parent.get(i);
		
		for (int j=point+1; j<parent.size(); j++)
		{
			if (!children.contains(parent.get(j)))
				return parent.get(j);
		}
		
		throw new NoSuchElementException("A non-repeating gene cannot be found.");
	}
	
	public static Population cyclicCrossover(Chromosome parent1, Chromosome parent2)
	{
		Validator.validateEqualChromosomeLength(parent1, parent2);
		
		if (!(parent1.getGenes().get(0) instanceof NumericGene))
			throw new IllegalArgumentException ("The cyclic crossover can be performed only on numeric chromosomes.");
		
		List<Gene> v1, v2;
		
		try
		{
			v1 = performCyclicCrossoverOnChildren(parent1, parent2);
			v2 = performCyclicCrossoverOnChildren(parent2, parent1);
		}
		catch (Throwable t)
		{
			throw new RuntimeException("Cannot perform cyclic crossover. Set differing parameters and try again.");
		}
		
		return buildPopulation(v1, v2);
	}
	
	private static List<Gene> performCyclicCrossoverOnChildren(Chromosome parent1, Chromosome parent2)
	{	
		List<Gene> p1 = new ArrayList<>(parent1.getGenes());
		List<Gene> p2 = new ArrayList<>(parent2.getGenes());
		
		List<Gene> children = new ArrayList<>(Collections.nCopies(parent1.getLength(), null));
		
		while (true)
		{
			Cycle c = fillCycle(p1, p2, 0);
						
			for (int i=0; i<c.getLength(); i++)
			{
				int index = ((NumericGene)c.get(i).getSource()).getValue();
				Gene gene = c.get(i).getDestination();

				children.set(index, gene);
			}
			if (p1.size() == 0)	return children;			
		}
	}
	
	private static Cycle fillCycle(List<Gene> first, List<Gene> second, int startIndex)
	{
		Cycle c = new Cycle();

		int index = startIndex;
		
		while (!c.isClosed())
		{
			NumericGene source = (NumericGene)first.get(index);
			NumericGene destination = (NumericGene)second.get(index);	
			
			c.addNode(source, destination);

			first.remove(index);
			second.remove(index);
			
			index = first.indexOf(destination);
		}
		return c;
	}
	
	public static Population universalCrossover(Chromosome parent1, Chromosome parent2)
	{
		Validator.validateEqualChromosomeLength(parent1, parent2);
		
		if (!(parent1.getGenes().get(0) instanceof BinaryGene))
			throw new IllegalArgumentException ("The cyclic crossover can be performed only on binary chromosomes.");
		
		List<Gene> v1 = new ArrayList<>();
		List<Gene> v2 = new ArrayList<>();
		
		Random rand = new Random();
		
		int[] mask = new int[parent1.getLength()];
		for (int i=0; i<mask.length; i++)
			mask[i] = rand.nextInt(1);
		
		for (int i=0; i<parent1.getLength(); i++)
		{
			v1.add(addGeneToMask((BinaryGene)parent1.getGenes().get(i), mask[i]));
			v2.add(addGeneToMask((BinaryGene)parent2.getGenes().get(i), mask[i]));
		}
		
		return buildPopulation(v1, v2);
	}
	
	private static BinaryGene addGeneToMask(BinaryGene gene, int maskBit)
	{
		int value = gene.getValue();
		value = value + maskBit;
		
		if (value > 1) value = 0;
		
		BinaryGene res = new BinaryGene(value);		
		return res;
	}
}
