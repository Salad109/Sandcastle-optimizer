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
        List<List<Integer>> permutations = Permutations.getPermutations(bucketList, STEP);
        WeightsCalculator weightsCalculator = DataReader.getWeights(weightsFile);

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);
        System.out.println("==============================");

        System.out.println("Total possible permutations length: " + permutations.size());
        System.out.println("Total possible permutations: " + permutations);
        System.out.println("==============================");

        List<List<List<Integer>>> permutationsList = new ArrayList<>(2);
        for (int i = 0; i < castleList.size(); i++) {
            permutationsList.add(getCompletePermutations(permutations, castleList.get(i), bucketList));
            System.out.println("Complete permutations length for castle " + castleList.get(i).number + ": " + permutationsList.get(i).size());
            System.out.println("Complete permutations for castle " + castleList.get(i).number + ": " + permutationsList.get(i));
            System.out.println("==============================");
        }
        if (permutationsList.get(0).equals(permutationsList.get(1)))
            System.out.println("Permutations are equal");
        else
            System.out.println("Permutations are not equal");
    }

    private static List<List<Integer>> getCompletePermutations(List<List<Integer>> permutations, Castle castle, ArrayList<Bucket> bucketList) {
        List<List<Integer>> completePermutations = new ArrayList<>();
        for (List<Integer> permutation : permutations) {
            castle = castle.getBlankCastle();
            castle.addLayerStack(bucketList, permutation, STEP);
            if (castle.complete) completePermutations.add(permutation);
        }
        return completePermutations;
    }
}
