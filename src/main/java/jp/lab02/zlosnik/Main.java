package jp.lab02.zlosnik;


import java.io.File;
import java.util.*;

public class Main {
    private final static double STEP = 2.75;

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

        List<List<List<Integer>>> permutationsList = new ArrayList<>(2);
        for (int i = 0; i < castleList.size(); i++) {
            permutationsList.add(PermutationBuilder.getCompletePermutations(permutations, castleList.get(i), bucketList, STEP));
            System.out.println("Complete permutations length for castle " + castleList.get(i).number + ": " + permutationsList.get(i).size());
            System.out.println("==============================");
        }

        castleList.getFirst().addLayerStack(bucketList, permutationsList.getFirst().get(4), STEP);
        castleList.getFirst().printLayers();
        System.out.println(castleList.getFirst());
    }

}
