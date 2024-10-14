package jp.lab01.zlosnik;

public class Castle {
    public final int number;
    public final double initialRadius;
    private double baseRadius;
    private double height;
    private final double PI = Math.PI;
    private double volume;

    Castle(int number, double radius) {
        this.number = number;
        this.initialRadius = radius;
        this.baseRadius = radius;
        this.height = 0;
        volume = 0;
    }

    public void addSand(double volume, double angle) {
        angle = Math.toRadians(angle);
        double tanTheta = Math.tan(angle);

        double topBase = Math.cbrt((baseRadius * baseRadius * baseRadius) - ((12 * volume) / (PI * tanTheta)));
        height += 3 * volume / (PI * ((baseRadius * baseRadius) + (baseRadius * topBase) + (topBase * topBase)));
        baseRadius = topBase;
        this.volume = ((double) 1 / 3) * PI * height * ((initialRadius * initialRadius) + (initialRadius * topBase) + (topBase * topBase));
    }

    @Override
    public String toString() {
        return String.format("Castle(Number: %d, Initial radius: %f, Current top radius: %f, Height: %f, Volume: %f)", number, initialRadius, baseRadius, height, volume);
    }
}