package jp.lab02.zlosnik;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private final static double STEP = 5;

    public static void main(String[] args) {

        File castlesFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab02/zlosnik/miejsca.txt");
        File bucketsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab02/zlosnik/wiaderka.txt");
        File weightsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab02/zlosnik/wagi.txt");

        ArrayList<Castle> castleList = DataReader.getCastles(castlesFile);
        ArrayList<Bucket> bucketList = DataReader.getBuckets(bucketsFile);
        List<List<Integer>> permutations = Permutations.getPermutations(bucketList, STEP);
        WeightsCalculator weightsCalculator = DataReader.getWeights(weightsFile);

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);
        System.out.println("==============================");

        System.out.println(permutations);
        System.out.println("Length: " + permutations.size());

        Castle firstCastle = castleList.getFirst();


        double maxHeight = 0;
        List<Castle.Layer> layers = new LinkedList<>();
        for (List<Integer> permutation : permutations) {
            System.out.println("Now building permutation: " + permutation);
            firstCastle.addLayerStack(bucketList, permutation);
            if (firstCastle.baseRadius < 0) continue; // Ignore impossible towers
            if (firstCastle.height > maxHeight) {
                maxHeight = firstCastle.height;
                layers = firstCastle.layers;
            }
            firstCastle = firstCastle.destroyCastle();
        }
        System.out.println("Highest castle height: " + firstCastle.height);
        System.out.println("Highest castle layers: " + layers);

        /*
        Oblicz długość najdłuższej możliwej permutacji (volume wszystkich wiaderek / step)
        Wylicz wszystkie permutacje do tej długości
        Przeiteruj po nich wszystkich i zapisz najwyższą
        */

    }
}
