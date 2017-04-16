package genetic.models.population;

import java.util.ArrayList;
import java.util.List;

import genetic.models.chromosome.Chromosome;

/**
 * Represents a set of solutions of an optimization problem.
 */
public class Population
{
	private List<Chromosome> chromosomes;
	
	public Population ()
	{
		chromosomes = new ArrayList<>();
	}
	
	public Population(List<Chromosome> chromosomes)
	{
		this.chromosomes = chromosomes;
	}
	
	public List<Chromosome> getChromosomes() { return this.chromosomes; }
	
	public int getLength() { return chromosomes.size(); }
	
	public String toString()
	{
		String res = "";
		for (Chromosome c: chromosomes)
			res += c.toString() + "\n";
		
		return res;		
	}
}
