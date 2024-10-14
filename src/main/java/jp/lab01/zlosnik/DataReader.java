package jp.lab01.zlosnik;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class DataReader {
    public static ArrayList<Castle> getCastles(File castlesFile) {
        ArrayList<Castle> castleList = new ArrayList<>();
        Scanner castleScanner;
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
        return castleList;
    }

    public static ArrayList<Bucket> getBuckets(File bucketsFile) {
        ArrayList<Bucket> bucketList = new ArrayList<>();
        Scanner bucketScanner;
        try {
            bucketScanner = new Scanner(bucketsFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int bucketNumber;
        double angle, volume;
        bucketScanner.nextLine();
        while (bucketScanner.hasNextLine()) {
            String[] parts = bucketScanner.nextLine().split(", ");
            bucketNumber = Integer.parseInt(parts[0]);
            angle = Double.parseDouble(parts[1]);
            volume = Double.parseDouble(parts[2]);
            bucketList.add(new Bucket(bucketNumber, angle, volume));
        }
        return bucketList;
    }

    public static WeightsCalculator getWeights(File weightsFile) {
        Scanner weightsScanner;
        try {
            weightsScanner = new Scanner(weightsFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String[] parts = weightsScanner.nextLine().split(", ");
        double w1 = Double.parseDouble(parts[0]);
        double w2 = Double.parseDouble(parts[1]);
        return new WeightsCalculator(w1, w2);
    }
}
