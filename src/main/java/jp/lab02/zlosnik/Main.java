package jp.lab02.zlosnik;


import jp.lab02.zlosnik.logic.*;

import java.util.*;

public class Main {
    public static final double STEP = 5;
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
        List<List<Integer>> permutations = PermutationBuilder.getPermutations(bucketList);
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
                castle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList);
                System.out.println("Complete permutations for castle " + castle.number + ":\t" + castle.completePermutationsList.size());
            }
            System.out.println("==============================");
        }


        Map<List<Integer>, List<List<Integer>>> permutationCombinations = PermutationBuilder.getCombinations(castleList, dataReader);
        List<List<List<Integer>>> unwrappedCombinationList = new LinkedList<>();
        for (Map.Entry<List<Integer>, List<List<Integer>>> entry : permutationCombinations.entrySet()) {
            List<Integer> key = entry.getKey();
            List<List<Integer>> valueList = entry.getValue();

            for (List<Integer> value : valueList) {
                List<List<Integer>> unwrappedCombination = new LinkedList<>();
                unwrappedCombination.add(key);
                unwrappedCombination.add(value);
                unwrappedCombinationList.add(unwrappedCombination);
            }
        }
        Castle firstCastle = castleList.get(0);
        Castle secondCastle = castleList.get(1);
        double score;
        double bestScore = 0;
        int index = 0;
        int bestIndex = 0;
        for (List<List<Integer>> unwrappedCombination : unwrappedCombinationList) {
            firstCastle.addLayerStack(bucketList, unwrappedCombination.getFirst());
            secondCastle.addLayerStack(bucketList, unwrappedCombination.getLast());
            double leftoverVolume = dataReader.getTotalBucketVolume() - firstCastle.volume - secondCastle.volume;
            double avgHeight = (firstCastle.height + secondCastle.height) / 2;
            score = weightsCalculator.calculateScore(leftoverVolume, avgHeight);
            if (PRINT_COMBINATIONS) {
                System.out.printf("%d\t| Leftover volume: %6.2f\t| Average height: %6.3f\t| Score: %8.5f\t| First layers: %s\t|\tSecond layers: %s%n", index++, leftoverVolume, avgHeight, score, unwrappedCombination.getFirst(), unwrappedCombination.getLast());
                System.out.println("H1: " + firstCastle.height + " H2: " + secondCastle.height + "| R1: " + firstCastle.baseRadius + " R2: " + secondCastle.baseRadius);
            }
            if (score >= bestScore) {
                bestScore = score;
                bestIndex = index;
            }

            firstCastle = firstCastle.getBlankCastle();
            secondCastle = secondCastle.getBlankCastle();
        }
        bestIndex--;
        System.out.println("There are " + unwrappedCombinationList.size() + " possible permutation combinations");
        System.out.printf("The best combination was number %d", bestIndex);
    }
}
