package mnist;

import network.Network;
import network.NetworkBuilder;
import network.data.TrainSet;
import network.functions.activation.Sigmoid;
import network.layers.DenseLayer;
import network.layers.Layer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;

import java.io.File;

/**
 * Created by Luecx on 10.08.2017.
 */
public class Mnist {


    public static void main(String[] args) {
        TrainSet trainSet = createTrainSet(0,0);

        NetworkBuilder builder = new NetworkBuilder(1,28,28);
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(70)
                .setActivationFunction(new Sigmoid())
                .biasRange(0,1)
                .weightsRange(-1,1));
        builder.addLayer(new DenseLayer(35)
                .setActivationFunction(new Sigmoid())
                .biasRange(0,1)
                .weightsRange(-1,1));
        builder.addLayer(new DenseLayer(10)
                .setActivationFunction(new Sigmoid())
                .biasRange(0,1)
                .weightsRange(-1,1));
        Network network = builder.buildNetwork();

        Layer.printArray(trainSet.getInput(0));

        network.train(trainSet, 1000,10,0.3);

        Layer.printArray(network.calculate(trainSet.getInput(0)));

//
//        trainData(network, trainSet, 100,100,100);
//        testTrainSet(network, trainSet,1);
    }

    public static TrainSet createTrainSet(int start, int end) {
        TrainSet set = new TrainSet(1,28,28,1,1,10);

        try {
            String path = new File("").getAbsolutePath();

            MnistImageFile m = new MnistImageFile(path + "/res/trainImage.idx3-ubyte", "rw");
            MnistLabelFile l = new MnistLabelFile(path + "/res/trainLabel.idx1-ubyte", "rw");

            for(int i = start; i <= end; i++) {
                if(i % 100 ==  0){
                    System.out.println("prepared: " + i);
                }

                double[][] input = new double[28][28];
                double[] output = new double[10];

                output[l.readLabel()] = 1d;
                for(int j = 0; j < 28*28; j++){
                    input[j / 28][j % 28] = (double)m.read() / (double)256;
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

    public static void trainData(Network net,TrainSet set, int epochs, int loops, int batch_size) {
        for(int e = 0; e < epochs;e++) {
            net.train(set, loops, batch_size, 0.3);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>   "+ e+ "   <<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public static void testTrainSet(Network net, TrainSet set, int printSteps) {
        int correct = 0;
        for(int i = 0; i < set.size(); i++) {

            double highest = ArrayTools.indexOfHighestValue(ArrayTools.convertFlattenedArray(net.calculate(set.getInput(i))));
            double actualHighest = ArrayTools.indexOfHighestValue(ArrayTools.convertFlattenedArray(set.getOutput(i)));
            if(highest == actualHighest) {
                correct ++ ;
            }
            if(i % printSteps == 0) {
                System.out.println(i + ": " + (double)correct / (double) (i + 1));
            }
        }
        System.out.println("Testing finished, RESULT: " + correct + " / " + set.size()+ "  -> " + (double)correct / (double)set.size() +" %");
    }
}
