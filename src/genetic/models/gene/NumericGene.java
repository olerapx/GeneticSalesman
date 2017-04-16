package genetic.models.gene;

/**
 * Represents a gene that can hold an integer value.
 */
public class NumericGene extends Gene 
{
	private Integer value;
	
	private int maxValue;
	private int minValue;
	
	public NumericGene(Integer value, int minValue, int maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;	
		
		setValue (value);
	}
	
	public void setValue(Integer value)
	{
		if (value < minValue || value > maxValue)
			throw new IllegalArgumentException("Gene value is out of range.");
		
		this.value = value;
	}
	
	public Integer getValue() { return value; }
		
	public boolean equals(Object o)
	{
		if (o instanceof NumericGene)		
			return value.equals(((NumericGene)o).getValue());
		else return false;
	}	
	
	public double getHammingDistance(Gene other)
	{
		if (!(other instanceof NumericGene))
			throw new IllegalArgumentException ("Cannot calculate hamming distance with the specified gene.");
		
		return Math.abs(value - ((NumericGene) other).getValue());		
	}
	
	public String toString()
	{
		return value.toString();
	}
}
