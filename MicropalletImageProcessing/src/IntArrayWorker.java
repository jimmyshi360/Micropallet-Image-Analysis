/**
 * @info
 *<BR> Name:          Yihong Sun
 *<BR> Date:          7-01-2017
 *<BR> Lab:           Nelson Lab
 *<BR> Assignment:    Image Analysis
 *<BR> Description:   A class that works out the picture array
 *<BR> Cite Sources:  AP Picture Lab
 */


public class IntArrayWorker
{
	/** two dimensional matrix */
	private int[][] matrix = null;
	
	/** set the matrix to the passed one
	* @param theMatrix the one to use
	*/
	public void setMatrix(int[][] theMatrix)
	{
		matrix = theMatrix;
	}
	
	/**
	* Method to return the total 
	* @return the total of the values in the array
	*/
	public int getTotal()
	{
		int total = 0;
		for (int row = 0; row < matrix.length; row++)
		{
			for (int col = 0; col < matrix[0].length; col++)
			{
				total = total + matrix[row][col];
			}
		}
		return total;
	}
	
	/**
	* Method to return the total using a nested for-each loop
	* @return the total of the values in the array
	*/
	public int getTotalNested()
	{
		int total = 0;
		for (int[] rowArray : matrix)
		{
			for (int item : rowArray)
			{
				total = total + item;
			}
		}
		return total;
	}
	
	/**
	* Method to fill with an increasing count
	*/
	public void fillCount()
	{
		int numCols = matrix[0].length;
		int count = 1;
		for (int row = 0; row < matrix.length; row++)
		{
			for (int col = 0; col < numCols; col++)
			{
				matrix[row][col] = count;
				count++;
			}
		}
	}
	
	/**
	* print the values in the array in rows and columns
	*/
	public void print()
	{
		for (int row = 0; row < matrix.length; row++)
		{
			for (int col = 0; col < matrix[0].length; col++)
			{
				System.out.print( matrix[row][col] + " " );
			}
		System.out.println();
		}
	System.out.println();
	}
	
	
	/** 
	* fill the array with a pattern
	*/
	public void fillPattern1()
	{
		for (int row = 0; row < matrix.length; row++)
		{
			for (int col = 0; col < matrix[0].length; col++)
			{
				if (row < col)
					matrix[row][col] = 1;
				else if (row == col)
					matrix[row][col] = 2;
				else
					matrix[row][col] = 3;
			}
		}
	}

	/**
	* <b>summary</b>: find the count of the number of times a passed integer value is found in the 2-d array.
	* @param value -- the integer used to find the number of occurrence 
	* @return the number of times that the input value was present
	*/
  	public int getCount(int value)
	{
		int count = 0;
		for (int[] rowArray : matrix)
		{
			for (int item : rowArray)
			{
				if (item == value)
				{
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	* <b>summary</b>: find the largest value in the 2-d array
	* @return the largest value in the 2-d array
	*/
  	public int getLargest()
	{
		int largest = 0;
		for (int[] rowArray : matrix)
		{
			for (int item : rowArray)
			{
				if (item > largest)
				{
					largest = item;
				}
			}
		}
		return largest;
	}
	
	/**
	* <b>summary</b>: find the total of all values in a column
	* @param column -- the number of the column
	* @return the total in the column
	*/
  	public int getColTotal(int column)
	{
		int total = 0;
		for (int[] rowArray : matrix)
		{
			total += rowArray[column];
		}
		return total;
	}
}