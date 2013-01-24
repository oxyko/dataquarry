/**
 * @author      Oksana Korol 
 * @version     0.8                        
 * @since       2013-01-22   
 * 
 * Example of hierarchical agglomerative clustering for character strings of the same size.
 * Implementation using HashMap.
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgglomeratClustering_HashImpl {
	
	
	// make sure that sequences are of the same length
	public String performClustering(ArrayList<String> sequences, ArrayList<String> seqNames){
		ConcurrentHashMap<ClusterKey, Double> clusterDistances = computeDistanceMatrix(sequences, seqNames);
		//printHash(clusterDistances);
		String clusterStr = agglomerate(clusterDistances);
		return clusterStr;
	}
	
	
	/* Computer the distance between each
	 * sequence pair in the list
	 */
	public ConcurrentHashMap<ClusterKey, Double> computeDistanceMatrix(ArrayList<String> sequences, ArrayList<String> seqNames){
		ConcurrentHashMap<ClusterKey, Double> clusterDistances = new ConcurrentHashMap<ClusterKey, Double>();
		int seqNum = sequences.size();
		
		for (int i=0; i < (seqNum - 1); i++){
			for (int j = i+1; j < seqNum; j++) {
				clusterDistances.put(new ClusterKey(seqNames.get(i), seqNames.get(j)), 
						computeFractionalDifference(sequences.get(i), sequences.get(j))
						);
			}
		}
		return clusterDistances;
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
	
	
	public String agglomerate(ConcurrentHashMap<ClusterKey, Double> clusterDistances){
		
		//Iterator<Map.Entry<ClusterKey, Double>> outerIterator = clusterDistances.entrySet().iterator();
		while(clusterDistances.size() > 1){
		
			//***
			//printHash(clusterDistances);
		
			//find 2 sequences/clusters with minimal distance
			ClusterKey minPair = new ClusterKey("", "");
			Double minValue = Double.MAX_VALUE;
			Iterator<Map.Entry<ClusterKey, Double>> it = clusterDistances.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<ClusterKey, Double> pair = (Map.Entry<ClusterKey, Double>)it.next();
				if (pair.getValue() < minValue){
					minValue = pair.getValue();
					minPair = pair.getKey();
				}
			}
			
			String newClusterString = "(" + minPair.getAllValues() + ", " + minValue + ")";
			//System.out.println("Min distance pair: " +newClusterString);
			
			
			//aggregate min distance cluster and recalculate the distance from it to remaining clusters/sequences
			clusterDistances.remove(minPair);
			it = clusterDistances.entrySet().iterator();
			while(it.hasNext() && clusterDistances.size() > 1){
				Map.Entry<ClusterKey, Double> pair = (Map.Entry<ClusterKey, Double>)it.next();
				ClusterKey key = pair.getKey();
				Double value = pair.getValue();
				
				if (key.getValue1().equals(minPair.getValue1())){
					ClusterKey otherKey = new ClusterKey(key.getValue2(), minPair.getValue2());
					if (null != clusterDistances.get(otherKey)){
						Double newDistance = round((value + clusterDistances.get(otherKey)) / 2.0);
						clusterDistances.put(new ClusterKey(key.getValue2(), newClusterString), newDistance);
						clusterDistances.remove(otherKey);
						//clusterDistances.remove(key);
						it.remove();
					}
				} else if (key.getValue1().equals(minPair.getValue2())){
					ClusterKey otherKey = new ClusterKey(key.getValue2(), minPair.getValue1());
					if (null != clusterDistances.get(otherKey)){
						Double newDistance = round((value + clusterDistances.get(otherKey)) / 2.0);
						clusterDistances.put(new ClusterKey(key.getValue2(), newClusterString), newDistance);
						clusterDistances.remove(otherKey);
						it.remove();
					}
				} else if (key.getValue2().equals(minPair.getValue1())){
					ClusterKey otherKey = new ClusterKey(key.getValue1(), minPair.getValue2());
					if (null != clusterDistances.get(otherKey)){
						Double newDistance = round((value + clusterDistances.get(otherKey)) / 2.0);
						clusterDistances.put(new ClusterKey(key.getValue1(), newClusterString), newDistance);
						clusterDistances.remove(otherKey);
						it.remove();
					}
				} else if (key.getValue2().equals(minPair.getValue2())){
					ClusterKey otherKey = new ClusterKey(key.getValue1(), minPair.getValue1());
					if (null != clusterDistances.get(otherKey)){
						Double newDistance = round((value + clusterDistances.get(otherKey)) / 2.0);
						clusterDistances.put(new ClusterKey(key.getValue1(), newClusterString), newDistance);
						clusterDistances.remove(otherKey);
						it.remove();
					}
				}
				
			}
			
			

		}//outer while
		
		// At this point, clusterDistances has collected all clusters and their distances 
		// and contains only one element - "top of the tree" or distance between the
		// last two clusters
		
		//We still need to iterate through the set to get one element
		String result = "";
		Iterator<Map.Entry<ClusterKey, Double>> it = clusterDistances.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<ClusterKey, Double> pair = (Map.Entry<ClusterKey, Double>)it.next();
			result = "("+ ((ClusterKey)pair.getKey()).getAllValues() + ", " + pair.getValue()+")";
		}

		return result;
	}
	
	public void printHash(ConcurrentHashMap<ClusterKey, Double> hm){
		Iterator<Map.Entry<ClusterKey, Double>> it = hm.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<ClusterKey, Double> pair = (Map.Entry<ClusterKey, Double>)it.next();
			System.out.println("\t"+ ((ClusterKey)pair.getKey()).getAllValues() + " = " + pair.getValue()+"");
		}
	}

	
	/* Inner class to represent the key for HashMap of distances between clusters/sequences
	 * Key consists of 2 strings, they can be either names of the sequences or clusters.
	 * The order of keys do not matter, i.e. ClusterKey("A","B") and ClusterKey("B","A")
	 * point to the same value.
	 */
	static class ClusterKey{
		private String value1;
		private String value2;
		
		public ClusterKey(String value1, String value2){
			this.value1 = value1;
			this.value2 = value2;
		}

		public String getValue1() {
			return value1;
		}

		public void setValue1(String value1) {
			this.value1 = value1;
		}

		public String getValue2() {
			return value2;
		}

		public void setValue2(String value2) {
			this.value2 = value2;
		}
		
		public String getAllValues(){
			return (null == value1 ? "NaN" : value1) + ", " + 
					(null == value2 ? "NaN" : value2);
		}

		
		public boolean equals(Object o){
			if (o != null && o instanceof ClusterKey){
				ClusterKey obj = (ClusterKey) o;
				if (this.getValue1() == null && obj.getValue1() == null &&
						this.getValue2() == null && obj.getValue2() == null){
					return true;
				}
				
			
				if (this.getValue1().equals(obj.getValue1()) &&
						this.getValue2().equals(obj.getValue2())){
					return true;
				}
				
				if (this.getValue1().equals(obj.getValue2()) &&
						this.getValue2().equals(obj.getValue1())){
					return true;
				}
				
			}
			return false;
		}
		
		public int hashCode() {
            int hashCode = 13;

            hashCode = 31 * hashCode + (null == value1 ? 0 : value1.hashCode())+ (null == value2 ? 0 : value2.hashCode());

            return hashCode;
		}
		
	}


}
