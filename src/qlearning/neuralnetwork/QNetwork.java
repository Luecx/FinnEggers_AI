package qlearning.neuralnetwork;

import network.Network;
import network.tools.ArrayTools;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

/**
 * Created by finne on 05.02.2018.
 */
public class QNetwork {


    private Network network;
    private QGame qGame;

    public int buffer_size = 500;
    public int batch_size = 300;
    public int iterations_per_batch = 5;

    public double threshold = 0.8;
    public double discount_factor = 0.8;
    public double learning_rate = 0.2;

    Deque<QState> buffer = new ArrayDeque<>();

    public QNetwork(Network network, QGame qGame) throws Exception {
        this.network = network;
        this.qGame = qGame;
        if(this.network.getINPUT_DEPTH() != qGame.getSTATE_DEFINITION_DEPTH() ||
                this.network.getINPUT_WIDTH() != qGame.getSTATE_DEFINITION_WIDTH() ||
                this.network.getINPUT_HEIGHT() != qGame.getSTATE_DEFINITION_HEIGHT() ||
                this.network.getOUTPUT_DEPTH() != 1 ||
                this.network.getOUTPUT_WIDTH() != 1 ||
                this.network.getOUTPUT_HEIGHT() != qGame.STATE_ACTIONS){
            throw new Exception("Doesnt work!");
        }
    }

    public void train(int its) {

        QState currentState = qGame.getCurrentState();

        for(int i = 0; i < its; i++) {

        }

        double ran = Math.random();
        int action = 0;
        QState q = qGame.getCurrentState();
        if(ran > threshold) {
            action = (int)(Math.random() * qGame.getSTATE_ACTIONS());
        }else{
            action = networkRecommendation(this.qGame.getCurrentState());
        }

        qGame.performAction(action);
        double reward = qGame.getCurrentStateReward();

    }

    public int networkRecommendation(QState qState) {
        return ArrayTools.indexOfHighestValue(ArrayTools.convertFlattenedArray(network.calculate(qState.getData())));
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public QGame getqGame() {
        return qGame;
    }

    public void setqGame(QGame qGame) {
        this.qGame = qGame;
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

    public int getIterations_per_batch() {
        return iterations_per_batch;
    }

    public void setIterations_per_batch(int iterations_per_batch) {
        this.iterations_per_batch = iterations_per_batch;
    }
}
