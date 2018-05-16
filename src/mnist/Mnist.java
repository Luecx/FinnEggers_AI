package mnist;

import network.Network;
import network.NetworkBuilder;
import network.data.TrainSet;
import network.functions.activation.*;
import network.functions.error.CrossEntropy;
import network.functions.error.MSE;
import network.layers.*;
import network.tools.ArrayTools;

import java.io.File;

/**
 * Created by Luecx on 10.08.2017.
 */
public class Mnist {


    public static void main(String[] args) {

//        NetworkBuilder builder = new NetworkBuilder(1,10,10);
//        builder.addLayer(new TransformationLayer());
//        builder.addLayer(new DenseLayer(30).setActivationFunction(new LeakyReLU()));
//        builder.addLayer(new DenseLayer(10).setActivationFunction(new LeakyReLU()));
//
//        Network network = builder.buildNetwork();
//
//        Layer.printArray(network.calculate(ArrayTools.createRandomArray(1,10,10,0,1)));
//        network.analyseNetwork();
//
//        double[][][] in = ArrayTools.createRandomArray(1,10,10,0,1);
//        double[][][] out = ArrayTools.createRandomArray(1,1,10,0,1);
//
//        for(int i = 0; i < 100; i++){
//            network.train(in,out,0.9);
//        }
//
//        System.out.println("#################################################");
//        Layer.printArray(network.calculate(in));
//        Layer.printArray(out);


        ConvLayer conv1;

        NetworkBuilder builder = new NetworkBuilder(1, 28, 28);
        builder.addLayer(conv1 = new ConvLayer(12, 5, 1, 2)
                .biasRange(0, 0)
                .weightsRange(-2, 2)
                .setActivationFunction(new ReLU()));
        builder.addLayer(new PoolingLayer(2));
        builder.addLayer(new ConvLayer(30, 5, 1, 0)
                .biasRange(0, 0)
                .weightsRange(-2, 2)
                .setActivationFunction(new ReLU()));
        builder.addLayer(new PoolingLayer(2));
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(120)
                .setActivationFunction(new ReLU())
        );
        builder.addLayer(new DenseLayer(10)
                .setActivationFunction(new Softmax())
        );
        Network network = builder.buildNetwork();
        network.setErrorFunction(new CrossEntropy());

        network.printArchitecture();

        TrainSet trainSet = createTrainSet(0,2999);


        network.train(trainSet,3,10,0.001);

        testTrainSet(network, trainSet,1);




        for(int i = 0; i < 8; i++){
            System.out.println("");
            Layer.printArray(conv1.getFilter(i));
        }
    }

    public static TrainSet createTrainSet(int start, int end) {
        TrainSet set = new TrainSet(1, 28, 28, 1, 1, 10);

        try {
            String path = new File("").getAbsolutePath();

            MnistImageFile m = new MnistImageFile(path + "/res/trainImage.idx3-ubyte", "rw");
            MnistLabelFile l = new MnistLabelFile(path + "/res/trainLabel.idx1-ubyte", "rw");

            for (int i = start; i <= end; i++) {
                if (i % 100 == 0) {
                    System.out.println("prepared: " + i);
                }

                double[][] input = new double[28][28];
                double[] output = new double[10];

                output[l.readLabel()] = 1d;
                for (int j = 0; j < 28 * 28; j++) {
                    input[j / 28][j % 28] = (double) m.read() / (double) 256;
                }

                set.addData(new double[][][]{input}, new double[][][]{{output}});
                m.next();
                l.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return set;
    }

    public static void trainData(Network net, TrainSet set, int epochs, int loops, int batch_size) {
        for (int e = 0; e < epochs; e++) {
            net.train(set, loops, batch_size, 2);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>   " + e + "   <<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public static void testTrainSet(Network net, TrainSet set, int printSteps) {
        int correct = 0;
        for (int i = 0; i < set.size(); i++) {

            double highest = ArrayTools.indexOfHighestValue(ArrayTools.convertFlattenedArray(net.calculate(set.getInput(i))));
            double actualHighest = ArrayTools.indexOfHighestValue(ArrayTools.convertFlattenedArray(set.getOutput(i)));
            if (highest == actualHighest) {
                correct++;
            }
            if (i % printSteps == 0) {
                System.out.println(i + ": " + (double) correct / (double) (i + 1));
            }
        }
        System.out.println("Testing finished, RESULT: " + correct + " / " + set.size() + "  -> " + (double) correct / (double) set.size() + " %");
    }
}
