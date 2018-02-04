package network.layers;

import network.functions.activation.ActivationFunction;
import network.functions.activation.Sigmoid;
import network.tools.ArrayTools;

/**
 * Created by finne on 27.01.2018.
 */
public class DenseLayer extends Layer {

    private double[][] weights;
    private double[]   bias;

    private ActivationFunction activationFunction;


    public DenseLayer(int output_height) {
        super(1, 1, output_height);
    }

    public DenseLayer setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        return this;
    }

    private double lowerWeightsRange = 0, upperWeigthsRange = 1;
    private double lowerBiasRange = 0, upperBiasRange = 1;

    public DenseLayer weightsRange(double lower, double upper) {
        this.lowerWeightsRange = lower;
        this.upperWeigthsRange = upper;
        return this;
    }

    public DenseLayer biasRange(double lower, double upper) {
        this.lowerBiasRange = lower;
        this.upperBiasRange = upper;
        return this;
    }

    @Override
    protected void on_build() throws Exception{
        if(this.INPUT_WIDTH > 1 || this.INPUT_DEPTH > 1){
            throw new Exception("Input must be flattened");
        }else{
            weights = ArrayTools.createRandomArray(this.OUTPUT_HEIGHT, this.INPUT_HEIGHT, lowerWeightsRange, upperWeigthsRange);
            bias =  ArrayTools.createRandomArray(this.OUTPUT_HEIGHT, lowerBiasRange, upperBiasRange);
            if(activationFunction == null) {
                activationFunction = new Sigmoid();
            }
        }
    }

    @Override
    protected void calculateOutputDimensions() {

    }

    @Override
    public void calculate() {
        for(int i = 0; i < this.OUTPUT_HEIGHT; i++) {
            double sum = bias[i];
            for(int n = 0; n < this.INPUT_HEIGHT; n++) {
                sum += this.getInput_values()[0][0][n] * weights[i][n];
            }
            this.output_values[0][0][i] = this.activationFunction.activation(sum);
            this.output_derivative_values[0][0][i] = this.activationFunction.activation_prime(sum);
        }
    }

    @Override
    public void backprop_error() {
        for(int i = 0; i < this.INPUT_HEIGHT; i++) {
            double sum = 0;
            for(int n = 0; n < this.getOUTPUT_HEIGHT(); n++) {
                sum += weights[n][i] * output_error_values[0][0][n];
            }
            this.getInput_error_values()[0][0][i] = this.getInput_derivative_values()[0][0][i] * sum;
        }
    }

    @Override
    public void update_weights(double eta) {

        for(int i = 0; i < this.OUTPUT_HEIGHT; i++) {
            double delta = - eta * this.output_error_values[0][0][i];
            bias[i] += delta;

            for(int prevNeuron = 0; prevNeuron < this.INPUT_HEIGHT; prevNeuron ++) {
                weights[i][prevNeuron] += delta * getInput_values()[0][0][prevNeuron];
            }
        }
    }

}
