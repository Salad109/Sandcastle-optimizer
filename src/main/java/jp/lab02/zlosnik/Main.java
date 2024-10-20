package jp.lab02.zlosnik;


import jp.lab02.zlosnik.logic.*;

import java.util.*;

public class Main {
    public static final double STEP = 5;
    private static final boolean PRINT_DATA = true;
    private static final boolean PRINT_PERMUTATIONS = true;
    private static final boolean PRINT_COMBINATIONS = false; // true false

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
            System.out.println("Total possible permutations of sand layers:\t" + permutations.size());
            for (Castle castle : castleList) {
                castle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList);
                System.out.println("Complete layer permutations for castle " + castle.number + ":\t" + castle.completePermutationsList.size());
            }
            System.out.println("==============================");
        }


        Map<List<Integer>, List<List<Integer>>> wrappedPermutationCombinations = PermutationBuilder.getCombinations(castleList, dataReader);
        List<List<List<Integer>>> cleanCombinationList = PermutationBuilder.unwrapCombinations(wrappedPermutationCombinations, castleList, bucketList);
        System.out.printf("There are a total of %d possible combinations to build castles%n", cleanCombinationList.size());

        double score;
        double bestScore = 0;
        int index = 0;
        int bestIndex = 0;
        double bestHeight = 0;
        double bestLeftoverVolume = 0;
        Castle firstCastle = castleList.get(0);
        Castle secondCastle = castleList.get(1);
        for (List<List<Integer>> combination : cleanCombinationList) {
            firstCastle.addLayerStack(bucketList, combination.getFirst());
            secondCastle.addLayerStack(bucketList, combination.getLast());
            double leftoverVolume = dataReader.getTotalBucketVolume() - firstCastle.volume - secondCastle.volume;
            double avgHeight = (firstCastle.height + secondCastle.height) / 2;
            score = weightsCalculator.calculateScore(leftoverVolume, avgHeight);
            index++;
            if (PRINT_COMBINATIONS) {
                System.out.printf("%d\t| Leftover volume: %6.2f\t| Average height: %6.3f\t| Score: %8.5f\t| First layers: %s\t|\tSecond layers: %s%n", index, leftoverVolume, avgHeight, score, combination.getFirst(), combination.getLast());
            }
            if (score > bestScore) {
                bestScore = score;
                bestIndex = index - 1;
                bestHeight = avgHeight;
                bestLeftoverVolume = leftoverVolume;
            }

            firstCastle = firstCastle.getBlankCastle();
            secondCastle = secondCastle.getBlankCastle();
        }
        System.out.printf("The winner is combination %d with an average height of %.3f and leftover volume of %.2f", bestIndex, bestHeight, bestLeftoverVolume);
    }
}
