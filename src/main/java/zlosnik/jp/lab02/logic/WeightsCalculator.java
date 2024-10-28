package zlosnik.jp.lab02.logic;

public record WeightsCalculator(double leftoverSandValue, double heightValue) {

    public double calculateScore(double k1, double k2) {
        return k1 * leftoverSandValue + k2 * heightValue;
    }

    @Override
    public String toString() {
        return "Weights[Leftover sand value: " + leftoverSandValue + ", height value: " + heightValue + "]";
    }
}
