package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;
import jp.lab02.zlosnik.Main;

import java.util.*;

public abstract class PermutationBuilder {
    private PermutationBuilder() {
    }

    // Main entry point: returns the valid permutations for given bucketList and STEP
    public static List<List<Integer>> getPermutations(List<Bucket> bucketList) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        int maxPossibleLength = calculateMaxLength(bucketList);

        List<List<Integer>> permutations = generatePermutations(bucketIndexes, maxPossibleLength);
        return filterPossiblePermutations(bucketList, permutations);
    }

    // Calculates the maximum possible length of permutations based on total volume and step
    private static int calculateMaxLength(List<Bucket> bucketList) {
        double totalVolume = bucketList.stream().mapToDouble(bucket -> bucket.volume).sum();
        return (int) (totalVolume / Main.STEP);
    }

    private static List<List<Integer>> generatePermutations(int[] numbers, int maxLength) {
        List<List<Integer>> result = new ArrayList<>();
        generatePermutationsRecursive(result, new ArrayList<>(), numbers, maxLength);
        return result;
    }

    private static void generatePermutationsRecursive(List<List<Integer>> result, List<Integer> current, int[] numbers, int maxLength) {
        // Base case: stop recursion if we hit the maximum length
        if (current.size() > maxLength) return;

        // Add the current permutation if it's non-empty
        if (!current.isEmpty()) {
            result.add(new ArrayList<>(current));
        }

        // Recursively generate more permutations
        for (int number : numbers) {
            current.add(number);
            generatePermutationsRecursive(result, current, numbers, maxLength);
            current.removeLast();  // Backtrack after recursion
        }
    }


    // Filters permutations that are valid based on the bucket volume and step
    private static List<List<Integer>> filterPossiblePermutations(List<Bucket> bucketList, List<List<Integer>> permutations) {
        List<List<Integer>> validPermutations = new ArrayList<>();

        for (List<Integer> permutation : permutations) {
            Map<Integer, Integer> occurrences = countOccurrences(permutation);

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


    // Counts the occurrences of each number in the given list
    public static Map<Integer, Integer> countOccurrences(List<Integer> permutation) {
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (Integer number : permutation) {
            occurrences.put(number, occurrences.getOrDefault(number, 0) + 1);
        }
        return occurrences;
    }

    public static List<List<Integer>> getCompletePermutations(List<List<Integer>> permutations, Castle castle, List<Bucket> bucketList) {
        List<List<Integer>> completePermutations = new ArrayList<>();
        for (List<Integer> permutation : permutations) {
            castle = castle.getBlankCastle();
            castle.addLayerStack(bucketList, permutation);
            if (castle.complete) completePermutations.add(permutation);
        }
        return completePermutations;
    }

    public static Map<List<Integer>, List<List<Integer>>> getCombinations(List<Castle> castleList, DataReader dataReader) {
        Map<List<Integer>, List<List<Integer>>> permutationCombinations = new HashMap<>();
        List<List<Integer>> permutations;
        for (Castle castle : castleList) {
            // For each castle, process its permutations
            for (List<Integer> castlePermutation : castle.completePermutationsList) {
                // Get a fresh bucket list for each permutation processing
                List<Bucket> newBucketList = dataReader.getBuckets();

                // Count occurrences of the permutation for the current castle
                Map<Integer, Integer> occurrences = PermutationBuilder.countOccurrences(castlePermutation);

                // Subtract sand from the appropriate buckets based on the current castle's permutation
                for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                    Integer key = entry.getKey();
                    Integer value = entry.getValue();
                    newBucketList.get(key - 1).volume -= value * Main.STEP; // Subtract sand based on the occurrences
                }

                // Compute new permutations based on the updated bucket list
                permutations = PermutationBuilder.getPermutations(newBucketList);

                // Filter new permutations for the current castle
                List<List<Integer>> filteredPermutations = PermutationBuilder.getCompletePermutations(permutations, castle, newBucketList);

                // Store the new permutation combination for the current castle
                permutationCombinations.put(castlePermutation, filteredPermutations);
            }
        }
        return filterCombinations(permutationCombinations);

    }

    private static Map<List<Integer>, List<List<Integer>>> filterCombinations(Map<List<Integer>, List<List<Integer>>> permutationCombinations) {
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

    public static List<List<List<Integer>>> unwrapCombinations(Map<List<Integer>, List<List<Integer>>> wrappedPermutationCombinations, List<Castle> castleList, List<Bucket> bucketList) {
        List<List<List<Integer>>> unwrappedCombinationList = new LinkedList<>();
        Castle firstCastle = castleList.getFirst();
        for (Map.Entry<List<Integer>, List<List<Integer>>> entry : wrappedPermutationCombinations.entrySet()) {
            List<Integer> key = entry.getKey();
            List<List<Integer>> valueList = entry.getValue();

            for (List<Integer> value : valueList) {
                List<List<Integer>> unwrappedCombination = new LinkedList<>();
                firstCastle.addLayerStack(bucketList, key);
                unwrappedCombination.add(key);
                unwrappedCombination.add(value);
                unwrappedCombinationList.add(unwrappedCombination);
            }
        }
        return cleanUnwrappedCombinations(bucketList, castleList, unwrappedCombinationList);
    }

    private static List<List<List<Integer>>> cleanUnwrappedCombinations(List<Bucket> bucketList, List<Castle> castleList, List<List<List<Integer>>> unwrappedCombinationList) {
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
