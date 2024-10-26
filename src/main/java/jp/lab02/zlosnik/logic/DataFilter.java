package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;
import jp.lab02.zlosnik.Main;

import java.util.*;

public abstract class DataFilter {
    private DataFilter() {
    }

    static List<List<Integer>> filterPossiblePermutations(List<Bucket> bucketList, List<List<Integer>> permutations) {
        List<List<Integer>> validPermutations = new ArrayList<>();

        for (List<Integer> permutation : permutations) {
            Map<Integer, Integer> occurrences = countOccurrences(permutation);

            boolean isValid = true;

            for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                int key = entry.getKey();
                int count = entry.getValue();

                if (count * Main.STEP > bucketList.get(key - 1).volume) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                validPermutations.add(permutation);
            }
        }

        return validPermutations;
    }

    private static Map<Integer, Integer> countOccurrences(List<Integer> permutation) {
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (Integer number : permutation) {
            occurrences.put(number, occurrences.getOrDefault(number, 0) + 1);
        }
        return occurrences;
    }

    private static Map<Integer, Integer> countOccurrencesInCombination(List<List<Integer>> combination) {
        List<Integer> permutation = new LinkedList<>();
        for (List<Integer> combinationElement : combination) {
            permutation.addAll(combinationElement);
        }
        Map<Integer, Integer> occurrences = new HashMap<>();
        for (Integer number : permutation) {
            occurrences.put(number, occurrences.getOrDefault(number, 0) + 1);
        }
        return occurrences;
    }

    public List<List<List<Integer>>> filterCombinations(List<Castle> castleList, List<Bucket> bucketList) {
        List<List<List<Integer>>> allCombinations = Castle.generateAllCombinations(castleList);
        List<List<List<Integer>>> validCombinations = new ArrayList<>();

        for (List<List<Integer>> combination : allCombinations) {
            Map<Integer, Integer> occurrences = countOccurrencesInCombination(combination);
            boolean isValid = true;

            for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                int key = entry.getKey();
                int count = entry.getValue();

                if (count * Main.STEP > bucketList.get(key - 1).volume) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                validCombinations.add(combination);
            }
        }

        return validCombinations;
    }
}
