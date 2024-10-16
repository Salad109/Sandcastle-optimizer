package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.List;

public abstract class Permutations {
    public static List<List<Integer>> getPermutations(ArrayList<Bucket> bucketList, double STEP) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        double totalVolume = 0;
        for (Bucket bucket : bucketList) {
            totalVolume += bucket.volume;
        }
        int maxPossibleLength = (int) (totalVolume / STEP);
        System.out.println(maxPossibleLength);

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        generatePermutations(result, current, bucketIndexes, maxPossibleLength);

        return result;
    }

    private static void generatePermutations(List<List<Integer>> result, List<Integer> current, int[] numbers, int length) {
        // Base case: add the current list if it is non-empty and within the length constraint
        if (!current.isEmpty() && current.size() <= length) {
            result.add(new ArrayList<>(current));
        }

        // Stop the recursion when we reach the maximum length
        if (current.size() == length) {
            return;
        }

        // Recurse further to generate permutations
        for (int number : numbers) {
            current.add(number);
            generatePermutations(result, current, numbers, length);
            current.removeLast();  // Remove the last added element
        }
    }

}
