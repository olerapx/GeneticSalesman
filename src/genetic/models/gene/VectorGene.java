package genetic.models.gene;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a gene that can hold a vector of integer values.
 */
public class VectorGene extends Gene 
{
	private List<Integer> value;
	
	private int maxValue;
	private int minValue;
	
	public VectorGene(int minValue, int maxValue)
	{		
		this.minValue = minValue;
		this.maxValue = maxValue;		
		
		value = new ArrayList<>();
	}
	
	public VectorGene(List<Integer> value, int minValue, int maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;	
		
		setValue(value);
	}
	
	public void setValue(List<Integer> value)
	{
		for (Integer i: value)
			if (i < minValue || i > maxValue)
				throw new IllegalArgumentException ("Vector gene value is out of range.");
		
		this.value = value;
	}
	
	public List<Integer> getValue() { return value; }
	
	public boolean equals(Object o)
	{
		if (o instanceof VectorGene)		
			return value.equals(((VectorGene)o).getValue());
		else return false;
	}	
	
	public double getHammingDistance(Gene other)
	{
		if (!(other instanceof VectorGene))
			throw new IllegalArgumentException ("Cannot calculate hamming distance with the specified gene.");
		
		VectorGene o = (VectorGene) other;
		
		if (o.getValue().size() != value.size())
			throw new RuntimeException ("Vector gene sizes are not equal.");
		
		double res = 0.0;
		for (int i=0; i<value.size(); i++)
			res += Math.pow(o.getValue().get(i) - value.get(i), 2);
		
		res = Math.sqrt(res);
		
		return res;	
	}
	
	public String toString()
	{
		String res = "<";
		for (Integer i: value)
			res += i.toString() + " ";
		
		res = res.substring(0, res.length()-1) + ">";		
		return res;
	}
}
