package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Castle {
    private static class Layer {
        double volume;
        double angle;

        Layer(double volume, double angle) {
            this.volume = volume;
            this.angle = angle;
        }

        @Override
        public String toString() {
            return String.format("Layer(volume=%f, angle=%f)", volume, angle);
        }
    }

    private final LinkedList<Layer> layers;
    public final int number;
    public final double initialRadius;
    private double baseRadius;
    private double height;
    private double volume;

    Castle(int number, double radius) {
        this.number = number;
        this.initialRadius = radius;
        baseRadius = radius;
        height = 0;
        volume = 0;
        layers = new LinkedList<>();
    }

    public void addLayer(double volume, double angle) {
        layers.add(new Layer(volume, angle));
        double tanTheta = Math.tan(Math.toRadians(layers.getLast().angle));

        double topBase = Math.cbrt((baseRadius * baseRadius * baseRadius) - ((12 * layers.getLast().volume) / (Math.PI * tanTheta)));

        if (topBase < 0) {
            throw new IllegalArgumentException("Top base must not be negative");
        }
        this.height += 3 * layers.getLast().volume / (Math.PI * ((baseRadius * baseRadius) + (baseRadius * topBase) + (topBase * topBase)));
        this.volume += layers.getLast().volume;
        baseRadius = topBase;
    }

    public void addLayerStack(ArrayList<Bucket> buckets, List<Integer> permutation) {
        for (Integer i : permutation) {
            Bucket bucket = buckets.get(i - 1);
            addLayer(bucket.volume, bucket.angle);
        }
    }

    public void printLayers() {
        System.out.printf("Layers of castle %d, top to bottom:\n", number);
        int j = layers.size() - 1;
        for (int i = layers.size() - 1; i >= 0; i--) {
            System.out.println(j-- + ": " + layers.get(i).toString());
        }
    }

    @Override
    public String toString() {
        return String.format("Castle(Number: %d, Initial radius: %f, Current top radius: %f, Height: %f, Volume: %f, Layer count: %d)", number, initialRadius, baseRadius, height, volume, layers.size());
    }

    public Castle destroyCastle() {
        return new Castle(number, initialRadius);
    }
}