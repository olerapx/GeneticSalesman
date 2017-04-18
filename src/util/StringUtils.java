package util;

public class StringUtils 
{
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");
	}
}
