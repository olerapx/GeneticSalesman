package genetic.models.function;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import genetic.models.chromosome.Chromosome;
import genetic.models.gene.Gene;

/**
 * Represents the objective function expressing an optimization problem.
 */
public class ObjectiveFunction 
{
	private String function;	
	private Bindings bindings;
	
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
	
	public ObjectiveFunction(String function)
	{
		this.function = function;
	}
	
	public String toString()
	{
		return function;
	}
	
	/**
	 * Calculates the value of the function substituting the given chromosome.
	 * @param chromosome
	 * @return
	 */
	public double calculate(Chromosome chromosome)
	{
		bindings = new SimpleBindings();
		
	    int i = 1;
	    for (Gene g: chromosome.getGenes())
	    {
	    	bindings.put("x" + i, g.getValue());
	    	i++;
	    }
	    Double res = 0.0;
	    try 
	    {
			res = (Double)engine.eval(function, bindings);
		} catch (ScriptException e) 
	    {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		
		return res;
	}
}
