package jp.lab02.zlosnik;

import java.util.ArrayList;
import java.util.List;

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

    public Bucket clone() {
        return new Bucket(this.number, this.angle, this.volume);
    }

    public static List<Bucket> deepCopyList(List<Bucket> buckets) {
        List<Bucket> copiedList = new ArrayList<>(buckets.size());
        for (Bucket bucket : buckets) {
            copiedList.add(bucket.clone());
        }
        return copiedList;
    }

}
