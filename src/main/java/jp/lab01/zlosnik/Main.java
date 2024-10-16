package jp.lab01.zlosnik;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File castlesFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab01/zlosnik/miejsca.txt");
        File bucketsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab01/zlosnik/wiaderka.txt");
        File weightsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab01/zlosnik/wagi.txt");

        ArrayList<Castle> castleList = DataReader.getCastles(castlesFile);
        ArrayList<Bucket> bucketList = DataReader.getBuckets(bucketsFile);
        WeightsCalculator weightsCalculator = DataReader.getWeights(weightsFile);

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);

        int[] bucketIndexes = new int[bucketList.size()];
        for (int i = 0; i < bucketList.size(); i++) {
            bucketIndexes[i] = bucketList.get(i).number;
        }

        List<List<Integer>> permutations = Permutations.getPermutations(bucketIndexes);
        System.out.println(permutations);


        Castle firstCastle = castleList.getFirst();
        for (List<Integer> permutation : permutations) {
            firstCastle.addLayerStack(bucketList, permutation);
            firstCastle.printLayers();
            System.out.println(firstCastle);
            firstCastle.destroyCastle();
        }
        /*for (List<Integer> permutation : permutations) {
            addLayerStack(bucketList, castleList.getFirst(), permutation);
            castleList.getFirst().printLayers();
            castleList.getFirst().destroyCastle();
        }*/
    }
}
