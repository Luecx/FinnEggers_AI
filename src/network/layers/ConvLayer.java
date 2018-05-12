package network.layers;

import network.Network;
import network.NetworkBuilder;
import network.functions.activation.ActivationFunction;
import network.functions.activation.ReLU;
import network.tools.ArrayTools;

/**
 * Created by finne on 04.02.2018.
 */
public class ConvLayer extends Layer {


    private int channel_amount;
    private int filter_size;
    private int filter_Stride;
    private int padding;

    private double[][][][] filter;
    private double[] bias;

    public ConvLayer(int channel_amount, int filter_size, int filter_Stride, int padding) {
        this.channel_amount = channel_amount;
        this.filter_size = filter_size;
        this.filter_Stride = filter_Stride;
        this.padding = padding;
    }

    private double lowerWeightsRange = 0, upperWeigthsRange = 1;
    private double lowerBiasRange = 0, upperBiasRange = 1;

    private ActivationFunction activationFunction = new ReLU();

    public ConvLayer weightsRange(double lower, double upper) {
        this.lowerWeightsRange = lower;
        this.upperWeigthsRange = upper;
        return this;
    }

    public ConvLayer biasRange(double lower, double upper) {
        this.lowerBiasRange = lower;
        this.upperBiasRange = upper;
        return this;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public ConvLayer setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        return this;
    }

    public ConvLayer setFilter(int index, double[][][] filter, double bias) {
        this.bias[index] = bias;
        this.filter[index] = filter;
        return this;
    }

    public double[][][] getFilter(int index) {
        return filter[index];
    }

    @Override
    protected void on_build() throws Exception {
        filter = ArrayTools.createRandomArray(channel_amount, this.getINPUT_DEPTH(), filter_size, filter_size, lowerWeightsRange, upperWeigthsRange);
        bias = ArrayTools.createRandomArray(channel_amount, lowerBiasRange, upperBiasRange);
    }

    @Override
    protected void calculateOutputDimensions() throws Exception {

        this.OUTPUT_DEPTH = channel_amount;
        this.OUTPUT_WIDTH = (this.INPUT_WIDTH + this.padding * 2 - filter_size) / filter_Stride + 1;
        this.OUTPUT_HEIGHT = (this.INPUT_HEIGHT + this.padding * 2 - filter_size) / filter_Stride + 1;

        double g = ((double) this.INPUT_WIDTH + (double) this.padding * 2 - (double) filter_size) / (double) filter_Stride + 1;
        double g1 = ((double) this.INPUT_HEIGHT + (double) this.padding * 2 - (double) filter_size) / (double) filter_Stride + 1;

        if (g != (int) g || g1 != (int) g1) throw new Exception("Format does not work! Use a different padding-value!");

    }

    public double getWeight(int channel, int depth, int x, int y) {
        return this.filter[channel][depth][x][y];
    }

    public void increaseWeight(int channel, int depth, int x, int y, double val) {
        this.filter[channel][depth][x][y] += val;
    }

    @Override
    public void calculate() {
        for (int i = 0; i < this.OUTPUT_DEPTH; i++) {
            for (int j = 0; j < this.OUTPUT_WIDTH; j++) {
                for (int n = 0; n < this.OUTPUT_HEIGHT; n++) {
                    this.output_values[i][j][n] = this.calcSample(i, j, n);
                    this.output_derivative_values[i][j][n] = this.output_values[i][j][n];
                }
            }
        }
        this.activationFunction.apply(this.output_values, this.output_derivative_values);
    }

    public double calcSample(int actIndex, int x, int y) {
        double total = bias[actIndex];
        for (int j = 0; j < getINPUT_DEPTH(); j++) {
            for (int i = 0; i < filter_size; i++) {
                for (int n = 0; n < filter_size; n++) {
                    int x_i = -padding + (x * filter_Stride) + i;
                    int y_i = -padding + (y * filter_Stride) + n;
                    if (x_i >= 0 && y_i >= 0 && x_i < getINPUT_WIDTH() && y_i < getINPUT_HEIGHT()) {
                        total += getWeight(actIndex, j, i, n) *
                                getInput_values()[j][x_i][y_i];
                    }
                }
            }
        }
        return total;
    }


    @Override
    public void backprop_error() {

        for (int j = 0; j < getINPUT_DEPTH(); j++) {
            for (int i = 0; i < getINPUT_WIDTH(); i++) {
                for (int n = 0; n < getINPUT_HEIGHT(); n++) {
                    this.getPrev_layer().getOutput_error_values()[j][i][n] = 0;
                }
            }
        }

        for (int output_d = 0; output_d < this.OUTPUT_DEPTH; output_d++) {
            for (int output_w = 0; output_w < this.OUTPUT_WIDTH; output_w++) {
                for (int output_h = 0; output_h < this.OUTPUT_HEIGHT; output_h++) {

                    for (int j = 0; j < getINPUT_DEPTH(); j++) {
                        for (int i = 0; i < filter_size; i++) {
                            for (int n = 0; n < filter_size; n++) {

                                int x_i = -padding + (output_w * filter_Stride) + i;
                                int y_i = -padding + (output_h * filter_Stride) + n;
                                if (x_i >= 0 && y_i >= 0 && x_i < getINPUT_WIDTH() && y_i < getINPUT_HEIGHT()) {
                                    this.getPrev_layer().output_error_values[j][x_i][y_i] +=
                                            getWeight(output_d, j, i, n) * output_error_values[output_d][output_h][output_h]
                                                    * getPrev_layer().output_derivative_values[j][i][n];
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void update_weights(double eta) {
        for (int output_d = 0; output_d < this.OUTPUT_DEPTH; output_d++) {

            for (int output_w = 0; output_w < this.OUTPUT_WIDTH; output_w++) {
                for (int output_h = 0; output_h < this.OUTPUT_HEIGHT; output_h++) {

                    bias[output_d] += -getOutput_error_values()[output_d][output_w][output_h];

                    for (int j = 0; j < getINPUT_DEPTH(); j++) {
                        for (int i = 0; i < filter_size; i++) {
                            for (int n = 0; n < filter_size; n++) {

                                int x_i = -padding + (output_w * filter_Stride) + i;
                                int y_i = -padding + (output_h * filter_Stride) + n;
                                if (x_i >= 0 && y_i >= 0 && x_i < getINPUT_WIDTH() && y_i < getINPUT_HEIGHT()) {
                                    increaseWeight(output_d, j, i, n,
                                            -getOutput_error_values()[output_d][output_w][output_h] *
                                                    getWeight(output_d, j, i, n) *
                                                    eta);

                                }
                            }
                        }
                    }


                }
            }
        }
    }

    public static void main(String[] args) {
        NetworkBuilder builder = new NetworkBuilder(3, 5, 5);
        ConvLayer convLayer = new ConvLayer(2, 3, 2, 1);
        builder.addLayer(convLayer);

        double[][][] input = ArrayTools.flipWidthAndHeight(new double[][][]
                {
                        {
                                {0, 1, 2, 2, 1},
                                {1, 1, 0, 1, 1},
                                {1, 1, 0, 0, 2},
                                {0, 2, 1, 0, 2},
                                {1, 0, 0, 0, 2}},
                        {
                                {2, 0, 1, 1, 2},
                                {1, 0, 2, 2, 1},
                                {1, 1, 2, 2, 1},
                                {0, 1, 1, 1, 0},
                                {0, 0, 1, 1, 2}},

                        {
                                {1, 1, 0, 2, 2},
                                {2, 2, 1, 1, 0},
                                {0, 0, 0, 1, 1},
                                {0, 0, 2, 0, 2},
                                {2, 0, 1, 1, 0}
                        }
                });


        double[][][] filter1 = ArrayTools.flipWidthAndHeight(new double[][][]
                {
                        {
                                {1, -1, -1},
                                {1, 0, 1},
                                {-1, -1, 1}},
                        {
                                {0, 0, -1},
                                {1, 1, 0},
                                {-1, 0, 0}},

                        {
                                {-1, -1, 1},
                                {-1, -1, 1},
                                {-1, 1, 0}
                        }
                });
        double[][][] filter2 = ArrayTools.flipWidthAndHeight(new double[][][]
                {
                        {
                                {1, 0, 1},
                                {0, -1, 0},
                                {0, -1, -1}},
                        {
                                {-1, 1, 1},
                                {-1, 1, 0},
                                {-1, -1, 0}},

                        {
                                {0, -1, 0},
                                {-1, 1, 0},
                                {1, 1, -1}
                        }
                });

        Network network = builder.buildNetwork();

        convLayer.setFilter(0, filter1, 1);
        convLayer.setFilter(1, filter2, 0);

        Layer.printArray(network.calculate(input));

    }

}
