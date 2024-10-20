package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;
import jp.lab02.zlosnik.Main;

import java.util.*;

public abstract class PermutationFilter {
    private PermutationFilter() {
    }

    // Filters permutations that are valid based on the bucket volume and step
    static List<List<Integer>> filterPossiblePermutations(List<Bucket> bucketList, List<List<Integer>> permutations) {
        List<List<Integer>> validPermutations = new ArrayList<>();

        for (List<Integer> permutation : permutations) {
            Map<Integer, Integer> occurrences = PermutationBuilder.countOccurrences(permutation);

            // Check if the current permutation is valid
            boolean isValid = true; // Assume valid unless proven otherwise

            for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                int key = entry.getKey();
                int count = entry.getValue();

                // Check the validity condition
                if (count * Main.STEP > bucketList.get(key - 1).volume) {
                    isValid = false; // Set isValid to false if condition fails
                    break; // No need to check further
                }
            }

            if (isValid) {
                validPermutations.add(permutation);
            }
        }

        return validPermutations;
    }

    static Map<List<Integer>, List<List<Integer>>> filterCombinations(Map<List<Integer>, List<List<Integer>>> permutationCombinations) {
        Iterator<Map.Entry<List<Integer>, List<List<Integer>>>> iterator = permutationCombinations.entrySet().iterator();

        // Loop through the map entries
        while (iterator.hasNext()) {
            Map.Entry<List<Integer>, List<List<Integer>>> entry = iterator.next();
            // If the value (a list of lists) is empty, remove the entry
            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        }
        return permutationCombinations;
    }

    static List<List<List<Integer>>> cleanUnwrappedCombinations(List<Bucket> bucketList, List<Castle> castleList, List<List<List<Integer>>> unwrappedCombinationList) {
        List<List<List<Integer>>> cleanCombinationList = new LinkedList<>();
        Castle firstCastle = castleList.get(0);
        Castle secondCastle = castleList.get(1);
        for (List<List<Integer>> unwrappedCombination : unwrappedCombinationList) {
            firstCastle.addLayerStack(bucketList, unwrappedCombination.getFirst());
            secondCastle.addLayerStack(bucketList, unwrappedCombination.getLast());
            if (firstCastle.baseRadius == 0 && secondCastle.baseRadius == 0) {
                cleanCombinationList.add(unwrappedCombination);
            }
            firstCastle = firstCastle.getBlankCastle();
            secondCastle = secondCastle.getBlankCastle();
        }
        return cleanCombinationList;
    }
}
