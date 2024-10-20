package jp.lab02.zlosnik;


import jp.lab02.zlosnik.logic.*;

import java.util.*;

public class Main {
    private static final double STEP = 5;
    private static final boolean PRINT_DATA = true;
    private static final boolean PRINT_PERMUTATIONS = true;
    private static final boolean PRINT_COMBINATIONS = true; // true false

    public static void main(String[] args) {
        DataReader dataReader = new DataReader();
        dataReader.setCastlesPath("src/main/java/jp/lab02/zlosnik/data/miejsca.txt");
        dataReader.setBucketsPath("src/main/java/jp/lab02/zlosnik/data/wiaderka.txt");
        dataReader.setWeightsPath("src/main/java/jp/lab02/zlosnik/data/wagi.txt");

        List<Castle> castleList = dataReader.getCastles();
        List<Bucket> bucketList = dataReader.getBuckets();
        List<List<Integer>> permutations = PermutationBuilder.getPermutations(bucketList, STEP);
        WeightsCalculator weightsCalculator = dataReader.getWeights();
        if (PRINT_DATA) {
            for (Castle castle : castleList) {
                System.out.println(castle.toString());
            }
            for (Bucket bucket : bucketList) {
                System.out.println(bucket.toString());
            }
            System.out.println(weightsCalculator);
            System.out.println("==============================");
        }

        if (PRINT_PERMUTATIONS) {
            System.out.println("Total possible permutations:\t\t" + permutations.size());
            for (Castle castle : castleList) {
                castle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList, STEP);
                System.out.println("Complete permutations for castle " + castle.number + ":\t" + castle.completePermutationsList.size());
            }
            System.out.println("==============================");
        }


        Map<List<Integer>, List<List<Integer>>> permutationCombinations = PermutationBuilder.getCombinations(castleList, dataReader, STEP, permutations);
        List<List<List<Integer>>> unwrappedCombinationList = new LinkedList<>();

        for (Map.Entry<List<Integer>, List<List<Integer>>> entry : permutationCombinations.entrySet()) {
            List<Integer> key = entry.getKey();
            List<List<Integer>> valueList = entry.getValue();

            for (List<Integer> value : valueList) {
                List<List<Integer>> unwrappedCombination = new LinkedList<>();
                unwrappedCombination.add(key);
                unwrappedCombination.add(value);
                unwrappedCombinationList.add(unwrappedCombination);

                // Zbuduj zamek 1 z warstw "key"
                // Zbuduj zamek 2 z warstw "value"
                // Oblicz ich wspólne masy i max wysokość
            }
        }
        if (PRINT_COMBINATIONS) {
            int i = 0;
            for (List<List<Integer>> unwrappedCombination : unwrappedCombinationList) {
                System.out.println(i++ + " = " + unwrappedCombination);
            }
            System.out.println("There are " + unwrappedCombinationList.size() + " possible permutation combinations");
        }

    }
}
