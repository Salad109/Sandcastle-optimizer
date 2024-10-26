package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;
import jp.lab02.zlosnik.Main;

import java.util.*;

public abstract class PermutationBuilder {
    private PermutationBuilder() {
    }

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

    public static List<List<Integer>> getPermutationsForCastle(Castle castle, List<Bucket> bucketList) {
        List<List<Integer>> possiblePermutations = getPermutations(bucketList);
        List<List<Integer>> completePermutations = new ArrayList<>();
        for (List<Integer> permutation : possiblePermutations) {
            castle = castle.getBlankCastle();
            castle.addLayerStack(bucketList, permutation);
            if (castle.complete) completePermutations.add(permutation);
        }
        return completePermutations;
    }

    /*
     dostępny piasek = całkowity piasek
     dla każdej permutacji zamku x:
        jeśli zamek x to ostatni istniejący zamek:
            wrzuć wszystkie permutacje do kombinacji
            dodaj ten obiekt do listy kombinacji
        pobierz dostępny/pozostały piasek
        odejmij piasek równy permutacji zamku x
        oblicz możliwe permutacje z pozostałego piasku dla zamku x+1
        dla każdej permutacji zamku x+1:
            jeśli zamek x to ostatni istniejący zamek:
                wrzuć wszystkie permutacje do kombinacji
                dodaj ten obiekt do listy kombinacji
            pobierz dostępny/pozostały piasek
            odejmij piasek równy permutacji zamku x+1
            oblicz możliwe permutacje z pozostałego piasku dla zamku x+2
            dla każdej permutacji zamku x+2:
                jeśli zamek x to ostatni istniejący zamek:
                    wrzuć wszystkie permutacje do kombinacji
                    dodaj ten obiekt do listy kombinacji
                pobierz dostępny/pozostały piasek
                odejmij piasek równy permutacji zamku x+2
                oblicz możliwe permutacje z pozostałego piasku dla zamku x+3
     */
    public static List<List<List<Integer>>> getPermutationCombinations(List<Castle> castleList, List<Bucket> originalBucketList) {
        List<List<List<Integer>>> combinationList = new LinkedList<>();
        List<Bucket> bucketListCopy = Bucket.deepCopyList(originalBucketList);
        for (int i = 0; i < castleList.size(); i++) {
            List<List<Integer>> combination = new LinkedList<>();
            castleList.get(i).completePermutationsList = PermutationBuilder.getPermutationsForCastle(castleList.get(i), bucketListCopy);
            if (i == castleList.size() - 1) {
            }
        }


        List<List<List<Integer>>> cleanCombinationList = new LinkedList<>();
        for (List<List<Integer>> combination : combinationList) {
            for (List<Integer> permutation : combination) {
                if (!permutation.isEmpty() && !cleanCombinationList.contains(combination)) {
                    cleanCombinationList.add(combination);
                }
            }
        }
        return cleanCombinationList;
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

                List<List<Integer>> filteredPermutations = PermutationBuilder.getPermutationsForCastle(castle, newBucketList);

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
