package jp.lab02.zlosnik;

public class Bucket implements Cloneable {
    public final int number;
    public final double angle;
    public double volume;

    public Bucket(int number, double angle, double volume) {
        this.number = number;
        this.angle = angle;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return String.format("Bucket[Number: %d, Angle: %.1f, Volume: %.3f]", number, angle, volume);
    }

    @Override
    public Bucket clone() {
        try {
            return (Bucket) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
