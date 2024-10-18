package jp.lab02.zlosnik;


import java.io.File;
import java.util.*;

public class Main {
    private final static double STEP = 5;

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
        System.out.println("Permutations length: " + permutations.size());
        System.out.println("Permutations: " + permutations);
        System.out.println("==============================");

        Castle castle = castleList.getFirst().getBlankCastle();
        castle.addLayerStack(bucketList, permutations.getLast(), STEP);
        castle.printLayers();
        System.out.println(castle);

        /*permutations = getCompletePermutations(permutations, castleList.getFirst(), bucketList);

        System.out.println("==============================");
        System.out.println("Complete permutations length: " + permutations.size());
        System.out.println("Complete permutations: " + permutations);
        System.out.println("==============================");*/

    }
    private static List<List<Integer>> getCompletePermutations(List<List<Integer>> permutations, Castle castle, ArrayList<Bucket> bucketList) {
        List<List<Integer>> completePermutations = new ArrayList<>();
        for(List<Integer> permutation : permutations) {
            castle = castle.getBlankCastle();
            castle.addLayerStack(bucketList, permutation, STEP);
            if(castle.complete)
                completePermutations.add(permutation);
        }
        return completePermutations;
    }
}
