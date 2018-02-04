package qlearning.neuralnetwork;

/**
 * Created by finne on 04.02.2018.
 */
public class QState {

    private double[][][] data;

    public QState(double[][][] data) {
        this.data = data;
    }

    public double[][][] getData() {
        return data;
    }

    public void setData(double[][][] data) {
        this.data = data;
    }
}
