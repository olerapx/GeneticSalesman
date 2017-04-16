package genetic.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genetic.models.chromosome.Chromosome;
import genetic.models.chromosome.ChromosomeType;
import genetic.models.gene.BinaryGene;
import genetic.models.gene.Gene;
import genetic.models.gene.NumericGene;
import genetic.models.gene.VectorGene;
import genetic.models.population.Population;
import genetic.util.NumberGenerator;

public class PopulationGenerator
{	
	public static Population blanketMethod(ChromosomeType type, int chromosomeLength, int min, int max, Integer vectorLength)
	{
		Validator.validateVectorLengthIsSpecified(type, vectorLength);
		
		int totalLength = calculateTotalLength(type, chromosomeLength, vectorLength);
		if (type == ChromosomeType.Binary)
		{
			min = 0;
			max = 1;
		}
		
		List<Chromosome> chromosomes = new ArrayList<>();
		
		int[] numbers = new int[totalLength];
		for (int i=0; i< totalLength; i++)
			numbers[i] = min;
		
		int range = max - min + 1;
		
		int totalCombinations = (int)Math.pow(range, totalLength);
		
		for (int i=1; i<=totalCombinations; i++)
		{
			Chromosome c = buildChromosome(type, numbers, min, max, vectorLength);			
			chromosomes.add(c);
			
			for (int j=0; j<numbers.length; j++)
			{
				if ((i % Math.pow(range, numbers.length-j-1)) == 0)
				{
					numbers[j] ++;
					if (numbers[j] > max) numbers[j] = min;
				}
			}
		}
		return new Population (chromosomes);		
	}
	
	private static int calculateTotalLength(ChromosomeType type, int chromosomeLength, Integer vectorLength)
	{
		int totalLength = 0;
		if (type == ChromosomeType.Vector)
			totalLength = chromosomeLength * vectorLength;
		else
			totalLength = chromosomeLength;

		return totalLength;
	}
	
	private static List<Gene> splitNumbersToVectorGenes(int[] numbers, int vectorLength, int min, int max)
	{
		int vectorNumber = numbers.length/vectorLength;
		int curr = 0;
		
		List<Gene> res = new ArrayList<>();
		
		for (int i=0; i < vectorNumber; i++)
		{
			List<Integer> vector = new ArrayList<>();
			for (int j = 0; j< vectorLength; j++)
			{
				vector.add(numbers[curr]);
				curr ++;
			}
			res.add(new VectorGene(vector, min, max));
		}		
		
		return res;
	}
	
	private static Chromosome buildChromosome(ChromosomeType type, int[] numbers, int min, int max, Integer vectorLength)
	{			
		if (type == ChromosomeType.Vector && vectorLength == null)
			throw new IllegalArgumentException ("Vector size is not specified.");
		
		Chromosome c;
		List<Gene> genes = new ArrayList<>();
		
		if (type == ChromosomeType.Binary)
		{			
			for (int i = 0; i < numbers.length; i++)
				genes.add(new BinaryGene (numbers[i]));
			
			c = new Chromosome (genes);
		}
		else if (type == ChromosomeType.Numeric)
		{
			for (int i = 0; i < numbers.length; i++)
				genes.add(new NumericGene(numbers[i], min, max));
			
			c = new Chromosome (genes);						
		}	
		else
		{
			genes = splitNumbersToVectorGenes (numbers, vectorLength, min, max);			
			c = new Chromosome (genes);
		}
		
		return c;
	}
	
	public static Population focusingMethod(ChromosomeType type, int chromosomeNumber, int chromosomeLength, int min, int max, Integer vectorLength)
	{
		Validator.validateVectorLengthIsSpecified(type, vectorLength);
		
		int totalLength = calculateTotalLength(type, chromosomeLength, vectorLength);
		if (type == ChromosomeType.Binary)
		{
			min = 0;
			max = 1;
		}
		
		Random rand = new Random();
		List<Chromosome> chromosomes = new ArrayList<>();
		
		int[] numbers = new int[totalLength];
				
		for (int i=0; i < chromosomeNumber; i++)
		{
			double focusingPoint = max*0.25 + rand.nextDouble()*max*0.5;
			double focusingBias = focusingPoint/(2*chromosomeNumber);
			
			for (int j=0; j<totalLength/2; j++)
				numbers[j] = (int) (focusingPoint + 2*j + focusingBias);
			
			for (int j = totalLength/2; j< totalLength; j++)
				numbers[j] = (int) (focusingPoint - (2*(j - chromosomeNumber/2 + 1) + focusingBias));
			
			Chromosome c = buildChromosome(type, numbers, min, max, vectorLength);
			chromosomes.add(c);	
		}
		
		return new Population (chromosomes);		
	}
	
	public static Population shotgunMethod(ChromosomeType type, int chromosomeNumber, int chromosomeLength, int min, int max, Integer vectorLength, boolean unique)
	{
		Validator.validateVectorLengthIsSpecified(type, vectorLength);
		
		int totalLength = calculateTotalLength(type, chromosomeLength, vectorLength);
		if (type == ChromosomeType.Binary)
		{
			min = 0;
			max = 1;
		}
		
		Random rand = new Random();
		List<Chromosome> chromosomes = new ArrayList<>();
		
		int[] numbers = new int[totalLength];
		
		for (int i=0; i<chromosomeNumber; i++)
		{
			if (unique)
				numbers = NumberGenerator.generateUniqueRandomNumbers(min, max, totalLength);
			else
				for (int j=0; j< totalLength; j++)
					numbers[j] = NumberGenerator.getRandomNumberInRange(rand, min, max);
			
			Chromosome c = buildChromosome(type, numbers, min-10, max+10, vectorLength);
			chromosomes.add(c);	
		}
		
		return new Population (chromosomes);
	}
}