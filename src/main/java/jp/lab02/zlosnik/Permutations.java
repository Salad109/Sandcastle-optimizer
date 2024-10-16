package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.List;

public abstract class Permutations {
    public static List<List<Integer>> getPermutations(ArrayList<Bucket> bucketList) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        generatePermutations(result, current, bucketIndexes);

        return result;
    }

    private static void generatePermutations(List<List<Integer>> result, List<Integer> current, int[] numbers) {
        if (!current.isEmpty()) {
            result.add(new ArrayList<>(current));
        }

        if (current.size() < numbers.length) {
            for (int number : numbers) {
                current.add(number);
                generatePermutations(result, current, numbers);
                current.removeLast();
            }
        }
    }
}
