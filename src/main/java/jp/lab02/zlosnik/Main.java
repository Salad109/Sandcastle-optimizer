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
        System.out.println("Permutations: " + permutations);
        System.out.println("Length: " + permutations.size());

        List<Integer> faultyPermutationIndexes = new LinkedList<>();
        Map<Integer, Integer> occurrences;

        for (int i = 0; i < permutations.size(); i++) {
            occurrences = countOccurrences(permutations.get(i));
            for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                if (entry.getValue() * STEP > bucketList.get(entry.getKey() - 1).volume)
                    faultyPermutationIndexes.add(i);
            }
        }


        faultyPermutationIndexes.sort(Collections.reverseOrder());
        permutations.sort(Collections.reverseOrder()); // TODO XD
        for (int index : faultyPermutationIndexes) {
            if (index >= 0 && index < faultyPermutationIndexes.size()) {
                permutations.remove(index);
            }
        }
        permutations.sort(Collections.reverseOrder());


        System.out.println("==============================");
        System.out.println("Faulty Permutation Indexes: " + faultyPermutationIndexes);
        System.out.println("Faulty permutations: " + permutations);
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


        System.out.println(permutations.getLast());
    }

    private static Map<Integer, Integer> countOccurrences(List<Integer> numbers) {
        Map<Integer, Integer> occurrences = new HashMap<>();

        // Iterate through the list and count occurrences
        for (Integer number : numbers) {
            occurrences.put(number, occurrences.getOrDefault(number, 0) + 1);
        }

        return occurrences;
    }

}
