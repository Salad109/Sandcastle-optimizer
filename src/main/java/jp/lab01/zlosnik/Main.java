package jp.lab01.zlosnik;


import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        File castlesFile = new File("/home/mat/Desktop/Sandcastle-Maven/src/main/java/jp/lab01/zlosnik/miejsca.txt");
        File bucketsFile = new File("/home/mat/Desktop/Sandcastle-Maven/src/main/java/jp/lab01/zlosnik/wiaderka.txt");
        File weightsFile = new File("/home/mat/Desktop/Sandcastle-Maven/src/main/java/jp/lab01/zlosnik/wagi.txt");
        var reader = new DataReader(castlesFile, bucketsFile, weightsFile);

        ArrayList<Castle> castleList = reader.getCastles(castlesFile);
        ArrayList<Bucket> bucketList = reader.getBuckets(bucketsFile);
        WeightsCalculator weightsCalculator = reader.getWeights(weightsFile);

        System.out.println(castleList);
        System.out.println(bucketList);
        System.out.println(weightsCalculator);

        System.out.println(castleList.getFirst());
        castleList.getFirst().addSand(50, bucketList.getFirst().angle);
        System.out.println(castleList.getFirst());
        castleList.getFirst().addSand(50, bucketList.getFirst().angle);
        System.out.println(castleList.getFirst());
        castleList.getFirst().addSand(15, bucketList.getFirst().angle);
        System.out.println(castleList.getFirst());
        castleList.getFirst().addSand(15, bucketList.getFirst().angle);
        System.out.println(castleList.getFirst());

    }
}