package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Permutations {

    // Main entry point: returns the valid permutations for given bucketList and STEP
    public static List<List<Integer>> getPermutations(List<Bucket> bucketList, double STEP) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        int maxPossibleLength = calculateMaxLength(bucketList, STEP);

        List<List<Integer>> permutations = generatePermutations(bucketIndexes, maxPossibleLength);
        return filterValidPermutations(bucketList, permutations, STEP);
    }

    // Calculates the maximum possible length of permutations based on total volume and step
    private static int calculateMaxLength(List<Bucket> bucketList, double STEP) {
        double totalVolume = bucketList.stream().mapToDouble(bucket -> bucket.volume).sum();
        return (int) (totalVolume / STEP);
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
    private static List<List<Integer>> filterValidPermutations(List<Bucket> bucketList, List<List<Integer>> permutations, double STEP) {
        List<List<Integer>> validPermutations = new ArrayList<>();

        for (List<Integer> permutation : permutations) {
            Map<Integer, Integer> occurrences = countOccurrences(permutation);

            // Check if the current permutation is valid
            boolean isValid = occurrences.entrySet().stream().allMatch(entry ->
                    entry.getValue() * STEP <= bucketList.get(entry.getKey() - 1).volume
            );

            if (isValid) {
                validPermutations.add(permutation);
            }
        }

        return validPermutations;
    }

    // Counts the occurrences of each number in the given list
    private static Map<Integer, Integer> countOccurrences(List<Integer> numbers) {
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (Integer number : numbers) {
            occurrences.put(number, occurrences.getOrDefault(number, 0) + 1);
        }
        return occurrences;
    }
}
