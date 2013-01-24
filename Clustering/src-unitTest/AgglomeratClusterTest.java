

import java.util.ArrayList;

import junit.framework.TestCase;

public class AgglomeratClusterTest extends TestCase {
	AgglomeratClustering_ArrayImpl fixtureArr = new AgglomeratClustering_ArrayImpl();
	AgglomeratClustering_HashImpl fixtureHash = new AgglomeratClustering_HashImpl();
	
	public void testPerformClustering1(){
		ArrayList<String> sequences = new ArrayList<String>();
		sequences.add("actgactgaa");
		sequences.add("actaactagg");
		sequences.add("actgaaaacc");
		sequences.add("actgacatcc");
		sequences.add("attgactgcc");

		ArrayList<String> seqNames = new ArrayList<String>();
		seqNames.add("A");
		seqNames.add("B");
		seqNames.add("C");
		seqNames.add("D");
		seqNames.add("E");
		
		String expected = "(B, ((C, D, 0.2), (A, E, 0.3), 0.4), 0.475)";
		String actualArr = fixtureArr.performClustering(sequences, seqNames);
		String actualHash = fixtureHash.performClustering(sequences, seqNames);
		
		assertEquals(expected, actualArr);
		assertEquals(expected, actualHash);
	}
	
	public void testPerformClustering2(){
		ArrayList<String> sequences = new ArrayList<String>();
		sequences.add("gccctcttcctaacactcacaacaaaactaaccaacactaacattacggatgcccaagaa"); //Hy
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactagtatttcagacgcccaggaa"); //Pa
		sequences.add("gcccttttcctaacactcacaacaaagctaactagcaccaacatctcagacgcccaagaa"); //Go
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactaacatctcagacgctcaggaa"); //Ho

		ArrayList<String> seqNames = new ArrayList<String>();
		seqNames.add("Hy");
		seqNames.add("Pa");
		seqNames.add("Go");
		seqNames.add("Ho");
		
		String expected = "(Hy, (Go, (Pa, Ho, 0.067), 0.116), 0.15)";
		String actualArr = fixtureArr.performClustering(sequences, seqNames);
		String actualHash = fixtureHash.performClustering(sequences, seqNames);
		
		assertEquals(actualArr, actualHash);
		assertEquals(expected, actualArr);
		assertEquals(expected, actualHash);
	}
	
	public void testPerformClustering3(){
		ArrayList<String> sequences = new ArrayList<String>();
		sequences.add("gccctcttcctaacactcacaacaaaactaaccaacactaacattacggatgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactagtatttcagacgcccaggaa"); 
		sequences.add("gcccttttcctaacactcacaacaaagctaactagcaccaacatctcagacgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactaacatctcagacgctcaggaa"); 
		sequences.add("gccctattcgtaacactcacaacaatactaaccaacactaacattacggatgcccaagaa"); 
		sequences.add("gtccttttcctatcactcacaacaaaactatctaatactagtattcctgacgcacaggca"); 
		sequences.add("gcccctttgctgacagtcacaaaaaagctaactagcaccaacatctcagacgcccaagaa"); 
		sequences.add("gcccttttcctgacactcacaacataactaaataatactaccatctcagacgctcagcaa"); 
		sequences.add("gccctcatcctaactctcacgacaaaactaaccaacactaacataacggatgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactagtattacagacgcccaggaa"); 
		sequences.add("gcccttatcctaactctcacgactttgctaactagcaccaacatcacagacgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaacataactaactaatactaacatctgagacgctcaggaa"); 
		sequences.add("gccctattcgtaatactcacgacaatactaaccaacactaacattagggatgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaacataactaactaatactagtattcgagacgcacaggca"); 
		sequences.add("gcccctatgctgacagtcacgaaaaagctaactagcaccaacatctgagacgcccaagaa"); 
		sequences.add("gcccttttcgtgacactcacgacagaactaaataatactaccatctgagacgctcagcaa"); 
		sequences.add("gccatcttcctaacactcacaacataactagccaacagtaacgttacggatgcccaagaa"); 
		sequences.add("gcccttatcctaacactcacaacataactagctaatagtagtgtttcagacgcccaggaa"); 
		sequences.add("gcccttttcctaacactcacaacatagctagctagcagcaacgtctctgacgcccaagaa"); 
		sequences.add("gcccttttactaacactcacaacataactagctaatagtaacttctcagacgctcaggaa"); 
		sequences.add("gccctattcataacactcacaacattactagccaacagtaactttacggatgcccaagaa"); 
		sequences.add("gtccttttccaatcactcacaacataactagctaatagtagttttgctgacgcaaaggca"); 
		sequences.add("gcccctttgctaacagtcacaaaatagctagctagcagcaacctctcagacgcccaagaa"); 
		sequences.add("gcccttttcctgatactcacaacaaaactagataatagtaccctctcagacgctcagcaa"); 
		sequences.add("gccctcatcctaactttcacgacataactagccaacagtaacctaacggatgcccaagaa"); 
		sequences.add("gcccttttcctaacactaacaacataactagctaatagtagttttacagacgcccaggaa"); 
		sequences.add("gcccttatcctaactctcaggacatagctagctagcagcaacttcacagacgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaagaaaactagctaatagtaacttctgagacgctcaggaa"); 
		sequences.add("gccctattcgtaatactcacgacattactagccaacagtaaggttagggatgcccaagaa"); 
		sequences.add("gcccttttcctaacactcacaacaaaactagctaatagtagtggtcgagacgcacaggca"); 
		sequences.add("gcccctatgctgacagtcacgaaatagctagctagcagcaacgtcggagacgcccaagaa"); 
		sequences.add("gcccttttcgtgacactcacgacataactagataatagtaccttcggagacgctcagcaa"); 
		
		ArrayList<String> seqNames = new ArrayList<String>();
		seqNames.add("A");
		seqNames.add("B");
		seqNames.add("C");
		seqNames.add("D");
		seqNames.add("E");
		seqNames.add("F");
		seqNames.add("G");
		seqNames.add("H");
		seqNames.add("I");
		seqNames.add("J");
		seqNames.add("K");
		seqNames.add("L");
		seqNames.add("M");
		seqNames.add("N");
		seqNames.add("O");
		seqNames.add("P");
		seqNames.add("A1");
		seqNames.add("B1");
		seqNames.add("C1");
		seqNames.add("D1");
		seqNames.add("E1");
		seqNames.add("F1");
		seqNames.add("G1");
		seqNames.add("H1");
		seqNames.add("I1");
		seqNames.add("J1");
		seqNames.add("K1");
		seqNames.add("L1");
		seqNames.add("M1");
		seqNames.add("N1");
		seqNames.add("O1");
		seqNames.add("P1");
		
		//String expected = "(Hy, (Go, (Pa, Ho, 0.067), 0.116), 0.15)";
		long start = System.currentTimeMillis();
		String actualArr = fixtureArr.performClustering(sequences, seqNames);
		long stop1 = System.currentTimeMillis();
		String actualHash = fixtureHash.performClustering(sequences, seqNames);
		long stop2 = System.currentTimeMillis();
		
		System.out.println("Execution time:\n Array implementation: " + (stop1 - start) + "ms\n Hash implementation:  " + (stop2 - stop1) + "ms");
		System.out.println("Array result: " + actualArr);
		System.out.println("Hash  result: " + actualHash);
		//Note that two implementation won't necessarily be the same, because
		//when there are several minimal distances that are the same,
		//one algorithm may pick different instance
		assertEquals(actualArr, actualHash);
		
	}
	
	public void testComputeDistanceMatrix(){
		ArrayList<String> sequences = new ArrayList<String>();
		sequences.add("gccctcttcctaacactcacaacaaaactaaccaacactaacattacggatgcccaagaa"); //Hy
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactagtatttcagacgcccaggaa"); //Pa
		sequences.add("gcccttttcctaacactcacaacaaagctaactagcaccaacatctcagacgcccaagaa"); //Go
		sequences.add("gcccttttcctaacactcacaacaaaactaactaatactaacatctcagacgctcaggaa"); //Ho
		
		double[][] actual = fixtureArr.computeDistanceMatrix(sequences);
		double[][] expected = {
				{0.0, 0.0, 0.0, 0.0},
				{0.15, 0.0, 0.0, 0.0},
				{0.15, 0.133, 0.0, 0.0},
				{0.15, 0.067, 0.1, 0.0},
		};
		for (int i = 0; i<actual.length; i++){
			for (int j = 0; j<actual.length; j++){
				assertEquals(expected[i][j], actual[i][j]);
			}
		}
		
	}
	
	public void testGetHummingDistance(){
		assertEquals(3, fixtureArr.getHummingDistance("abcdefg", "aXcdXfX"));
	}
	

	
}
