package genetic.operators.cycle;

import java.util.ArrayList;
import java.util.List;

import genetic.models.gene.NumericGene;

public class Cycle 
{
	private List<CycleNode> cycle;
	
	public Cycle()
	{
		cycle = new ArrayList<>();
	}
	
	public void addNode(NumericGene source, NumericGene destination)
	{
		cycle.add(new CycleNode(source, destination));
	}
	
	public boolean isClosed()
	{
		if (cycle.size() == 0) return false;
		
		CycleNode first = cycle.get(0);
		CycleNode last = cycle.get(cycle.size()-1);
		
		return (last.getDestination().equals(first.getSource()));
	}	
	
	public int getLength() { return cycle.size(); }
	
	public CycleNode get(int index)
	{
		return cycle.get(index);
	}
}
