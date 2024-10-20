package jp.lab02.zlosnik.logic;

public record WeightsCalculator(double w1, double w2) {

    public double calculateScore(double k1, double k2) {
        return k1 * w1 + k2 * w2;
    }

    @Override
    public String toString() {
        return "Weights[" + w1 + ", " + w2 + "]";
    }
}
