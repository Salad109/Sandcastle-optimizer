package jp.lab01.zlosnik;

import java.util.LinkedList;

public class Castle {
    private class Layer {
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

    private LinkedList<Layer> layers;
    public final int number;
    public final double initialRadius;
    private double baseRadius;
    private double height;
    private double volume;

    Castle(int number, double radius) {
        this.number = number;
        this.initialRadius = radius;
        this.baseRadius = radius;
        this.height = 0;
        volume = 0;
        layers = new LinkedList<>();
    }

    public void addLayer(double volume, double angle) {
        layers.add(new Layer(volume, angle));
        angle = Math.toRadians(angle);
        double tanTheta = Math.tan(angle);

        double PI = Math.PI;
        double topBase = Math.cbrt((baseRadius * baseRadius * baseRadius) - ((12 * volume) / (PI * tanTheta)));
        if (topBase < 0) {
            throw new IllegalArgumentException("Top base must not be negative");
        }
        height += 3 * volume / (PI * ((baseRadius * baseRadius) + (baseRadius * topBase) + (topBase * topBase)));
        baseRadius = topBase;
        this.volume = ((double) 1 / 3) * PI * height * ((initialRadius * initialRadius) + (initialRadius * topBase) + (topBase * topBase));
    }

    public void printLayers() {
        System.out.printf("Layers of castle %d, top to bottom:\n", number);
        int j = layers.size() - 1;
        for (int i = layers.size() - 1; i >= 0; i--) {
            System.out.println(j-- + ": " + layers.get(i).toString());
        }
    }

    public boolean isComplete() {
        return baseRadius <= 0;
    }

    @Override
    public String toString() {
        return String.format("Castle(Number: %d, Initial radius: %f, Current top radius: %f, Height: %f, Volume: %f, Layer count: %d)", number, initialRadius, baseRadius, height, volume, layers.size());
    }
}