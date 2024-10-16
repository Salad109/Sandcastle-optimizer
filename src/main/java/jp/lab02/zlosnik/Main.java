package jp.lab02.zlosnik;


import java.io.File;
import java.util.*;

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


        System.out.println(permutations.get(27));

        Map<Integer, Integer> occurrences = countOccurrences(permutations.get(27));

        for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            System.out.println("Number " + entry.getKey() + ": " + entry.getValue() + " times");
        }

        System.out.println(permutations);
        System.out.println("Length: " + permutations.size());

        /*
        Usuwanie wadliwych permutacji:
        Pobierz ilość każdej z cyfr
        if(ilość wystąpień cyfry x * STEP > volume wiaderka x)
            dodaj indeks tej permutacji do listy wadliwych

        przeiteruj po liście wadliwych usuwając pierw te z końca
         */
    }
        /*
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
        */

        /*
        Oblicz długość najdłuższej możliwej permutacji (volume wszystkich wiaderek / step)
        Wylicz wszystkie permutacje do tej długości
        Przeiteruj po nich wszystkich i zapisz najwyższą
        */


    private static Map<Integer, Integer> countOccurrences(List<Integer> numbers) {
        Map<Integer, Integer> occurrences = new HashMap<>();

        // Iterate through the list and count occurrences
        for (Integer number : numbers) {
            occurrences.put(number, occurrences.getOrDefault(number, 0) + 1);
        }

        return occurrences;
    }

}
