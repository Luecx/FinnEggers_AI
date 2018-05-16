package qlearning.network;

import network.Network;
import network.functions.error.MSE;
import network.tools.ArrayTools;
import qlearning.basic.vectors.Vector3i;

/**
 * Created by finne on 11.02.2018.
 */
public class QNetwork {

    private Vector3i inputDimensions;
    private int decisions;
    private Network network;

    private double[][][] emptyOutputBuffer;

    public QNetwork(Network network) {
        this.network = network;
        this.inputDimensions = new Vector3i(network.getINPUT_DEPTH(),
                network.getINPUT_WIDTH(), network.getINPUT_HEIGHT());
        this.decisions = network.getOUTPUT_HEIGHT();
        this.network.setErrorFunction(new MSE());
        this.emptyOutputBuffer = new double[1][1][decisions];
    }

    public Vector3i getInputDimensions() {
        return inputDimensions;
    }

    public double highestQValue(QState qState) {
        double max = -10000;
        for(double d:this.network.calculate(qState.getIn())[0][0]){
            if(d > max) max = d;
        }
        return max;
    }

    public int bestQMove(QState qState) {
        return ArrayTools.indexOfHighestValue(network.calculate(qState.getIn())[0][0]);
    }

    public void trainQMove(QMove move, double discount_factor, double eta) {
//        if(move.getPrevState().getDimensions().equals(this.inputDimensions)){
//            if(move.getReward() != 0) {
//                ((MSESingleError1D) network.getErrorFunction()).expected_output = move.getReward();
//            }else{
//                ((MSESingleError1D) network.getErrorFunction()).expected_output =
//                        move.getReward() + discount_factor * highestQValue(move.getNextState());
//            }
//            ((MSESingleError1D) network.getErrorFunction()).error_index = move.getDecision();
//            network.train(move.getPrevState().getIn(), this.emptyOutputBuffer, eta);
//        }

    }

    public int getDecisions() {
        return decisions;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
