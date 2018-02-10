package qlearning.neuralnetwork.qnetworktables;

import network.Network;
import network.NetworkBuilder;
import network.functions.error.ErrorFunction;
import network.functions.error.MSESingleError1D;
import network.layers.DenseLayer;
import network.layers.Layer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;

import java.util.Arrays;

/**
 * Created by finne on 07.02.2018.
 */
public class QNetworkTable {

    private Network network;
    private int inputs;
    private int outputs;

    public QNetworkTable(Network network, int inputs, int outputs) {
        this.network = network;
        this.inputs = inputs;
        this.outputs = outputs;
        network.setErrorFunction(new MSESingleError1D());
        in_buffer = new double[1][1][inputs];
        out_buffer = new double[1][1][outputs];
    }

    public String toString() {
        String s = "";

        for(int i = 0; i < inputs; i++) {
            double[][][] in = new double[1][1][inputs];
            in[0][0][i] = 1;
            s += Arrays.toString(network.calculate(in)[0][0]) +"\n";
        }

        return s;
    }

    private double[][][] in_buffer;
    private double[][][] out_buffer;

    public void train(int in, int out, double val) {
        ((MSESingleError1D)network.getErrorFunction()).error_index = out;
        ((MSESingleError1D)network.getErrorFunction()).expected_output = val;

        in_buffer = new double[1][1][inputs];
        in_buffer[0][0][in] = 1;
        double v = 0;
        while((v = network.overall_error(in_buffer,out_buffer)) > 0.00000001){
            network.train(in_buffer, out_buffer, 0.3);
        }
    }

    public static void main(String[] args) {



        NetworkBuilder builder = new NetworkBuilder(1,1,10);
        DenseLayer denseLayer = new DenseLayer(4)
                .weightsRange(0,2)
                .biasRange(0,0);
        builder.addLayer(denseLayer);
        Network network = builder.buildNetwork();

        QNetworkTable table = new QNetworkTable(network, 10,4);

        network.analyseNetwork();

        table.train(0,0,1);
        network.analyseNetwork();
        table.train(0,1,0);

        network.analyseNetwork();

        System.out.println(table);

    }
}
