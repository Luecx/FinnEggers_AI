package qlearning.neuralnetwork;

import neuralnetworks.Network;

/**
 * Created by finne on 04.02.2018.
 */
public class QMap {

    public final int MAP_WIDTH;
    public final int MAP_HEIGHT;
    public final int MAP_ACTIONS;

    private Network network;

    public QMap(int MAP_WIDTH, int MAP_HEIGHT, int MAP_ACTIONS) {
        this.MAP_WIDTH = MAP_WIDTH;
        this.MAP_HEIGHT = MAP_HEIGHT;
        this.MAP_ACTIONS = MAP_ACTIONS;
    }

    public void setNetwork(Network network) {
        if(network.getO)
        this.network = network;
    }

    public int getMAP_WIDTH() {
        return MAP_WIDTH;
    }

    public int getMAP_HEIGHT() {
        return MAP_HEIGHT;
    }

    public int getMAP_ACTIONS() {
        return MAP_ACTIONS;
    }

    public Network getNetwork() {
        return network;
    }
}
