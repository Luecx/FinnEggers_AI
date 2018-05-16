package network;

import network.data.TrainSet;
import network.functions.activation.ActivationFunction;
import network.functions.activation.ReLU;
import network.functions.activation.Sigmoid;
import network.functions.activation.Softmax;
import network.functions.error.CrossEntropy;
import network.layers.DenseLayer;
import network.layers.InputLayer;
import network.layers.Layer;
import network.layers.OutputLayer;
import network.tools.ArrayTools;
import network.functions.error.ErrorFunction;

import java.util.ArrayList;

/**
 * Created by finne on 25.01.2018.
 */
public class Network {

    private InputLayer inputLayer;
    private OutputLayer outputLayer;

    public Network(InputLayer inputLayer) {
        Layer cur = inputLayer;
        this.inputLayer = inputLayer;
        while (cur.getNext_layer() != null) {
            cur = cur.getNext_layer();
        }
        if (cur instanceof OutputLayer == false) {
            System.err.println("Network does not have a Output Layer");
            System.exit(-1);
        } else {
            this.outputLayer = (OutputLayer) cur;
        }
    }

    public void printArchitecture() {
        Layer cur = inputLayer;

        while (cur.getNext_layer() != null) {
            System.out.println(cur);
            cur = cur.getNext_layer();
        }
    }

    public Network setErrorFunction(ErrorFunction errorFunction) {
        this.outputLayer.setErrorFunction(errorFunction);
        return this;
    }

    public ErrorFunction getErrorFunction() {
        return this.outputLayer.getErrorFunction();
    }

    public InputLayer getInputLayer() {
        return inputLayer;
    }

    public OutputLayer getOutputLayer() {
        return outputLayer;
    }

    public double[][][] calculate(double[][][] in) {
        if (this.getInputLayer().matchingDimensions(in) == false) return null;
        this.inputLayer.setInput(in);
        this.inputLayer.feedForwardRecursive();
        return getOutput();
    }

    public void backpropagateError(double[][][] expectedOutput) {
        if (this.getOutputLayer().matchingDimensions(expectedOutput) == false) return;
        this.outputLayer.calculateOutputErrorValues(expectedOutput);
        this.outputLayer.backpropagateErrorRecursive();
    }

    public void updateWeights(double eta) {
        this.inputLayer.updateWeightsRecursive(eta);
    }

    public void train(double[][][] input, double[][][] expected, double eta) {
        if (this.getInputLayer().matchingDimensions(input) == false ||
                this.getOutputLayer().matchingDimensions(expected) == false) {
            System.err.println(
                    this.inputLayer.getOUTPUT_DEPTH() + " - " + input.length + "\n" +
                            this.inputLayer.getOUTPUT_WIDTH() + " - " + input[0].length + "\n" +
                            this.inputLayer.getOUTPUT_HEIGHT() + " - " + input[0][0].length + "\n" +
                            this.outputLayer.getOUTPUT_DEPTH() + " - " + expected.length + "\n" +
                            this.outputLayer.getOUTPUT_WIDTH() + " - " + expected[0].length + "\n" +
                            this.outputLayer.getOUTPUT_HEIGHT() + " - " + expected[0][0].length + "\n");
            return;
        }
        this.calculate(input);
        this.backpropagateError(expected);
        this.updateWeights(eta);
    }

    public void train(TrainSet trainSet, int epochs, int batch_size, double fall_of) {
        double e = 0.1;
        for (int i = 0; i < epochs; i++) {
            ArrayList<TrainSet> trainSets = trainSet.shuffledParts(batch_size);
            int index = 0;
            for (TrainSet t : trainSets) {
                index++;
                for (int k = 0; k < t.size(); k++) {
                    train(t.getInput(k), t.getOutput(k), e*fall_of);
                }
                e = overall_error(t);
                System.out.println(index + "     " + e);
            }
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<< " + i + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }



//    public void train(TrainSet trainSet, int iterations, int batch_size, double eta) {
//
//        for (int it = 0; it < iterations; it++) {
//            TrainSet batch = trainSet.extractBatch(batch_size);
//            for (int k = 0; k < batch.size(); k++) {
//                train(batch.getInput(k), batch.getOutput(k), eta);
//            }
//            System.out.println(it + "   " + this.overall_error(batch));
//        }
//    }
//
//    public void train(TrainSet trainSet, int iterations, int batch_size, double init_eta, double factor) {
//
//        double er = init_eta;
//
//        for (int it = 0; it < iterations; it++) {
//            TrainSet batch = trainSet.extractBatch(batch_size);
//            for (int k = 0; k < batch.size(); k++) {
//                train(batch.getInput(k), batch.getOutput(k), er * factor);
//            }
//            System.out.println(it + "   " + (er = this.overall_error(batch)));
//        }
//    }

    public double overall_error(TrainSet trainSet) {
        double t = 0;
        for (int i = 0; i < trainSet.size(); i++) {
            this.calculate(trainSet.getInput(i));
            t += this.getOutputLayer().overall_error(trainSet.getOutput(i));
        }
        return t / (double) trainSet.size();
    }

    public double overall_error(double[][][] in, double[][][] exp) {
        this.calculate(in);
        return this.getOutputLayer().overall_error(exp);
    }

    public double[][][] getOutput() {
        return ArrayTools.copyArray(this.outputLayer.getOutput_values());
    }

    public double[][][] getInput() {
        return ArrayTools.copyArray(this.inputLayer.getOutput_values());
    }


    public static void main(String[] args) {

        TrainSet trainSet = new TrainSet(1,1,1,1,1,1);
        for(int i = 0; i < 10; i++){
            trainSet.addData(ArrayTools.createComplexFlatArray((int)(Math.random() * 1000)),ArrayTools.createComplexFlatArray((int)(Math.random() * 1000)));
        }

        System.out.println(trainSet);

        for(TrainSet t:trainSet.shuffledParts(3)){
            System.out.println(t);
        }


//        DenseLayer denseLayer;
//        NetworkBuilder builder = new NetworkBuilder(1, 1, 3);
//        builder.addLayer(denseLayer = new DenseLayer(3)
//                .setActivationFunction(new ReLU())
//                .setWeights(new double[][]{
//                        {0.1, 0.3, 0.4},
//                        {0.2, 0.2, 0.3},
//                        {0.3, 0.7, 0.9}})
//                .setBias(new double[]{1, 1, 1}));
//        builder.addLayer(new DenseLayer(3)
//                .setActivationFunction(new Sigmoid())
//                .setWeights(new double[][]{
//                        {0.2, 0.3, 0.6},
//                        {0.3, 0.5, 0.4},
//                        {0.5, 0.7, 0.8}})
//                .setBias(new double[]{1, 1, 1}));
//        builder.addLayer(new DenseLayer(3)
//                .setActivationFunction(new Softmax())
//                .setWeights(new double[][]{
//                        {0.1, 0.3, 0.5},
//                        {0.4, 0.7, 0.2},
//                        {0.8, 0.2, 0.9}})
//                .setBias(new double[]{1, 1, 1}));
//
//        Network network = builder.buildNetwork();
//        network.setErrorFunction(new CrossEntropy());
//
//        double[][][] in = ArrayTools.createComplexFlatArray(0.1, 0.2, 0.7);
//        double[][][] out = ArrayTools.createComplexFlatArray(1, 0, 0);
//
//        for (int i = 0; i < 1000; i++) {
//            network.train(in, out, 0.003);
//            double e = network.overall_error(in, out);
//            if (Double.isNaN(e)) {
//                network.analyseNetwork();
//                break;
//            } else {
//                System.out.println(e);
//            }
//        }
//
//        network.analyseNetwork();
    }

    public void analyseNetwork() {
        Layer cur = inputLayer;
        System.out.println(cur.getClass().getSimpleName());
        while (cur.getNext_layer() != null) {
            System.out.println("..................................");
            System.out.println(cur.getClass().getSimpleName());
            System.out.println("Output:");
            Layer.printArray(cur.getOutput_values());
            System.out.println("Derivative:");
            Layer.printArray(cur.getOutput_derivative_values());
            System.out.println("Error:");
            Layer.printArray(cur.getOutput_error_values());
            cur = cur.getNext_layer();
        }
    }

    public int getINPUT_DEPTH() {
        return inputLayer.getOUTPUT_DEPTH();
    }

    public int getINPUT_WIDTH() {
        return inputLayer.getOUTPUT_WIDTH();
    }

    public int getINPUT_HEIGHT() {
        return inputLayer.getOUTPUT_HEIGHT();
    }

    public int getOUTPUT_DEPTH() {
        return outputLayer.getOUTPUT_DEPTH();
    }

    public int getOUTPUT_WIDTH() {
        return outputLayer.getOUTPUT_WIDTH();
    }

    public int getOUTPUT_HEIGHT() {
        return outputLayer.getOUTPUT_HEIGHT();
    }
}
