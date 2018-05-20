package qlearning.network_v3;

import network.Network;
import network.NetworkBuilder;
import network.data.TrainSet;
import network.functions.activation.TanH;
import network.layers.DenseLayer;
import network.layers.Layer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;

/**
 * Created by finne on 17.05.2018.
 */
public class QController {

    private final Network network;
    private final QGame qGame;

    public int buffer_size = 100;
    public int batch_size =70;
    public double learning_rate = 0.2;
    public double min_threshold = 0.7;
    public double max_threshold = 0.97;
    public double discount_factor = 0.95;

    public QController(Network network, QGame qGame) {
        this.network = network;
        this.qGame = qGame;
    }

    public QController(QGame qGame) {
        this.qGame = qGame;
        NetworkBuilder builder = new NetworkBuilder(qGame.DEPTH, qGame.WIDTH, qGame.HEIGHT);
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(30).biasRange(-1, 1).setActivationFunction(new TanH()));
        builder.addLayer(new DenseLayer(20).biasRange(-1, 1).setActivationFunction(new TanH()));
        builder.addLayer(new DenseLayer(4).biasRange(-1, 1).setActivationFunction(new TanH()));
        this.network = builder.buildNetwork();
    }

    public Network getNetwork() {
        return network;
    }

    public QGame getqGame() {
        return qGame;
    }


    public int getBuffer_size() {
        return buffer_size;
    }

    public void setBuffer_size(int buffer_size) {
        this.buffer_size = buffer_size;
    }

    public int getBatch_size() {
        return batch_size;
    }

    public void setBatch_size(int batch_size) {
        this.batch_size = batch_size;
    }

    public double getLearning_rate() {
        return learning_rate;
    }

    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public double getMin_threshold() {
        return min_threshold;
    }

    public void setMin_threshold(double min_threshold) {
        this.min_threshold = min_threshold;
    }

    public double getMax_threshold() {
        return max_threshold;
    }

    public void setMax_threshold(double max_threshold) {
        this.max_threshold = max_threshold;
    }

    public double getDiscount_factor() {
        return discount_factor;
    }

    public void setDiscount_factor(double discount_factor) {
        this.discount_factor = discount_factor;
    }

    public void train(int its) {

        TrainSet set = new TrainSet(qGame.DEPTH, qGame.WIDTH, qGame.HEIGHT, 1, 1, qGame.OUTPUT_SIZE);

        for (int i = 0; i < its; i++) {

            if(i % 10 == 0){
                System.out.println(i);
            }

            double t = min_threshold + (max_threshold - min_threshold) * (double) i / (double) its;
            int m = (Math.random() > t) ? (int) (Math.random() * qGame.OUTPUT_SIZE) : qGame.network_move(this.network);

            double[][][] in = qGame.getInputData();
            double[][][] network_out = network.calculate(in);
            double r = qGame.move(m);


            double[][][] in_next = qGame.getInputData();
            double[][][] out_next = network.calculate(in_next);
            double optimalFutureValue = out_next[0][0][ArrayTools.indexOfHighestValue(out_next[0][0])];

            if (r > 0.8 || r < -0.8) {
                network_out[0][0][m] = r;
            } else {
                network_out[0][0][m] = r + discount_factor * optimalFutureValue;
            }

            set.addData(in, network_out);
            if (set.size() > buffer_size) {
                set.remove(0);
                train_network(set);
            }
        }
    }

    private void train_network(TrainSet s) {
        TrainSet b = s.extractBatch(batch_size);
        for(int i = 0; i < b.size(); i++) {
            network.train(b.getInput(i), b.getOutput(i), learning_rate);
        }
    }


    public void validate(int moves) {
        qGame.reset();
        int score = 0;
        for(int i = 0; i < moves; i++){
            try{
                Thread.sleep( 400);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("z");
            System.out.println("Score: " + score + "        move: " + i);
            double v = qGame.move(qGame.network_move(network));
            score += v > 0.8? 1: v < -0.8? -1:0;
            qGame.printMap(null);
        }

    }

}
