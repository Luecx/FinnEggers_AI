package qlearning.neuralnetwork.qgames;

import network.Network;
import network.NetworkBuilder;
import network.layers.DenseLayer;
import network.layers.Layer;
import network.tools.ArrayTools;
import qlearning.neuralnetwork.QGame;
import qlearning.neuralnetwork.QNetwork;
import qlearning.neuralnetwork.QState;
import qlearning.neuralnetwork.console.Console;
import sun.nio.ch.Net;

/**
 * Created by finne on 05.02.2018.
 */
public class Cheese1D extends QGame {

    int size;
    int currentIndex;
    int negativeIndex;
    int positiveIndex;


    public Cheese1D(int size, int negativeIndex, int positiveIndex) {
        super(1,1,size,2);
        this.currentIndex = size / 2;
        this.size = size;
        this.negativeIndex = negativeIndex;
        this.positiveIndex = positiveIndex;
        reset();
    }

    @Override
    public void performAction(int action) {
        this.currentState[0][0][currentIndex] = 0;
        currentStateReward = 0;

        if(action == 0) {
            this.currentIndex --;
        }else{
            this.currentIndex ++;
        }
        this.currentState[0][0][currentIndex] = 1;

        if(currentIndex == 0 || currentIndex == size-1 || currentIndex == negativeIndex){
            this.currentStateReward = -1;
            reset();
        }else if(currentIndex == positiveIndex){
            this.currentStateReward = +1;
            reset();
        }
    }

    @Override
    public void printGameState() {
        String t = "z#";
        for(int i = 1; i < this.size - 1; i++) {
            if(this.negativeIndex == i){
                t += "#";
            }else if(this.positiveIndex == i) {
                t += "O";
            }else if(this.currentIndex == i) {
                t += "M";
            }else{
                t += "-";
            }
        }
        System.out.print("\r" + t +"#");
    }

    public void printRecommendationField(Network network){
        String t = "z# ";
        for(int i = 1; i < this.size - 1; i++) {
            if(this.negativeIndex == i){
                t += "#";
            }else if(this.positiveIndex == i) {
                t += "O";
            }else{
                double[][][] in = new double[1][1][size];
                in[0][0][i] = 1;
                int id = ArrayTools.indexOfHighestValue(network.calculate(in)[0][0]);
                t += id;
            }
        }
        System.out.print("\r" + t +"#");
    }

    private void reset() {
        this.currentState[0][0][currentIndex] = 0;
        this.currentState[0][0][size / 2] = 1;
        currentIndex = size/2;
    }

    public static void main(String[] args) throws Exception {
        Cheese1D cheese1D = new Cheese1D(20,3,16);

        NetworkBuilder builder = new NetworkBuilder(1,1,20);
        builder.addLayer(new DenseLayer(18));
        builder.addLayer(new DenseLayer(12));
        builder.addLayer(new DenseLayer(2));
        Network network = builder.buildNetwork();

        QNetwork qNetwork = new QNetwork( network, cheese1D);
        qNetwork.batch_size = 100;
        qNetwork.train(2000);

        Console c = new Console();

        qNetwork.validate(10);
        cheese1D.printRecommendationField(qNetwork.getNetwork());
    }
}
