package network.functions.error;

import network.layers.OutputLayer;

/**
 * Created by finne on 05.02.2018.
 */
public class MSESingleError1D extends ErrorFunction {

    public int error_index;
    public double expected_output;

    public MSESingleError1D(int error_index, double expected_output) {
        this.error_index = error_index;
        this.expected_output = expected_output;
    }

    public MSESingleError1D() {
    }

    @Override
    public double overall_error(OutputLayer outputLayer, double[][][] expected) {
        return 0.5 * (outputLayer.getOutput_values()[0][0][error_index] - expected_output) * (outputLayer.getOutput_values()[0][0][error_index] - expected_output);
    }

    @Override
    public void apply(OutputLayer outputLayer, double[][][] expected) {
        outputLayer.getOutput_error_values()[0][0][error_index] = outputLayer.getOutput_values()[0][0][error_index] - expected_output;
    }

    public int getError_index() {
        return error_index;
    }

    public void setError_index(int error_index) {
        this.error_index = error_index;
    }

    public double getExpected_output() {
        return expected_output;
    }

    public void setExpected_output(double expected_output) {
        this.expected_output = expected_output;
    }
}
