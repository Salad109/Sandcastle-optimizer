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
            System.out.println("Total possible permutations of sand layers:\t" + permutations.size());
            for (Castle castle : castleList) {
                castle.completePermutationsList = PermutationBuilder.getCompletePermutations(permutations, castle, bucketList);
                System.out.println("Complete layer permutations for castle " + castle.number + ":\t" + castle.completePermutationsList.size());
            }
            System.out.println("==============================");
        }


        Map<List<Integer>, List<List<Integer>>> wrappedPermutationCombinations = PermutationBuilder.getCombinations(castleList, dataReader);
        List<List<List<Integer>>> cleanCombinationList = PermutationBuilder.unwrapCombinations(wrappedPermutationCombinations, castleList, bucketList);
        System.out.printf("There are a total of %d possible combinations to build %d castles%n", cleanCombinationList.size(), castleList.size());

        double score;
        double bestScore = 0;
        int index = 0;
        int bestIndex = 0;
        double bestHeight = 0;
        double bestLeftoverVolume = 0;

        for (List<List<Integer>> combination : cleanCombinationList) {
            double totalVolume = 0;
            double totalHeight = 0;

            for (int castleIndex = 0; castleIndex < castleList.size(); castleIndex++) {
                Castle castle = castleList.get(castleIndex);
                List<Integer> layers = combination.get(castleIndex);
                castle.addLayerStack(bucketList, layers);

                totalVolume += castle.volume;
                totalHeight += castle.height;
            }

            double leftoverVolume = dataReader.getTotalBucketVolume() - totalVolume;
            double avgHeight = totalHeight / castleList.size();

            score = weightsCalculator.calculateScore(leftoverVolume, avgHeight);
            index++;

            if (PRINT_COMBINATIONS) {
                System.out.printf("%d\t| Leftover volume: %6.2f\t| Average height: %6.3f\t| Score: %8.5f", index, leftoverVolume, avgHeight, score);
                for (int castleIndex = 0; castleIndex < castleList.size(); castleIndex++) {
                    System.out.printf("\t | Layers of castle %d: %s", castleList.get(castleIndex).number, combination.get(castleIndex));
                }
                System.out.println();
            }

            if (score > bestScore) {
                bestScore = score;
                bestIndex = index;
                bestHeight = avgHeight;
                bestLeftoverVolume = leftoverVolume;
            }

            castleList.replaceAll(Castle::getBlankCastle);
        }

        if (bestIndex == 0) {
            System.out.println("There is no winner...");
        } else {
            System.out.println("The winner:");
            System.out.printf("%d\t| Leftover volume: %6.2f\t| Average height: %6.3f\t| Score: %8.5f", bestIndex, bestLeftoverVolume, bestHeight, bestScore);
            for (int castleIndex = 0; castleIndex < castleList.size(); castleIndex++) {
                System.out.printf("\t | Layers of castle %d: %s", castleList.get(castleIndex).number, cleanCombinationList.get(bestIndex - 1).get(castleIndex));
            }
        }

    }
}
