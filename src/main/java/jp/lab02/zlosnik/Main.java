package jp.lab02.zlosnik;


import jp.lab02.zlosnik.logic.*;

import java.util.*;

public class Main {
    private static final double STEP = 5;

    public static void main(String[] args) {
        DataReader dataReader = new DataReader();
        dataReader.setCastlesPath("src/main/java/jp/lab02/zlosnik/data/miejsca.txt");
        dataReader.setBucketsPath("src/main/java/jp/lab02/zlosnik/data/wiaderka.txt");
        dataReader.setWeightsPath("src/main/java/jp/lab02/zlosnik/data/wagi.txt");

        List<Castle> castleList = dataReader.getCastles();
        List<Bucket> bucketList = dataReader.getBuckets();
        List<List<Integer>> permutations = PermutationBuilder.getPermutations(bucketList, STEP);
        WeightsCalculator weightsCalculator = dataReader.getWeights();

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);
        System.out.println("==============================");

        System.out.println("Total possible permutations:\t\t" + permutations.size());

        for (Castle castle : castleList) {
            castle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList, STEP);
            System.out.println("Complete permutations for castle " + castle.number + ":\t" + castle.completePermutationsList.size());
        }
        System.out.println("==============================");


        Castle firstCastle = castleList.getFirst();
        Castle secondCastle = castleList.get(1);
        Map<List<Integer>, List<List<Integer>>> permutationCombinations = new HashMap<>();
        for (List<Integer> firstCastlePermutation : firstCastle.completePermutationsList) {
            List<Bucket> newBucketList = dataReader.getBuckets(); // Get fresh bucket list

            Map<Integer, Integer> occurrences = PermutationBuilder.countOccurrences(firstCastlePermutation);
            for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                newBucketList.get(key - 1).volume -= value * STEP;
            }

            permutations = PermutationBuilder.getPermutations(newBucketList, STEP);
            secondCastle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, secondCastle, newBucketList, STEP);
            permutationCombinations.put(firstCastlePermutation, secondCastle.completePermutationsList);
        }

        System.out.println("There are " + permutationCombinations.size() + " combinations");
        PermutationBuilder.filterCombinations(permutationCombinations);
        System.out.println("There are " + permutationCombinations.size() + " possible combinations");
        PermutationBuilder.printCombinations(permutationCombinations);
    }
}
