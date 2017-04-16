package genetic.operators;

import genetic.models.chromosome.Chromosome;
import genetic.models.chromosome.ChromosomeType;
import genetic.models.population.Population;

public class Validator 
{
	public static void validateEqualChromosomeLength(Chromosome c1, Chromosome c2)
	{
		if (c1.getLength() != c2.getLength())
			throw new IllegalArgumentException("The chromosomes have different length.");
	}
	
	public static void validateCountInPopulation(Population p, int count)
	{
		if (count < 0 || count > p.getLength())
			throw new IllegalArgumentException("The population is too small.");
	}
	
	public static void validateCountInChromosome(Chromosome c, int count)
	{
		if (count < 0 || count > c.getLength())
			throw new IllegalArgumentException("The chromosome is too small.");
	}
	
	public static void validateVectorLengthIsSpecified(ChromosomeType type, Integer vectorLength)
	{
		if (type == ChromosomeType.Vector && vectorLength == null)
			throw new IllegalArgumentException("Vector size is not specified.");
	}
}
