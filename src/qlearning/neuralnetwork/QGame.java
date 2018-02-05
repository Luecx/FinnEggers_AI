package qlearning.neuralnetwork;

import network.tools.ArrayTools;

/**
 * Created by finne on 04.02.2018.
 */
public abstract class QGame {

    public final int STATE_DEFINITION_DEPTH;
    public final int STATE_DEFINITION_WIDTH;
    public final int STATE_DEFINITION_HEIGHT;

    public final int STATE_ACTIONS;
    
    public final double[][][] currentState;
    public double currentStateReward;

    public QGame(int STATE_DEFINITION_DEPTH, int STATE_DEFINITION_WIDTH, int STATE_DEFINITION_HEIGHT, int STATE_ACTIONS) {
        this.STATE_DEFINITION_DEPTH = STATE_DEFINITION_DEPTH;
        this.STATE_DEFINITION_WIDTH = STATE_DEFINITION_WIDTH;
        this.STATE_DEFINITION_HEIGHT = STATE_DEFINITION_HEIGHT;
        this.STATE_ACTIONS = STATE_ACTIONS;
        currentState = new double[STATE_DEFINITION_DEPTH][STATE_DEFINITION_WIDTH][STATE_DEFINITION_HEIGHT];
    }

    /**
     * Returns the current state of the game.
     * @return
     */
    public QState getCurrentState(){
        QState ret =  new QState(ArrayTools.copyArray(currentState), currentStateReward);
        currentStateReward = 0;
        return ret;
    }

    /**
     * Performs the action on the current state of the game.
     * Needs to be implemented. This is the "move" method in usual games.
     * The function needs to change the currentState variable.
     * Optional: Edit the currentStateReward
     * @param action
     */
    public abstract void performAction(int action);

    public int getSTATE_DEFINITION_DEPTH() {
        return STATE_DEFINITION_DEPTH;
    }

    public int getSTATE_DEFINITION_WIDTH() {
        return STATE_DEFINITION_WIDTH;
    }

    public int getSTATE_DEFINITION_HEIGHT() {
        return STATE_DEFINITION_HEIGHT;
    }

    public int getSTATE_ACTIONS() {
        return STATE_ACTIONS;
    }

    public double getCurrentStateReward() {
        return currentStateReward;
    }

    public void setCurrentStateReward(double currentStateReward) {
        this.currentStateReward = currentStateReward;
    }
}
