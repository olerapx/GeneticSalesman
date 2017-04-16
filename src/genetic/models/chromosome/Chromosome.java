package genetic.models.chromosome;

import java.util.ArrayList;
import java.util.List;

import genetic.models.gene.Gene;

/**
 * Represents a solution of an optimization problem.
 */
public class Chromosome
{
	private List<Gene> genes;
	
	public Chromosome()
	{
		genes = new ArrayList<Gene>();
	}
	
	public Chromosome(List<Gene> genes)
	{
		this.genes = genes;
	}
		
	public List<Gene> getGenes() { return genes; }
	
	public int getLength() { return genes.size(); }
	
	public double getHammingDistance(Chromosome other)
	{		
		double res = 0.0;
		for (int i=0; i< genes.size(); i++)
			res += genes.get(i).getHammingDistance(other.getGenes().get(i));
		
		return res;
	}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof Chromosome))
			return false;
		
		return genes.equals(((Chromosome)o).getGenes());	
	}
	
	public String toString()
	{
		String res = "[";
		for (Gene g: genes)
			res += g.toString() + " ";
		
		res = res.substring(0, res.length()-1) + "]";
		return res;		
	}
}
