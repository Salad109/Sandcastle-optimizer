package jp.lab01.zlosnik;

import java.util.ArrayList;
import java.util.List;

public abstract class Permutations {

    // Method to get all permutations of the provided numbers
    public static List<List<Integer>> getPermutations(int[] numbers) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        // Generate permutations starting with an empty chain
        generatePermutations(result, current, numbers);

        // Return the list of all permutations
        return result;
    }

    // Recursive helper method to generate permutations
    private static void generatePermutations(List<List<Integer>> result, List<Integer> current, int[] numbers) {
        // Add the current permutation to the result
        if (!current.isEmpty()) {
            result.add(new ArrayList<>(current));
        }

        // Continue building permutations if the current list is smaller than the array length
        if (current.size() < numbers.length) {
            for (int number : numbers) {
                current.add(number);
                generatePermutations(result, current, numbers); // Recur with the new chain
                current.removeLast(); // Backtrack
            }
        }
    }
}
