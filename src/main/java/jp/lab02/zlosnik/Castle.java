package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Castle {
    public static class Layer {
        final double volume;
        final double angle;
        final double height;
        final double bottomWidth;
        final double topWidth;

        Layer(double volume, double angle, double height, double bottomWidth, double topWidth) {
            this.volume = volume;
            this.angle = angle;
            this.height = height;
            this.bottomWidth = bottomWidth;
            this.topWidth = topWidth;
        }

        @Override
        public String toString() {
            return String.format("Layer(volume=%f, angle=%f, height=%f, bottom width=%f, top width=%f)",
                    volume, angle, height, bottomWidth, topWidth);
        }
    }

    public final LinkedList<Layer> layers;
    public final int number;
    public final double initialRadius;
    public double baseRadius;
    public double height;
    public double volume;
    public boolean complete;

    Castle(int number, double radius) {
        this.number = number;
        this.initialRadius = radius;
        baseRadius = radius;
        height = 0;
        volume = 0;
        layers = new LinkedList<>();
        complete = false;
    }

    public void addLayer(double volume, double angle) {
        if (!complete) {
            double tanTheta = Math.tan(Math.toRadians(angle));
            double height;
            final double PI = Math.PI;

            double topBase = Math.cbrt((baseRadius * baseRadius * baseRadius) - ((12 * volume) / (PI * tanTheta)));
            System.out.println("TOP BASE: " + topBase);

            if (topBase < 0) { // Layer cannot fit fully. Making a triangle.
                complete = true;
                height = baseRadius * tanTheta;
                this.height += height;
                this.volume += (PI * baseRadius * baseRadius * height) / 3;
                layers.add(new Layer((PI * baseRadius * baseRadius * height) / 3, angle, height, baseRadius, 0));
                baseRadius = 0;
            } else { // Adding full layer.
                height = 3 * volume / (PI * ((baseRadius * baseRadius) + (baseRadius * topBase) + (topBase * topBase)));
                this.height += height;
                this.volume += volume;
                layers.add(new Layer(volume, angle, height, baseRadius, topBase));
                baseRadius = topBase;
            }
        }
    }

    public void addLayerStack(ArrayList<Bucket> buckets, List<Integer> permutation, double STEP) {
        for (Integer i : permutation) {
            Bucket bucket = buckets.get(i - 1);
            addLayer(STEP, bucket.angle);
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
        return String.format("Castle(Number: %d, Initial radius: %f, Current top radius: %f, Height: %f, Volume: %f, Layer count: %d, Complete: %b)",
                number, initialRadius, baseRadius, height, volume, layers.size(), complete);
    }

    public Castle getBlankCastle() {
        return new Castle(number, initialRadius);
    }
}