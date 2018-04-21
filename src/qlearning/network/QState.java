package qlearning.network;

import qlearning.basic.vectors.Vector3i;

/**
 * Created by finne on 11.02.2018.
 */
public class QState {

    private double[][][] in;
    private Vector3i dimensions;

    public QState(double[][][] in) {
        this.in = in;
        this.dimensions = new Vector3i(in.length, in[0].length, in[0][0].length);
    }

    public double[][][] getIn() {
        return in;
    }

    public Vector3i getDimensions() {
        return dimensions;
    }
}
