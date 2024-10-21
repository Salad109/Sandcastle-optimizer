package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;
import jp.lab02.zlosnik.Main;

import java.util.*;

public abstract class PermutationBuilder {
    private PermutationBuilder() {}
    public static List<List<Integer>> getPermutations(List<Bucket> bucketList) {
        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        int maxPossibleLength = calculateMaxLength(bucketList);

        List<List<Integer>> permutations = generatePermutations(bucketIndexes, maxPossibleLength);
        return PermutationFilter.filterPossiblePermutations(bucketList, permutations);
    }

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
            for (List<Integer> castlePermutation : castle.completePermutationsList) {
                List<Bucket> newBucketList = dataReader.getBuckets();

                Map<Integer, Integer> occurrences = PermutationBuilder.countOccurrences(castlePermutation);

                for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                    Integer key = entry.getKey();
                    Integer value = entry.getValue();
                    newBucketList.get(key - 1).volume -= value * Main.STEP;
                }

                permutations = PermutationBuilder.getPermutations(newBucketList);

                List<List<Integer>> filteredPermutations = PermutationBuilder.getCompletePermutations(permutations, castle, newBucketList);

                permutationCombinations.put(castlePermutation, filteredPermutations);
            }
        }
        return PermutationFilter.filterCombinations(permutationCombinations);

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
        return PermutationFilter.cleanUnwrappedCombinations(bucketList, castleList, unwrappedCombinationList);
    }

}
