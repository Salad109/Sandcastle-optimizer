package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;

import java.util.*;

public abstract class PermutationBuilder {
    private PermutationBuilder() {
    }

    // Main entry point: returns the valid permutations for given bucketList and STEP
    public static List<List<Integer>> getPermutations(List<Bucket> bucketList, double step) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        int maxPossibleLength = calculateMaxLength(bucketList, step);

        List<List<Integer>> permutations = generatePermutations(bucketIndexes, maxPossibleLength);
        return filterPossiblePermutations(bucketList, permutations, step);
    }

    // Calculates the maximum possible length of permutations based on total volume and step
    private static int calculateMaxLength(List<Bucket> bucketList, double step) {
        double totalVolume = bucketList.stream().mapToDouble(bucket -> bucket.volume).sum();
        return (int) (totalVolume / step);
    }

    // Generates all permutations up to the given maximum length
    private static List<List<Integer>> generatePermutations(int[] numbers, int maxLength) {
        List<List<Integer>> result = new ArrayList<>();
        generateRecursivePermutations(result, new ArrayList<>(), numbers, maxLength);
        return result;
    }

    // Recursive helper for generating permutations
    private static void generateRecursivePermutations(List<List<Integer>> result, List<Integer> current, int[] numbers, int maxLength) {
        // Base case: if the current permutation is valid, add it
        if (!current.isEmpty() && current.size() <= maxLength) {
            result.add(new ArrayList<>(current));
        }

        // Stop recursion if we hit the maximum length
        if (current.size() == maxLength) return;

        // Recursively add more numbers to the current permutation
        for (int number : numbers) {
            current.add(number);
            generateRecursivePermutations(result, current, numbers, maxLength);
            current.removeLast();  // Backtrack
        }
    }

    // Filters permutations that are valid based on the bucket volume and STEP
    private static List<List<Integer>> filterPossiblePermutations(List<Bucket> bucketList, List<List<Integer>> permutations, double step) {
        List<List<Integer>> validPermutations = new ArrayList<>();

        for (List<Integer> permutation : permutations) {
            Map<Integer, Integer> occurrences = countOccurrences(permutation);

            // Check if the current permutation is valid
            boolean isValid = true; // Assume valid unless proven otherwise

            for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                int key = entry.getKey();
                int count = entry.getValue();

                // Check the validity condition
                if (count * step > bucketList.get(key - 1).volume) {
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

    public static List<List<Integer>> getCompletePermutations(List<List<Integer>> permutations, Castle castle, List<Bucket> bucketList, double step) {
        List<List<Integer>> completePermutations = new ArrayList<>();
        for (List<Integer> permutation : permutations) {
            castle = castle.getBlankCastle();
            castle.addLayerStack(bucketList, permutation, step);
            if (castle.complete) completePermutations.add(permutation);
        }
        return completePermutations;
    }

    public static void printCombinations(Map<List<Integer>, List<List<Integer>>> permutationCombinations) {
        for (Map.Entry<List<Integer>, List<List<Integer>>> entry : permutationCombinations.entrySet()) {
            String keyString = entry.getKey().toString();
            String valueString = entry.getValue().toString();
            System.out.println(keyString + "=" + valueString);
        }
    }

    public static Map<List<Integer>, List<List<Integer>>> getCombinations(List<Castle> castleList, DataReader dataReader, double step, List<List<Integer>> permutations) {
        Map<List<Integer>, List<List<Integer>>> permutationCombinations = new HashMap<>();
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
                    newBucketList.get(key - 1).volume -= value * step; // Subtract sand based on the occurrences
                }

                // Compute new permutations based on the updated bucket list
                permutations = PermutationBuilder.getPermutations(newBucketList, step);

                // Filter new permutations for the current castle
                List<List<Integer>> filteredPermutations = PermutationBuilder.getCompletePermutations(permutations, castle, newBucketList, step);

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
}
