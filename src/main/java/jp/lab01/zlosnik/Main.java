package jp.lab01.zlosnik;


import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        File castlesFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab01/zlosnik/miejsca.txt");
        File bucketsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab01/zlosnik/wiaderka.txt");
        File weightsFile = new File("/home/mat/Desktop/Sandcastle/src/main/java/jp/lab01/zlosnik/wagi.txt");

        ArrayList<Castle> castleList = DataReader.getCastles(castlesFile);
        ArrayList<Bucket> bucketList = DataReader.getBuckets(bucketsFile);
        WeightsCalculator weightsCalculator = DataReader.getWeights(weightsFile);

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);

        for (Bucket bucket : bucketList) {
            double A = bucket.angle;
            double step = 0.5;
            for (double V = bucket.volume; V > 0; V -= step)
                castleList.getFirst().addLayer(step, A);
        }
        castleList.getFirst().printLayers();
        System.out.println(castleList.getFirst());

        double x = 1;
    }
}