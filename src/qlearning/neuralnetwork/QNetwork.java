package qlearning.neuralnetwork;

import network.Network;
import network.data.TrainSet;
import network.functions.error.MSESingleError1D;
import network.tools.ArrayTools;

import java.util.*;

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

    Deque<QStateTouple> buffer = new ArrayDeque<>();

    public QNetwork(Network network, QGame qGame) throws Exception {
        this.network = network;
        this.network.setErrorFunction(new MSESingleError1D(0,0));
        this.qGame = qGame;
        
        System.out.println(network.getINPUT_DEPTH() + " " + network.getINPUT_WIDTH() + " " + network.getINPUT_HEIGHT());
        System.out.println(network.getOUTPUT_DEPTH() + " " + network.getOUTPUT_WIDTH() + " " + network.getOUTPUT_HEIGHT());
        
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

            System.out.println(i);

            int action = 0;
            if(Math.random() > threshold) {
                action = (int)(Math.random() * qGame.getSTATE_ACTIONS());
            }else{
                action = networkRecommendation(this.qGame.getCurrentState());
            }
            qGame.performAction(action);

            QState nextState = qGame.getCurrentState();
            QStateTouple touple = new QStateTouple(currentState, nextState, action);
            currentState = nextState;

            buffer.push(touple);
            if(buffer.size() > buffer_size) {
                buffer.pop();

                learnBuffer();
            }
        }
    }

    public void validate(int its) {

        for(int i = 0; i < its; i++) {

            try{
                Thread.sleep(400);
            }catch (Exception e) {

            }

            int action = networkRecommendation(this.qGame.getCurrentState());
            qGame.performAction(action);
            qGame.printGameState();
        }
    }

    private void learnBuffer() {

        QStateTouple[] out = buffer.toArray(new QStateTouple[0]);
        QStateTouple[] batch = ArrayTools.extractBatch(out, batch_size);
        ArrayTools.shuffleArray(batch);

        for(int i = 0; i < iterations_per_batch; i++) {
            for(QStateTouple t: batch){
                if(t != null) {

                    ((MSESingleError1D)network.getErrorFunction()).expected_output =
                            t.getNext_state().getReward() + discount_factor * networkRecommendation(t.next_state);
                    ((MSESingleError1D)network.getErrorFunction()).error_index = t.action;

                    network.train(t.getPrev_state().getData(), new double[1][1][qGame.STATE_ACTIONS], learning_rate);
                }
            }
        }
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
