package jp.lab02.zlosnik;


import jp.lab02.zlosnik.logic.DataReader;
import jp.lab02.zlosnik.logic.PermutationBuilder;
import jp.lab02.zlosnik.logic.WeightsCalculator;

import java.io.File;
import java.util.*;

public class Main {
    private final static double STEP = 4;

    public static void main(String[] args) {
        File castlesFile = new File("src/main/java/jp/lab02/zlosnik/data/miejsca.txt");
        File bucketsFile = new File("src/main/java/jp/lab02/zlosnik/data/wiaderka.txt");
        File weightsFile = new File("src/main/java/jp/lab02/zlosnik/data/wagi.txt");

        ArrayList<Castle> castleList = DataReader.getCastles(castlesFile);
        ArrayList<Bucket> bucketList = DataReader.getBuckets(bucketsFile);
        List<List<Integer>> permutations = PermutationBuilder.getPermutations(bucketList, STEP);
        WeightsCalculator weightsCalculator = DataReader.getWeights(weightsFile);

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
        System.out.println(castleList.getFirst().possiblePermutationsList);
    }
}
