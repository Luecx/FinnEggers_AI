package network.functions.error;

import network.layers.OutputLayer;

/**
 * Created by finne on 10.05.2018.
 */
public class CrossEntropy extends ErrorFunction {

    public CrossEntropy() {
    }

    @Override
    public double overall_error(OutputLayer outputLayer, double[][][] expected) {
        double val = 0;
        double c = 0;

        for (int i = 0; i < outputLayer.getOutput_values().length; i++) {
            for (int n = 0; n < outputLayer.getOutput_values()[0].length; n++) {
                for (int j = 0; j < outputLayer.getOutput_values()[0][0].length; j++) {
                    val += expected[i][n][j] * Math.log(outputLayer.getOutput_values()[i][n][j]) + (1 - expected[i][n][j]) * Math.log(1-outputLayer.getOutput_values()[i][n][j]) ;
                    c++;
                }
            }
        }

        return -(1 / c) * val;
    }

    @Override
    public void apply(OutputLayer outputLayer, double[][][] expected) {
        for (int i = 0; i < outputLayer.getOutput_values().length; i++) {
            for (int n = 0; n < outputLayer.getOutput_values()[0].length; n++) {
                for (int j = 0; j < outputLayer.getOutput_values()[0][0].length; j++) {
                    double v = outputLayer.getOutput_values()[i][n][j];
                    outputLayer.getOutput_error_values()[i][n][j] =
                            -(expected[i][n][j] - outputLayer.getOutput_values()[i][n][j]);
                }
            }
        }
    }
}
