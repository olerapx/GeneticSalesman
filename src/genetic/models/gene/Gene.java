package genetic.models.gene;

/**
 * Base class for all genes.
 */
public abstract class Gene 
{
	public abstract Object getValue();
	public abstract boolean equals(Object o);
	public abstract double getHammingDistance(Gene other);
}
