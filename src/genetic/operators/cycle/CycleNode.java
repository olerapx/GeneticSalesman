package genetic.operators.cycle;

import genetic.models.gene.NumericGene;

public class CycleNode 
{
	private NumericGene source, destination;
	
	public CycleNode(NumericGene source, NumericGene destination)
	{
		this.source = source;
		this.destination = destination;
	}
	
	public NumericGene getSource() { return source; }
	public NumericGene getDestination() { return destination; }
}
