/**
 * @author      Oksana Korol 
 * @version     0.8                        
 * @since       2013-01-22   
 * 
 * Example of hierarchical agglomerative clustering for character strings of the same size.
 * Implementation using arrays.
 */

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AgglomeratClustering_ArrayImpl {
	
	// make sure that sequences are of the same length
	public String performClustering(ArrayList<String> sequences, ArrayList<String> seqNames){
		double[][] distMatrix = computeDistanceMatrix(sequences);
		findClusters(distMatrix, seqNames);
		return clusterStr;
	}
	
	public double[][] computeDistanceMatrix(ArrayList<String> sequences){
		int seqNum = sequences.size();
		double[][] distMatrix = new double[seqNum][seqNum];
		
		for (int i=0; i < seqNum; i++){
			for (int j = 0; j < i; j++) { // matrix is symmetrical, so compute only half
				distMatrix[i][j] = computeFractionalDifference(sequences.get(i), sequences.get(j));
				distMatrix[i][j] = round(distMatrix[i][j]);
			}
		}
		return distMatrix;
	}
	
	public double computeFractionalDifference(String seq1, String seq2){
		return round((double)getHummingDistance(seq1,seq2) / seq1.length());
	}
	
	public int getHummingDistance(String seq1, String seq2){
		int humDist = 0;
		for(int i = 0; i < seq1.length(); i++){
			if (seq1.charAt(i) != seq2.charAt(i)){
				humDist++;
			}
		}
		return humDist;
	}
	
	double round(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.###");
    	return Double.valueOf(twoDForm.format(d));
	}
	
	
	private static String clusterStr = "";
	public void findClusters(double[][] distMatrix, ArrayList<String> seqNames){
		
		if (seqNames.size() == 1){
			clusterStr = seqNames.get(0);
			return;
		}
		
		//find minimal distance in the distance array
		int minI = Integer.MAX_VALUE;
		int minJ = Integer.MAX_VALUE;
		double minValue = Double.MAX_VALUE;
		
		for (int i=0; i < distMatrix.length; i++){
			for (int j = 0; j < i; j++) { // matrix is symmetrical, so go through only half
				if (distMatrix[i][j] < minValue){
					minValue = distMatrix[i][j];
					minI = i; minJ = j;
				}
			}
		}
		
		
		ArrayList<String> newSeqNames = new ArrayList<String>();
		newSeqNames = (ArrayList<String>)seqNames.clone();
		newSeqNames.remove(minI);
		newSeqNames.remove(minJ);
		double dist = round(distMatrix[minI][minJ]);
		newSeqNames.add("("+seqNames.get(minJ)+", "+ seqNames.get(minI)+", "+dist+")");
		
		double[][] newDistMatr = new double[distMatrix.length - 1][distMatrix.length - 1];
		int newI, newJ; //positions in the new matrix
		
		//first, fill the new matrix with values that don't need to be recalculated:
		for (int i=0; i < distMatrix.length; i++){
			for (int j = 0; j < i; j++){
				if (i != minI && i != minJ && j != minI && j != minJ){
					//find new position in the new matrix where to put the value
					newI = i; newJ = j;
					if (i > minI) newI--;
					if (i > minJ) newI--;
					if (j > minI) newJ--;
					if (j > minJ) newJ--;
				
					newDistMatr[newI][newJ] = distMatrix[i][j];
				}				
			}
		}
		
		//now fill the last column with recalculated values
		newI = newDistMatr.length - 1;
		for (int k = 0; k < newSeqNames.size()-1; k++) {
			int oldJ = seqNames.indexOf(newSeqNames.get(k));
			if (oldJ < minJ){
				newDistMatr[newI][k] = 
					(distMatrix[minJ][oldJ] + distMatrix[minI][oldJ]) / 2.0;
			} else if (oldJ < minI){
				newDistMatr[newI][k] = 
					(distMatrix[oldJ][minJ] + distMatrix[minI][oldJ]) / 2.0;				
			}else {
				newDistMatr[newI][k] = 
					(distMatrix[oldJ][minJ] + distMatrix[oldJ][minI]) / 2.0;				
			}
			newDistMatr[newI][k] = round(newDistMatr[newI][k]);
		}
/*		
		System.out.println("*** Initial sequences:");
		System.out.println("Sequence names: "+ seqNames.toString());
		printMatrix(distMatrix);

		System.out.println("*** Computed cluster sequences:");
		System.out.println("Sequence names: "+ newSeqNames.toString());
		printMatrix(newDistMatr);
*/		
		findClusters(newDistMatr, newSeqNames);
	}
	
	
	private void printMatrix(double[][] matrix){
		for (int j = 0; j < matrix[0].length; j++) {
			for (int i = 0; i < matrix.length; i++) {
				System.out.print(matrix[i][j]+"\t");	
			}
			System.out.println();
		}
	}

}
