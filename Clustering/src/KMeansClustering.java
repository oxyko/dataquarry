/**
 * @author      Oksana Korol 
 * @version     0.8                        
 * @since       2013-01-16   
 * 
 * K-Means Clustering example.
 */

import java.util.Arrays;


public class KMeansClustering {
	
	private int numberOfClusters;
	private double[][] data;
	
	public KMeansClustering(double[][] data, int numberOfClusters){
		this.data = data;
		this.numberOfClusters = numberOfClusters;
	}

	
	public  int[] findClusters() throws Exception {
		int[] clusters = new int[data.length];
		int[] previousClusters = new int[data.length];
		double[][] centroids = null;
		double[][] dist = new double[data.length][numberOfClusters];
		
		do{
			// *** Calculate centroids (centers of each cluster)
			if (centroids == null){
				centroids = pickInitialCentroids();
			} else {
				centroids = calculateCentroids(clusters);
			}
			
			// *** Calculate the distance between each data point and each centroid
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < centroids.length; j++) {
					dist[i][j] = calculateSquaredEuclidianDistance(data[i], centroids[j]);
				}
			}
			
			// *** Assign data to clusters
			previousClusters = Arrays.copyOf(clusters, clusters.length);
			for (int i = 0; i < dist.length; i++) {
				clusters[i] = minValueIndex(dist[i]);
			}
			
			
		} while (!Arrays.equals(clusters, previousClusters));
		
		
		return clusters;
	}
	
	/* Given an array of values, returns an index in the array
	 * which contains the smallest value.
	 * I.e. Array A = [3, 5.5, 1, 10]; result will be 2, since A[2] = 1 and 1 is the smallest value
	 */
	private int minValueIndex(double[] ds) {
		int minValueIndex = 0;
		
		for (int i = 0; i < ds.length; i++) {
			if (ds[minValueIndex] > ds[i]){
				minValueIndex = i;
			}
		}
		return minValueIndex;
	}


	/* Calculates the square of Euclidean distance between two data points.
	 * The formula is as follows:
	 * 	For points A and B with n coordinates, D = (coordinate_1(A) - coordinate_1(B))^2 + ... + (coordinate_n(A) - coordinate_n(B))^2
	 *  Example. For for two points on a plain (n=2). Point1: (2,4), point2: (3, -1). D = (2-3)^2 + (4 -(-1))^2 = 1 + 25 = 26.
	 *  
	 * Note that Euclidean distance takes a square root from the above formula.
	 * 
	 */
	public double calculateSquaredEuclidianDistance(double[] pointA, double[] pointB) throws Exception{
		if (pointA.length != pointB.length){
			throw new Exception("Can not calculate Euclidean distance: point should have the same dimention.");
		}
		double result = 0.0;
		
		for (int i = 0; i < pointB.length; i++) {
			result = result + (pointA[i] - pointB[i])*(pointA[i] - pointB[i]);
		}		
		return result;
	}
	
	
	private double[][] pickInitialCentroids(){
		double[][] centroids = new double[numberOfClusters][data[0].length];
		
		//pick first <numberOfClusters> instances from the list as the initial cluster centers:
		for (int i = 0; i < numberOfClusters; i++) {
			for (int j = 0; j < data[0].length; j++) {
				centroids[i][j] = data[i][j];
			}
		}
		
		return centroids;
	}
	
	private double[][] calculateCentroids(int[] clusters){
		double[][] centroids = new double[numberOfClusters][data[0].length];
		
		for (int k = 0; k < numberOfClusters; k++) {
			int numOfPointsInCluster = 0;
			
			// ** Sum up coordinates of all points in the cluster
			for (int i = 0; i < clusters.length; i++) {
				if (clusters[i] == k){ //the point belongs to a cluster
					numOfPointsInCluster++;
					for (int j = 0; j < data[i].length; j++) {
						centroids[k][j] = centroids[k][j] + data[i][j];
					}
				}
			}
			
			// ** Average summed up coordinates over the number of points in a cluster
			for (int i = 0; i < centroids[k].length; i++) {
				centroids[k][i] = centroids[k][i] / numOfPointsInCluster;
			}
		}
		
		return centroids;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*
		double[][] data = new double[][]{
				{2, 1}, 
				{1, 2}, 
				{2, 3}, 
				{6, 1}, 
				{5, 1}, 
				{3, 3}, 
				{2,-3}, 
				{3,-2}, 
				{6, 0}
		};
*/
		double[][] data = new double[][]{
				{2, 1}, 
				{6, 1}, 
				{2,-3}, 
				{3,-2}, 
				{1, 2}, 
				{2, 3}, 
				{5, 1}, 
				{3, 3}, 
				{6, 0}
		};
		
		int numberOfClusters = 3;
		KMeansClustering kMeans = new KMeansClustering(data, numberOfClusters);
		
		try {
			int[] result = kMeans.findClusters();
			
			System.out.println("Instance\tCluster");
			for (int i = 0; i < result.length; i++) {
				System.out.print("(");
				for (int j = 0; j < data[0].length; j++) {
					System.out.print(data[i][j]);
					if (j<(data[0].length-1)) System.out.print(", ");
				}
				System.out.println(")\t" + result[i]);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
