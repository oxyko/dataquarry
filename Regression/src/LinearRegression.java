/**
 * @author      Oksana Korol 
 * @version     0.8                        
 * @since       2012-11-26   
 * 
 * Linear regression analysis example.
 */


public class LinearRegression {

	
	public static void main(String[] args) {
		int[] days = new int[]{1,2,3,4,5};
		int[] numberOfVisits = new int[]{134,153,148,150,154};
		
		double[] res = getLinearRegression(days, numberOfVisits);
		
		if (res != null){
			
			System.out.println("\nLinear regression equation: y = " + res[0] + "*x + " + res[1]);
			System.out.println("\nCoefficient of determination for the above line: " + res[2]);
			System.out.println("\n  (Coefficient of determination is between 0 and 1 and represent \n" +
					"  the percent of data that can be explained by the linear relationship, \n" +
					"  represented by the regression line.)");
		}
	}

	/* Finds an equation of a line that fits through the given points.
	 * Equation is of the form: y = ax + b;
	 *@param xCoordinates - array of x coordinates of the given points
	 *@param yCoordinates - corresponding array of y coordinates of given points
	 *@return an array of 3 elements [a, b, coef] in this order (i.e. slope first,
	 *			y intersection second, coefficient of determination third) 
	 *			OR null if error in the input
	 */
	public static double[] getLinearRegression(int[] xCoordinates, int[] yCoordinates){
		
		if (xCoordinates.length != yCoordinates.length) return null;
		
		//Contains 2 elements in this order: 
		//  1) slope of the line (a)
		//  2) intersection of the line with y axis (b)
		//  3) coefficient of determination (measures how much of the data 
		//		can be explained by regression line)
		double[] result = new double[3];
		
		//Intermediate variables for reuse
		int sum_x = 0, sum_y = 0, sum_xy = 0, sum_x2 = 0, sum_y2 =0;
		int n = xCoordinates.length;
		
		for (int i = 0; i < n; i++) {
			sum_x  += xCoordinates[i];
			sum_y  += yCoordinates[i];
			sum_xy += xCoordinates[i]*yCoordinates[i];
			sum_x2 += xCoordinates[i]*xCoordinates[i];
			sum_y2 += yCoordinates[i]*yCoordinates[i];
		}
		
		double correlationCoef = (n*sum_xy - sum_x*sum_y) / 
				Math.sqrt((double)(n*sum_x2 - sum_x*sum_x)*(n*sum_y2 - sum_y*sum_y));
		
		double slope = (n*sum_xy - sum_x*sum_y) /
				(double)(n*sum_x2 - sum_x*sum_x);
		
		double lift = sum_y /(double) n - slope * (sum_x /(double) n);
		
		result[0] = slope;
		result[1] = lift;
		result[2] = correlationCoef * correlationCoef;
		
		return result;
	}
}
