package jp.lab02.zlosnik;


import jp.lab02.zlosnik.logic.*;

import java.util.*;

public class Main {
    private static final double STEP = 5;
    private static final boolean PRINT_DATA = true;
    private static final boolean PRINT_COMBINATIONS = true;

    public static void main(String[] args) {
        DataReader dataReader = new DataReader();
        dataReader.setCastlesPath("src/main/java/jp/lab02/zlosnik/data/miejsca.txt");
        dataReader.setBucketsPath("src/main/java/jp/lab02/zlosnik/data/wiaderka.txt");
        dataReader.setWeightsPath("src/main/java/jp/lab02/zlosnik/data/wagi.txt");

        List<Castle> castleList = dataReader.getCastles();
        List<Bucket> bucketList = dataReader.getBuckets();
        List<List<Integer>> permutations = PermutationBuilder.getPermutations(bucketList, STEP);
        WeightsCalculator weightsCalculator = dataReader.getWeights();
        if (PRINT_DATA) {
            System.out.println(castleList);
            System.out.println(bucketList);
            System.out.println(weightsCalculator);
        }


        System.out.println("==============================");
        System.out.println("Total possible permutations:\t\t" + permutations.size());
        for (Castle castle : castleList) {
            castle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList, STEP);
            System.out.println("Complete permutations for castle " + castle.number + ":\t" + castle.completePermutationsList.size());
        }
        System.out.println("==============================");


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
                    newBucketList.get(key - 1).volume -= value * STEP; // Subtract sand based on the occurrence
                }

                // Compute new permutations based on the updated bucket list
                permutations = PermutationBuilder.getPermutations(newBucketList, STEP);

                // Filter new permutations for the current castle
                List<List<Integer>> filteredPermutations = PermutationBuilder.getCompletePermutations(permutations, castle, newBucketList, STEP);

                // Store the new permutation combination for the current castle
                permutationCombinations.put(castlePermutation, filteredPermutations);
            }
        }

        System.out.println("There are " + permutationCombinations.size() + " combinations");
        PermutationBuilder.filterCombinations(permutationCombinations);
        System.out.println("There are " + permutationCombinations.size() + " possible combinations");
        if (PRINT_COMBINATIONS) PermutationBuilder.printCombinations(permutationCombinations);
    }
}
