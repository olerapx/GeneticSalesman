package genetic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Contains functions related to random number generation.
 */
public class NumberGenerator 
{
	/**
	 * Returns an array of random unique integers between min and max inclusively.
	 * @param min Min value.
	 * @param max Max value.
	 * @param count Count of values.
	 * @return Generated array.
	 */
	public static int[] generateUniqueRandomNumbers(int min, int max, int count)
	{
		if (max - min < count)
			throw new RuntimeException("Cannot generate the given count of numbers: range of the values is too small.");
		
	    ArrayList<Integer> list = new ArrayList<Integer>();
	    for (int i=min; i<max; i++) 
	    {
	    	list.add(new Integer(i));
	    }
	    
	    int[] res = new int[count];
	    
		Collections.shuffle(list);
		for (int i=0; i< count; i++)
			res[i] = list.get(i);
		
		return res;
	}
	
	/**
	 * Returns a random int number between between min and max inclusively.
	 * @param rand Initialized random number generator.
	 * @param min Min value.
	 * @param max Max value.
	 * @return Generated number.
	 */
	public static int getRandomNumberInRange(Random rand, int min, int max)
	{
		int range = max - min + 1;
		int res = rand.nextInt(range) + min;
		
		return res;
	}

	/**
	 * Returns true with the given chance. Otherwise returns false.
	 * @param rand Initialized random number generator.
	 * @param chance Chance between 0.0 to 1.0.
	 * @return Roll result.
	 */
	public static boolean rollWithChance(Random rand, double chance)
	{
		if (chance < 0.0 || chance > 1.0)
			throw new IllegalArgumentException("Invalid chance value.");
		
		if (rand.nextDouble() <= chance)
			return true;
		
		return false;
	}
}
