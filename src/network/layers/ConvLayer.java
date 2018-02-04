package network.layers;

import network.NetworkBuilder;
import network.functions.activation.ActivationFunction;
import network.functions.activation.ReLU;
import network.tools.ArrayTools;

/**
 * Created by finne on 04.02.2018.
 */
public class ConvLayer extends Layer{


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

    public void setFilter(int index, double[][][] filter, double bias) {
        this.bias[index] = bias;
        this.filter[index] = filter;
    }

    private double dotProduct(int channel, int startX, int startY){
        double sum = 0;
        for(int i = 0; i < filter_size; i++) {
            for(int n = 0; n < filter_size; n++) {
                if(startX + i >= 0 && startX + i < this.INPUT_WIDTH && startY + n >= 0 && startY + n < this.INPUT_HEIGHT){
                    for(int d = 0; d < this.INPUT_DEPTH; d++) {
                        sum += filter[channel][d][i][n] * this.getInput_values()[d][startX + i][startY + n];
                    }
                }
            }
        }
        return sum;
    }

    @Override
    protected void on_build() throws Exception {
        filter = ArrayTools.createRandomArray(channel_amount, this.getINPUT_DEPTH(), filter_size, filter_size, lowerWeightsRange, upperWeigthsRange);
        bias = ArrayTools.createRandomArray(channel_amount, lowerBiasRange, upperBiasRange);
    }

    @Override
    protected void calculateOutputDimensions() throws Exception {

        this.OUTPUT_DEPTH = channel_amount;
        this.OUTPUT_WIDTH = (this.INPUT_WIDTH + this.padding * 2 - filter_size) / filter_Stride;
        this.OUTPUT_HEIGHT = (this.INPUT_HEIGHT + this.padding * 2 - filter_size) / filter_Stride;

        double g = ((double)this.INPUT_WIDTH + (double)this.padding * 2 - (double)filter_size) / (double)filter_Stride;
        double g1 = ((double)this.INPUT_HEIGHT + (double)this.padding * 2 - (double)filter_size) / (double)filter_Stride;

        if(g != (int)g || g1 != (int)g1) throw new Exception("Format does not work! Use a different padding-value!");

    }

    @Override
    public void calculate() {
        for(int channel = 0; channel < this.OUTPUT_DEPTH; channel++) {
            for(int x = 0; x < this.OUTPUT_WIDTH; x ++) {
                for(int y = 0; y < this.OUTPUT_HEIGHT; y ++) {
                    int startIndexX = x * filter_Stride - padding;
                    int startIndexY = y * filter_Stride - padding;

                    double v = dotProduct(channel, startIndexX, startIndexY);

                    this.output_values[channel][x][y] = activationFunction.activation(v);
                    this.output_derivative_values[channel][x][y] = activationFunction.activation_prime(v);

                }
            }
        }
    }

    @Override
    public void backprop_error() {

    }

    @Override
    public void update_weights(double eta) {

    }

    public static void main(String[] args) {
        NetworkBuilder builder = new NetworkBuilder(3,5,5);
        builder.addLayer(new ConvLayer(2,3,2,1));
    }

}
