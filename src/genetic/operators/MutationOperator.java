package genetic.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import genetic.models.chromosome.Chromosome;
import genetic.models.gene.Gene;
import genetic.models.population.Population;
import genetic.util.NumberGenerator;

public class MutationOperator 
{
	public static Chromosome pointMutation(Chromosome c, int pointsCount)
	{
		Validator.validateCountInChromosome(c, pointsCount+1);
				
		if (pointsCount == 1)
			return singlePointMutation(c);
		
		List<Gene> v = new ArrayList<>(c.getGenes());
		
		int[] points = NumberGenerator.generateUniqueRandomNumbers(0, c.getLength()-1, pointsCount);
		Arrays.sort(points);
		
		Gene temp = null;
		
		for(int i=0; i<points.length; i++)
		{
			Gene old = temp;
			temp = v.get(points[i]+1);
			
			if (i != 0)
			{
				v.set(points[i]+1, old);
			}
		}		
		v.set(points[0]+1, temp);
		
		return new Chromosome(v);
	}
	
	private static Chromosome singlePointMutation(Chromosome c)
	{
		Random rand = new Random();		
		int point = rand.nextInt(c.getLength()-1);
		
		List<Gene> v = new ArrayList<>(c.getGenes());
		
		Gene temp = v.get(point+1);
		v.set(point+1, v.get(point));
		v.set(point, temp);
		
		return new Chromosome(v);
	}
	
	public static Population exchangeMutation(Chromosome parent1, Chromosome parent2, int pointsCount)
	{
		Validator.validateCountInChromosome(parent1, pointsCount+1);
		Validator.validateCountInChromosome(parent2, pointsCount+1);
		
		List<Gene> v1 = new ArrayList<>();
		List<Gene> v2 = new ArrayList<>();
		
		int[] points1 = NumberGenerator.generateUniqueRandomNumbers(0, parent1.getLength()-1, pointsCount);
		Arrays.sort(points1);
		
		int[] points2 = NumberGenerator.generateUniqueRandomNumbers(0, parent2.getLength()-1, pointsCount);
		Arrays.sort(points2);	
		
		int firstStartIndex, firstEndIndex, secondStartIndex, secondEndIndex;
		boolean parity = false;
		
		for  (int i=0; i<=pointsCount; i++)
		{
			if (i == 0)
				firstStartIndex = secondStartIndex = 0;
			else
			{
				firstStartIndex = points1[i-1] + 1;
				secondStartIndex = points2[i-1] + 1;
			}
			
			if (i == pointsCount)
			{
				firstEndIndex = parent1.getLength();
				secondEndIndex = parent2.getLength();
			}
			else
			{
				firstEndIndex = points1[i]+1;
				secondEndIndex = points2[i]+1;	
			}
			
			if (!parity)
			{
				v1.addAll(parent1.getGenes().subList(firstStartIndex, firstEndIndex));
				v2.addAll(parent2.getGenes().subList(secondStartIndex, secondEndIndex));
			}
			else
			{
				v2.addAll(parent1.getGenes().subList(firstStartIndex, firstEndIndex));
				v1.addAll(parent2.getGenes().subList(secondStartIndex, secondEndIndex));
			}
				
			parity = !parity;
		}
		
		return CrossoverOperator.buildPopulation(v1, v2);
	}
	
	public static Chromosome inversionMutation(Chromosome c, int pointsCount)
	{
		Validator.validateCountInChromosome(c, pointsCount+1);
		
		int[] points = NumberGenerator.generateUniqueRandomNumbers(0, c.getLength()-1, pointsCount);
		Arrays.sort(points);
		
		List<Gene> v = new ArrayList<>();
		
		int startIndex, endIndex;
		boolean parity = false;
		
		for (int i=0; i<=pointsCount; i++)
		{
			if (i==0)
				startIndex = 0;
			else 
				startIndex = points[i-1] + 1;
			
			if (i == pointsCount)
				endIndex = c.getLength();
			else 
				endIndex = points[i] + 1;
			
			List<Gene> sublist = new ArrayList<>(c.getGenes().subList(startIndex, endIndex));
			
			if (parity)
				Collections.reverse(sublist);
			v.addAll(sublist);			
				
			parity = !parity;				
		}
		
		return new Chromosome(v);
	}
	
	public static Population translocationMutation(Chromosome parent1, Chromosome parent2)
	{	
		Validator.validateCountInChromosome(parent1, 2);
		Validator.validateCountInChromosome(parent2, 2);
		
		List<Gene> v1 = new ArrayList<>();
		List<Gene> v2 = new ArrayList<>();
		
		Random rand = new Random();
		
		int point1 = rand.nextInt(parent1.getLength()-1);
		int point2 = rand.nextInt(parent2.getLength()-1);
		
		List<Gene> sublist1 = new ArrayList<>(parent1.getGenes().subList(point1+1, parent1.getLength()));
		List<Gene> sublist2 = new ArrayList<>(parent2.getGenes().subList(point2+1, parent2.getLength()));
		
		Collections.reverse(sublist1);
		Collections.reverse(sublist2);
		
		v1.addAll(parent1.getGenes().subList(0, point1+1));
		v2.addAll(parent2.getGenes().subList(0, point2+1));
		
		v1.addAll(sublist2);
		v2.addAll(sublist1);
		
		return CrossoverOperator.buildPopulation(v1, v2);
	}
	
	public static Chromosome transpositionMutation(Chromosome c)
	{
		Validator.validateCountInChromosome(c, 4);
		
		List<Gene> v = new ArrayList<>();
		
		int[] points = NumberGenerator.generateUniqueRandomNumbers(0, c.getLength()-1, 3);
		Arrays.sort(points);
		
		List<Gene> sublist = new ArrayList<>(c.getGenes().subList(points[1] + 1, points[2] + 1));
		Collections.reverse(sublist);
		
		v.addAll(c.getGenes().subList(0, points[0]+1));
		v.addAll(sublist);
		v.addAll(c.getGenes().subList(points[0]+1, points[1]+1));
		v.addAll(c.getGenes().subList(points[2]+1, c.getLength()));
		
		return new Chromosome (v);
	}
	
	public static Chromosome duplicationMutation(Chromosome c, int index, int count)
	{
		if (index < 0 || index >= c.getLength())
			throw new IllegalArgumentException("The specified index is out of range.");
		
		List<Gene> v = new ArrayList<>(c.getGenes());
		
		Gene gene = v.get(index);
		for (int i=0; i<count-1; i++)
			v.add(index+1, gene);	
				
		return new Chromosome (v);
	}
	
	public static Chromosome deletionMutation(Chromosome c, int index, int count)
	{
		if (index < 0 || index>=c.getLength()-1)
			throw new IllegalArgumentException("The specified index is out of range.");
		
		List<Gene> sublist = new ArrayList<>(c.getGenes().subList(index, index+count));
		
		if (count > sublist.size())
			throw new IllegalArgumentException("The specified index is out of range.");
		
		List<Gene> v = new ArrayList<>(c.getGenes());
		
		v.subList(index, index+count).clear();
		
		return new Chromosome (v);
	}
}
