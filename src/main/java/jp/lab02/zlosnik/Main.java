package jp.lab02.zlosnik;


import java.io.File;
import java.util.*;

public class Main {
    private final static double STEP = 4;

    public static void main(String[] args) {
        File castlesFile = new File("src/main/java/jp/lab02/zlosnik/miejsca.txt");
        File bucketsFile = new File("src/main/java/jp/lab02/zlosnik/wiaderka.txt");
        File weightsFile = new File("src/main/java/jp/lab02/zlosnik/wagi.txt");

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

    }
}
