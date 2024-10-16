package jp.lab02.zlosnik;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File castlesFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab02/zlosnik/miejsca.txt");
        File bucketsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab02/zlosnik/wiaderka.txt");
        File weightsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab02/zlosnik/wagi.txt");

        ArrayList<Castle> castleList = DataReader.getCastles(castlesFile);
        ArrayList<Bucket> bucketList = DataReader.getBuckets(bucketsFile);
        WeightsCalculator weightsCalculator = DataReader.getWeights(weightsFile);

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);
        System.out.println("==============================");

        List<List<Integer>> permutations = Permutations.getPermutations(bucketList);

        Castle firstCastle = castleList.getFirst();

        for (List<Integer> permutation : permutations) {
            System.out.println("Now building permutation: " + permutation);
            firstCastle.addLayerStack(bucketList, permutation);
            firstCastle.printLayers();
            System.out.println(firstCastle);
            firstCastle = firstCastle.destroyCastle();
        }
        /*
        Oblicz długość najdłuższej możliwej permutacji (volume wszystkich wiaderek / step)
        Wylicz wszystkie permutacje do tej długości
        Przeiteruj po nich wszystkich i zapisz najwyższą
        */

    }
}
