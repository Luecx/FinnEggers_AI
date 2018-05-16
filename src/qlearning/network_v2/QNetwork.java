package qlearning.network_v2;

import network.Network;
import network.NetworkBuilder;
import network.data.TrainSet;
import network.functions.activation.Softmax;
import network.functions.activation.TanH;
import network.functions.error.CrossEntropy;
import network.layers.DenseLayer;
import network.layers.Layer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.WeakHashMap;

/**
 * Created by finne on 16.05.2018.
 */
public class QNetwork {

    public CheeseGame cheeseGame;
    public Network network;

    public int buffer_size = 50;
    public int batch_size = 30;
    public double learning_rate = 0.2;
    public double threshold = 0.7;
    public double discount_factor = 0.95;

    public QNetwork(CheeseGame cheeseGame, Network network) {
        this.cheeseGame = cheeseGame;
        this.network = network;
    }

    public QNetwork(CheeseGame cheeseGame) {
        this.cheeseGame = cheeseGame;
        NetworkBuilder builder = new NetworkBuilder(1, cheeseGame.getWidth(), cheeseGame.getHeight());
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(30).biasRange(-1, 1).setActivationFunction(new TanH()));
        builder.addLayer(new DenseLayer(4).biasRange(-1, 1).setActivationFunction(new TanH()));
        this.network = builder.buildNetwork();
    }

    public void train(int iterations) {

        double t = threshold;
        double[][][] visited = new double[1][cheeseGame.getWidth()][cheeseGame.getHeight()];

        TrainSet set = new TrainSet(1,cheeseGame.getWidth(),cheeseGame.getHeight(),1,1,4);

        for (int i = 0; i < iterations; i++) {

            t = threshold + (1 - threshold) * (double) i / (double) iterations;
            int m = (Math.random() > t) ? (int) (Math.random() * 4) : network_move();

            double[][][] in = new double[1][cheeseGame.getWidth()][cheeseGame.getHeight()];
            in[0][cheeseGame.getPlayer_x()][cheeseGame.getPlayer_y()] = 1;

            double[][][] network_out = network.calculate(in);
            double r = cheeseGame.move(m);

            visited[0][cheeseGame.getPlayer_x()][cheeseGame.getPlayer_y()]++;

            double[][][] in_next = new double[1][cheeseGame.getWidth()][cheeseGame.getHeight()];
            in_next[0][cheeseGame.getPlayer_x()][cheeseGame.getPlayer_y()] = 1;
            double[][][] optimalFutureArray = network.calculate(in_next);
            double optimalFutureValue = optimalFutureArray[0][0][ArrayTools.indexOfHighestValue(optimalFutureArray[0][0])];

            if(r > 0.5 || r < -0.5){
                network_out[0][0][m] = r;
            }else{
                network_out[0][0][m] = r + discount_factor * optimalFutureValue;
            }

            if(r > 0.5){
                Layer.printArray(in);
                Layer.printArray(network_out);
            }

            set.addData(in, network_out);
            if (set.size() > buffer_size) {
                set.remove(0);
                train_network(set);
            }
        }

        Layer.printArray(visited);
    }

    private void train_network(TrainSet s) {
        network.train(s.extractBatch(batch_size),1, batch_size, learning_rate);
    }

    private int network_move() {
        double[][][] in = new double[1][cheeseGame.getWidth()][cheeseGame.getHeight()];
        in[0][cheeseGame.getPlayer_x()][cheeseGame.getPlayer_y()] = 1;
        return ArrayTools.indexOfHighestValue(network.calculate(in)[0][0]);
    }

    public void validate(int moves) {
        cheeseGame.reset();
        for(int i = 0; i < moves; i++){
            try{
                Thread.sleep( 200);
            }catch (Exception e){

            }
            System.out.println("z");
            System.out.println(network_move());
            cheeseGame.move(network_move());
            cheeseGame.printMap();
        }

    }
}
