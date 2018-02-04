package network;

import network.data.TrainSet;
import network.layers.InputLayer;
import network.layers.Layer;
import network.layers.OutputLayer;
import network.tools.ArrayTools;
import network.functions.error.ErrorFunction;

/**
 * Created by finne on 25.01.2018.
 */
public class Network {

    private InputLayer inputLayer;
    private OutputLayer outputLayer;

    public Network(InputLayer inputLayer){
        Layer cur = inputLayer;
        this.inputLayer = inputLayer;
        while(cur.getNext_layer() != null) {
            cur = cur.getNext_layer();
        }
        if(cur instanceof OutputLayer == false){
            System.err.println("Network does not have a Output Layer");
            System.exit(-1);
        }else{
            this.outputLayer = (OutputLayer)cur;
        }
    }

    public void printArchitecture() {
        Layer cur = inputLayer;

        System.out.println(cur.getClass().getSimpleName());
        System.out.println(">>>>> ns: [" + cur.getOUTPUT_DEPTH() + " "+ cur.getOUTPUT_WIDTH() + " " + cur.getOUTPUT_HEIGHT() +  "] <<<<<<<");

        while(cur.getNext_layer() != null) {
            System.out.println ("");
            cur = cur.getNext_layer();
            System.out.println(cur.getClass().getSimpleName());
            System.out.println(">>>>> ns: [" + cur.getOUTPUT_DEPTH() + " "+ cur.getOUTPUT_WIDTH() + " " + cur.getOUTPUT_HEIGHT() +  "] <<<<<<<");

        }
    }

    public Network setErrorFunction(ErrorFunction errorFunction) {
        this.outputLayer.setErrorFunction(errorFunction);
        return this;
    }

    public InputLayer getInputLayer() {
        return inputLayer;
    }

    public OutputLayer getOutputLayer() {
        return outputLayer;
    }

    public double[][][] calculate(double[][][] in) {
        if(this.getInputLayer().matchingDimensions(in) == false) return null;
        this.inputLayer.setInput(in);
        this.inputLayer.feedForwardRecursive();
        return getOutput();
    }

    public void backpropagateError(double[][][] expectedOutput) {
        if(this.getOutputLayer().matchingDimensions(expectedOutput) == false) return;
        this.outputLayer.calculateOutputErrorValues(expectedOutput);
        this.outputLayer.backpropagateErrorRecursive();
    }

    public void updateWeights(double eta) {
        this.inputLayer.updateWeightsRecursive(eta);
    }

    public void train(double[][][] input, double[][][] expected, double eta) {
        if(this.getInputLayer().matchingDimensions(input) == false ||
                this.getOutputLayer().matchingDimensions(expected) == false) {
            return;
        }
        this.calculate(input);
        this.backpropagateError(expected);
        this.updateWeights(eta);
    }

    public void train(TrainSet trainSet, int iterations, int batch_size, double eta) {

        for(int it = 0; it < iterations; it++){
            TrainSet batch = trainSet.extractBatch(batch_size);
            for(int k = 0; k < batch.size(); k++) {
                train(batch.getInput(k), batch.getOutput(k),eta);
            }
            System.out.println(it + "   " + this.overall_error(batch));
        }
    }

    public double overall_error(TrainSet trainSet) {
        double t = 0;
        for(int i = 0; i < trainSet.size(); i++) {
            this.calculate(trainSet.getInput(i));
            t += this.getOutputLayer().overall_error(trainSet.getOutput(i));
        }
        return t/(double)trainSet.size();
    }

    public double[][][] getOutput( ){
        return ArrayTools.copyArray(this.outputLayer.getOutput_values());
    }

    public double[][][] getInput( ){
        return ArrayTools.copyArray(this.inputLayer.getOutput_values());
    }

    public void analyseNetwork() {
        Layer cur = inputLayer;

        System.out.println(cur.getClass().getSimpleName());
        System.out.println(">>>>> ns: [" + cur.getOUTPUT_DEPTH() + " "+ cur.getOUTPUT_WIDTH() + " " + cur.getOUTPUT_HEIGHT() +  "] <<<<<<<");

        while(cur.getNext_layer() != null) {
            System.out.println("################################################################################################");
            System.out.println ("");
            cur = cur.getNext_layer();
            System.out.println(cur.getClass().getSimpleName());
            System.out.println(">>>>> ns: [" + cur.getOUTPUT_DEPTH() + " "+ cur.getOUTPUT_WIDTH() + " " + cur.getOUTPUT_HEIGHT() +  "] <<<<<<<");
            Layer.printArray(cur.getOutput_values());
            Layer.printArray(cur.getOutput_error_values());
        }
    }
}
