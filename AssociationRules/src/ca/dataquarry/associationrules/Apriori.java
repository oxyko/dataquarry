package ca.dataquarry.associationrules;
/**
 * @author      Oksana Korol
 * @version     0.8                        
 * @since       2012-11-13   
 * 
 * Method for finding frequent patters (itemsets) in transaction data
 * Based on Apriori algorithm by R Agrawal, R Srikant (Proc. 20th Int. Conf. Very Large Data, 1994)
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class Apriori {
	
	public static void main(String[] args){
		// Initialize test transactions		
		ArrayList<HashSet<String>> transactions = new ArrayList<HashSet<String>>();
		transactions.add(new HashSet<String>(){{add("Tea");add("Coffee");add("Chocolate");}});
		transactions.add(new HashSet<String>(){{add("Coffee");add("Mints");}});
		transactions.add(new HashSet<String>(){{add("Coffee");add("Muffin");}});
		transactions.add(new HashSet<String>(){{add("Tea");add("Coffee");add("Mints");}});
		transactions.add(new HashSet<String>(){{add("Tea");add("Muffin");}});
		transactions.add(new HashSet<String>(){{add("Coffee");add("Muffin");}});
		transactions.add(new HashSet<String>(){{add("Tea");add("Muffin");}});
		transactions.add(new HashSet<String>(){{add("Tea");add("Coffee");add("Muffin");add("Chocolate");}});
		transactions.add(new HashSet<String>(){{add("Tea");add("Coffee");add("Muffin");}});

		
		ArrayList<String> frequentItemsets = getFrequentItemsets(transactions, 2);
		
		System.out.println("Frequent itemsets:");
		for (Iterator iterator = frequentItemsets.iterator(); iterator.hasNext();) {
			System.out.println(iterator.next());
		}
	}
		

	
	public static ArrayList<String> getFrequentItemsets(ArrayList<HashSet<String>> transactions, int minSupport){
		ArrayList<String> candidateItemSets = new ArrayList<String>();
		ArrayList<Integer> candidateItemSetSupport = new ArrayList<Integer>();
		
		// Generate one item itemsets with support
		for (Iterator transactionsIterator = transactions.iterator(); transactionsIterator.hasNext();) {
			HashSet<String> transactionItems = (HashSet<String>) transactionsIterator.next();
			for (Iterator transactionItemsIterator = transactionItems.iterator(); transactionItemsIterator.hasNext();) {
				String transactionItem = (String) transactionItemsIterator.next();
				
				if (!candidateItemSets.contains(transactionItem)){
					candidateItemSets.add(transactionItem);
					candidateItemSetSupport.add(1);
				} else {
					int itemIndex = candidateItemSets.indexOf(transactionItem);
					candidateItemSetSupport.set(itemIndex, 
							candidateItemSetSupport.get(itemIndex)+1);
				}
			}
		}
		
		// Prune itemsets based on support to get frequent itemsets
		for (int i = 0; i < candidateItemSetSupport.size(); i++) {
			if (candidateItemSetSupport.get(i) < minSupport){
				candidateItemSets.remove(i);
				candidateItemSetSupport.remove(i);
				i--;
			}
		}
		
		
		//Generate new itemsets with increasing size from the previous itemsets
		while (!candidateItemSets.isEmpty()){
			ArrayList<String> newCandidates = generateNewCandidates(candidateItemSets);
			ArrayList<Integer> newCandidatesSupport = new ArrayList<Integer>(newCandidates.size());
			for (int i = 0; i < newCandidates.size(); i++){
				newCandidatesSupport.add(0);
			}
						
			//calculate support for new itemsets from the transactions
			for (Iterator iterator = transactions.iterator(); iterator.hasNext();) {
				HashSet<String> transaction = (HashSet<String>) iterator.next();
				for (int i = 0; i < newCandidates.size(); i++) {
					String[] candidates = (newCandidates.get(i)).split(",");
					boolean transactionContainsItemset = false;
					for (int j = 0; j < candidates.length; j++){
						if (transaction.contains(candidates[j])){
							transactionContainsItemset = true;
						} else {
							transactionContainsItemset = false;
							break;
						}
					}
						
					if (transactionContainsItemset){
						newCandidatesSupport.set(i, newCandidatesSupport.get(i) + 1);
					}
				}
			}
			
			// prune candidates that don't meet support criteria
			for (int i = 0; i < newCandidatesSupport.size(); i++) {
				if (newCandidatesSupport.get(i)  < minSupport){
					newCandidates.remove(i);
					newCandidatesSupport.remove(i);
					i--;
				}
			}
			
			if (newCandidates.isEmpty()){
				break;
			} else {
				candidateItemSets.clear();
				candidateItemSets = new ArrayList<String>(newCandidates);
			}
			
		}
		
		return candidateItemSets;
		
	}
	
	
	public static ArrayList<String> generateNewCandidates(ArrayList<String> oldCandidates){
		ArrayList<String> newCandidates = new ArrayList<String>();
		
		/*
		 * Find all itemsets
		 */
		for (int i = 0; i < oldCandidates.size() - 1; i++){
			
			String itemSet1 = oldCandidates.get(i);
			int lastItemIndex1 = itemSet1.lastIndexOf(",");
			String itemPrefix1;
			String itemSuffix1;
			if (lastItemIndex1 > 0){
				itemPrefix1 = itemSet1.substring(0, lastItemIndex1);
				itemSuffix1 = itemSet1.substring(lastItemIndex1+1, itemSet1.length());
			} else {
				itemPrefix1 = "";
				itemSuffix1 = itemSet1;
			}
			
			
			for (int j = i+1; j < oldCandidates.size(); j++){
				String itemSet2 = oldCandidates.get(j);
				int lastItemIndex2 = itemSet2.lastIndexOf(",");
				String itemPrefix2;
				String itemSuffix2;
				if (lastItemIndex1 > 0){
					itemPrefix2 = itemSet2.substring(0, lastItemIndex2);
					itemSuffix2 = itemSet2.substring(lastItemIndex2+1, itemSet2.length());
				} else {
					itemPrefix2 = "";
					itemSuffix2 = itemSet2;
				}
				
				if (itemPrefix1.equals(itemPrefix2)){
					if (!itemPrefix1.isEmpty()){
						newCandidates.add(itemPrefix1 + "," + itemSuffix1 + "," + itemSuffix2);
					} else {
						newCandidates.add(itemSuffix1 + "," + itemSuffix2);
					}
				} else {
					break;
				}
			}
		}
		
		/*
		 * Prune itemsets
		 */
		for (int i = 0; i < newCandidates.size(); i++) {
			String[] newItemSets = (newCandidates.get(i)).split(",");
			int k = newItemSets.length;
			
			//go through all itemsets of k-1 size 
			ArrayList<String> subSets = getAllSubsets(newItemSets, k-1);
			
			for (Iterator iterator = subSets.iterator(); iterator.hasNext();) {
				String newSubSet = (String) iterator.next();
				if (!oldCandidates.contains(newSubSet)){
					newCandidates.remove(i);
					i--;
					break;
				}
			}
			
		}
		
		return newCandidates;
	}
	
	

	public static ArrayList<String> getAllSubsets(String[] newItemSets, int subsetLength){
		ArrayList<String> result = new ArrayList<String>();
		
		ArrayList<ArrayList<String>> subSets = new ArrayList<ArrayList<String>>();
		
		getSubsets(newItemSets, subsetLength, 0, new ArrayList<String>(), subSets);
		
		for (Iterator iterator = subSets.iterator(); iterator.hasNext();) {
			ArrayList<String> subset = (ArrayList<String>) iterator.next();
			
			String subsetStr = "";
			for (int i = 0; i < subsetLength; i++) {
				subsetStr = subsetStr + "," + subset.get(i);
			}
			
			result.add(subsetStr.substring(1));
			
		}
		
		return result;
	}
	
	private static void getSubsets(String[] superSet, int subsetLength, int currentIndex, ArrayList<String> currentSet, ArrayList<ArrayList<String>> solution) {
	    
	    if (currentSet.size() == subsetLength) {
	        solution.add(new ArrayList<String>(currentSet));
	        return;
	    }
	    
	    if (currentIndex == superSet.length) return;
	    String item = superSet[currentIndex];
	    currentSet.add(item);
	    
	    getSubsets(superSet, subsetLength, currentIndex+1, currentSet, solution);
	    currentSet.remove(item);
	    
	    getSubsets(superSet, subsetLength, currentIndex+1, currentSet, solution);
	}
	
	
	
}
