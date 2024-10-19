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

        System.out.println("Total possible permutations length: " + permutations.size());
        System.out.println("==============================");

        for (Castle castle : castleList) {
            castle.possiblePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList, STEP);
            System.out.println("Complete permutations length for castle " + castle.number + ": " + castle.possiblePermutationsList.size());
            System.out.println("==============================");
        }

        // List<List<List<Integer>>> permutationCombinations;
        /* A,B,C... = castles   1, 2... = permutations
        A1 A2...
        B1 B2 B3...
        C1 C2...
        ...
        A1B1C1 A1B1C2 A1B2C1 A1B2C2 A1B3C1 A1B3C2 A2B1C1 A2B1C2 A2B2C1 A2B2C2 A2B3C1 A2B3C2
         */
        Castle firstCastle = castleList.getFirst();
        for (List<Integer> permutation : firstCastle.possiblePermutationsList) {
            List<Bucket> newBucketList = dataReader.getBuckets(); // Get fresh bucket list
            System.out.println("Buckets before:\t" + newBucketList);
            System.out.println(permutation);
            System.out.println(PermutationBuilder.countOccurrences(permutation));
            System.out.println("Buckets after:\t" + newBucketList);
            System.out.println("==============================");
        }
    }
}
