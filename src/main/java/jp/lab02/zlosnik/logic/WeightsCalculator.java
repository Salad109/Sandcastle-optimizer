package jp.lab02.zlosnik.logic;

public class WeightsCalculator {
    public final double w1, w2;

    public WeightsCalculator(double w1, double w2) {
        this.w1 = w1;
        this.w2 = w2;
    }

    public double calculateScore(double k1, double k2){
        return k1 * w1 + k2 * w2;
    }
    @Override
    public String toString() {
        return "WeightsCalculator[" + w1 + ", " + w2 + "]";
    }
}
