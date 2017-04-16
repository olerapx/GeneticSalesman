package genetic.models.gene;

/**
 * Represents a gene that can hold 0 or 1.
 */
public class BinaryGene extends Gene
{
	private Integer value;
	
	public BinaryGene(Integer value)
	{
		setValue (value);
	}
		
	public void setValue(Integer value)
	{
		if (value < 0 || value > 1)
			throw new IllegalArgumentException ("Invalid binary value.");
		
		this.value = value;
	}
	
	public Integer getValue() { return value; }
		
	public boolean equals(Object o)
	{
		if (o instanceof BinaryGene)		
			return value.equals(((BinaryGene)o).getValue());
		else return false;
	}
	
	public double getHammingDistance(Gene other)
	{
		if (!(other instanceof BinaryGene))
			throw new IllegalArgumentException ("Cannot calculate hamming distance with the specified gene.");
		
		return Math.abs(value - ((BinaryGene) other).getValue());		
	}
		
	public String toString()
	{
		return value.toString();
	}
}
