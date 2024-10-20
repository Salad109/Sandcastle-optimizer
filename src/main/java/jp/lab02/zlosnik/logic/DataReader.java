package jp.lab02.zlosnik.logic;

import jp.lab02.zlosnik.Bucket;
import jp.lab02.zlosnik.Castle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataReader {
    private File castlesFile;
    private File bucketsFile;
    private File weightsFile;
    private List<Bucket> buckets;

    public void setCastlesPath(String castlesPathname) {
        castlesFile = new File(castlesPathname);
    }

    public void setBucketsPath(String bucketsPathname) {
        bucketsFile = new File(bucketsPathname);
        buckets = getBuckets();
    }

    public void setWeightsPath(String weightsPathname) {
        weightsFile = new File(weightsPathname);
    }

    public double getTotalBucketVolume() {
        double volume = 0;
        for (Bucket bucket : buckets) {
            volume += bucket.volume;
        }
        return volume;
    }

    public List<Castle> getCastles() {
        Scanner castleScanner;
        List<Castle> castleList = new ArrayList<>();
        try {
            castleScanner = new Scanner(castlesFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int castleNumber;
        double radius;
        castleScanner.nextLine();
        while (castleScanner.hasNextLine()) {
            String[] parts = castleScanner.nextLine().split(", ");
            castleNumber = Integer.parseInt(parts[0]);
            radius = Double.parseDouble(parts[1]);
            castleList.add(new Castle(castleNumber, radius));
        }
        castleScanner.close();
        return castleList;
    }

    public List<Bucket> getBuckets() {
        Scanner bucketScanner;
        List<Bucket> bucketList = new ArrayList<>();
        try {
            bucketScanner = new Scanner(bucketsFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int bucketNumber;
        double angle;
        double volume;
        bucketScanner.nextLine();
        while (bucketScanner.hasNextLine()) {
            String[] parts = bucketScanner.nextLine().split(", ");
            bucketNumber = Integer.parseInt(parts[0]);
            angle = Double.parseDouble(parts[1]);
            volume = Double.parseDouble(parts[2]);
            bucketList.add(new Bucket(bucketNumber, angle, volume));
        }
        bucketScanner.close();
        return bucketList;
    }

    public WeightsCalculator getWeights() {
        Scanner weightsScanner;
        try {
            weightsScanner = new Scanner(weightsFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        weightsScanner.nextLine();
        String[] parts = weightsScanner.nextLine().split(", ");
        weightsScanner.close();
        double w1 = Double.parseDouble(parts[0]);
        double w2 = Double.parseDouble(parts[1]);
        return new WeightsCalculator(w1, w2);
    }
}
