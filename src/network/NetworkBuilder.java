package network;

import network.functions.activation.ReLU;
import network.functions.activation.Sigmoid;
import network.layers.*;
import network.tools.ArrayTools;
import network.functions.activation.LeakyReLU;

import java.util.ArrayList;

/**
 * Created by finne on 25.01.2018.
 */
public class NetworkBuilder {

    InputLayer inputLayer;

    ArrayList<Layer> layers = new ArrayList<>();

    public NetworkBuilder(int input_depth, int input_width, int input_height) {
        inputLayer = new InputLayer(input_depth, input_width, input_height);

        inputLayer.setOutput_error_values(new double[input_depth][input_width][input_height]);
        inputLayer.setOutput_derivative_values(new double[input_depth][input_width][input_height]);
        inputLayer.setOutput_values(new double[input_depth][input_width][input_height]);
    }

    public NetworkBuilder addLayer(Layer layer) {
        layers.add(layer);
        return this;
    }

    public Network buildNetwork() {
        try{
            Layer b = inputLayer;
            for(Layer l: layers){
                l.connectToPreviousLayer(b);
                b = l;
            }
            OutputLayer outputLayer = new OutputLayer(b);
            outputLayer.connectToPreviousLayer(b);

            return new Network(inputLayer);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

        NetworkBuilder builder = new NetworkBuilder(1,1,5);
        for(int i = 0; i < 1; i++) {
            builder.addLayer(
                    new DenseLayer(2)
                            .setActivationFunction(new ReLU())
                            .weightsRange(1,1)
                            .biasRange(0,1)
            );
        }
        Network network = builder.buildNetwork();

        double[][][] input = ArrayTools.createComplexFlatArray(0.1,0.1,0.1,0.4,0.3);
        double[][][] expected = ArrayTools.createComplexFlatArray(1,0);

        for(int i = 0; i < 10000; i++){
            network.train(input,expected,0.3);
        }


        Layer.printArray(network.calculate(input));

    }
}
