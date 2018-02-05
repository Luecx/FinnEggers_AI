package qlearning.neuralnetwork;

/**
 * Created by finne on 04.02.2018.
 */
public class QState {

    private double[][][] data;
    private double reward;

    public QState(double[][][] data) {
        this.data = data;
    }

    public QState(double[][][] data, double reward) {
        this.data = data;
        this.reward = reward;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double[][][] getData() {
        return data;
    }

    public void setData(double[][][] data) {
        this.data = data;
    }
}
