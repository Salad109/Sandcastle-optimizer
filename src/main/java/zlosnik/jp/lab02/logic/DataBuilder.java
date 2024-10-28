package zlosnik.jp.lab02.logic;

import zlosnik.jp.lab02.objects.Bucket;
import zlosnik.jp.lab02.objects.Castle;
import zlosnik.jp.lab02.Main;

import java.util.*;

public abstract class DataBuilder {
    private DataBuilder() {
    }

    public static List<List<Integer>> getLayerPermutations(List<Bucket> bucketList) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        int maxPossibleLength = calculateMaxLength(bucketList);

        List<List<Integer>> permutations = generatePermutations(bucketIndexes, maxPossibleLength);
        return DataFilter.filterPossiblePermutations(bucketList, permutations);
    }

    private static int calculateMaxLength(List<Bucket> bucketList) {
        double totalVolume = 0.0;
        for (Bucket bucket : bucketList) {
            totalVolume += bucket.volume;
        }
        return (int) (totalVolume / Main.STEP);
    }

    private static List<List<Integer>> generatePermutations(int[] numbers, int maxLength) {
        List<List<Integer>> result = new ArrayList<>();
        generatePermutationsRecursive(result, new ArrayList<>(), numbers, maxLength);
        return result;
    }

    private static void generatePermutationsRecursive(List<List<Integer>> result, List<Integer> current, int[] numbers, int maxLength) {
        if (current.size() > maxLength) return;

        if (!current.isEmpty()) {
            result.add(new ArrayList<>(current));
        }

        for (int number : numbers) {
            current.add(number);
            generatePermutationsRecursive(result, current, numbers, maxLength);
            current.removeLast();
        }
    }

    public static List<List<Integer>> getPermutationsForCastle(Castle castle, List<Bucket> bucketList, List<List<Integer>> totalPermutations) {
        List<List<Integer>> completePermutations = new ArrayList<>();
        for (List<Integer> permutation : totalPermutations) {
            castle = castle.getBlankCastle();
            for (int i = 0; i < permutation.size(); i++) {
                castle.addLayer(Main.STEP, bucketList.get(permutation.get(i) - 1).angle);
                if (castle.complete) {
                    if (i == permutation.size() - 1)
                        completePermutations.add(permutation);
                    else
                        break;
                }
            }
        }
        return completePermutations;
    }
}
