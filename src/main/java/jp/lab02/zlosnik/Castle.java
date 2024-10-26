package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Castle {
    public final int number;
    private final double initialRadius;
    public double baseRadius;
    public double height;
    public double volume;
    public boolean complete;
    public List<List<Integer>> permutationsList;

    public Castle(int number, double radius) {
        this.number = number;
        this.initialRadius = radius;
        this.baseRadius = radius;
        this.height = 0;
        this.volume = 0;
        this.complete = false;
        this.permutationsList = new LinkedList<>();
    }

    private void addLayer(double volume, double angle) {
        if (!complete) {
            double tanTheta = Math.tan(Math.toRadians(angle));
            double layerHeight;
            final double PI = Math.PI;

            double topRadius = Math.cbrt((baseRadius * baseRadius * baseRadius) - ((12 * volume) / (PI * tanTheta)));

            if (topRadius < 0) { // Layer cannot fit fully. Making a triangle.
                complete = true;
                layerHeight = 3 * volume / (PI * (baseRadius * baseRadius));
                this.height += layerHeight;
                this.volume += (PI * baseRadius * baseRadius * layerHeight) / 3;
                baseRadius = 0;
            } else { // Adding full layer.
                layerHeight = 3 * volume / (PI * ((baseRadius * baseRadius) + (baseRadius * topRadius) + (topRadius * topRadius)));
                this.height += layerHeight;
                this.volume += volume;
                baseRadius = topRadius;
            }
        }
    }

    public void addLayerStack(List<Bucket> buckets, List<Integer> permutation) {
        for (Integer i : permutation) {
            Bucket bucket = buckets.get(i - 1);
            addLayer(Main.STEP, bucket.angle);
        }
    }

    @Override
    public String toString() {
        return String.format("Castle(Number: %d, Initial radius: %.2f, Current top radius: %.2f, Height: %.2f, Volume: %.3f, Complete: %b)", number, initialRadius, baseRadius, height, volume, complete);
    }

    public Castle getBlankCastle() {
        return new Castle(number, initialRadius);
    }

    // Method to generate all combinations of permutations from the castles
    public static List<List<List<Integer>>> generateAllCombinations(List<Castle> castles) {
        List<List<List<Integer>>> result = new ArrayList<>();
        generateCombinations(castles, 0, new ArrayList<>(), result);
        return result;
    }

    // Recursive helper to build combinations
    private static void generateCombinations(List<Castle> castles, int depth, List<List<Integer>> currentCombination, List<List<List<Integer>>> result) {
        // Base case: if we have selected a permutation from each castle
        if (depth == castles.size()) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        // Recursive case: iterate over all permutations in the current castle
        Castle currentCastle = castles.get(depth);
        for (List<Integer> permutation : currentCastle.permutationsList) {
            currentCombination.add(permutation);
            generateCombinations(castles, depth + 1, currentCombination, result);
            currentCombination.removeLast();  // backtrack
        }
    }
}