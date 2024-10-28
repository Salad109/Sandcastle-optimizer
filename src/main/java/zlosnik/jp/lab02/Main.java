package zlosnik.jp.lab02;


import zlosnik.jp.lab02.logic.*;
import zlosnik.jp.lab02.objects.*;

import java.util.*;

public class Main {
    public static final double STEP = 5;

    public static void main(String[] args) {
        DataReader dataReader = new DataReader();
        dataReader.setCastlesPath("miejsca.txt");
        dataReader.setBucketsPath("wiaderka.txt");
        dataReader.setWeightsPath("wagi.txt");

        List<Castle> castleList = dataReader.getCastles();
        List<Bucket> bucketList = dataReader.getBuckets();
        List<List<Integer>> totalPermutations = DataBuilder.getLayerPermutations(bucketList);
        WeightsCalculator weightsCalculator = dataReader.getWeights();

        printData(castleList, bucketList, weightsCalculator);

        printPermutations(totalPermutations, castleList, bucketList);

        List<List<List<Integer>>> allCombinations = Castle.generateAllCombinations(castleList);
        List<List<List<Integer>>> cleanCombinationList = DataFilter.filterCombinations(allCombinations, bucketList);
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

            printAttempt(index, leftoverVolume, avgHeight, score);
            for (int castleIndex = 0; castleIndex < castleList.size(); castleIndex++) {
                printCombination(castleList.get(castleIndex).number, combination.get(castleIndex));
            }
            System.out.println();

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
            printAttempt(bestIndex, bestLeftoverVolume, bestHeight, bestScore);
            for (int castleIndex = 0; castleIndex < castleList.size(); castleIndex++) {
                printCombination(castleList.get(castleIndex).number, cleanCombinationList.get(bestIndex - 1).get(castleIndex));
            }
        }

    }

    public static void printData(List<Castle> castleList, List<Bucket> bucketList, WeightsCalculator weightsCalculator) {
        for (Castle castle : castleList) {
            System.out.println(castle.toString());
        }
        for (Bucket bucket : bucketList) {
            System.out.println(bucket.toString());
        }
        System.out.println(weightsCalculator);
        System.out.println("==============================");
    }

    public static void printPermutations(List<List<Integer>> totalPermutations, List<Castle> castleList, List<Bucket> bucketList) {
        System.out.println("Total theoretical permutations of sand layers:\t" + totalPermutations.size());
        for (Castle castle : castleList) {
            castle.permutationsList = DataBuilder.getPermutationsForCastle(castle, bucketList, totalPermutations);
            System.out.println("Complete layer permutations for castle " + castle.number + ":\t" + castle.permutationsList.size());
        }
        System.out.println("==============================");
    }

    public static void printAttempt(int index, double leftoverVolume, double height, double score) {
        System.out.printf("%d\t| Leftover volume: %6.2f\t| Average height: %6.3f\t| Score: %8.5f", index, leftoverVolume, height, score);
    }

    public static void printCombination(int number, List<Integer> combination) {
        System.out.printf("\t | Layers of castle %d: %s", number, combination);
    }
}
