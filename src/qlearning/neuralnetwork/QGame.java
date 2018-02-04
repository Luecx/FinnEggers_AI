package qlearning.neuralnetwork;

/**
 * Created by finne on 04.02.2018.
 */
public abstract class QGame {

    public final int STATE_DEFINITION_DEPTH;
    public final int STATE_DEFINITION_WIDTH;
    public final int STATE_DEFINITION_HEIGHT;

    public final int STATE_ACTIONS;

    public QGame(int STATE_DEFINITION_DEPTH, int STATE_DEFINITION_WIDTH, int STATE_DEFINITION_HEIGHT, int STATE_ACTIONS) {
        this.STATE_DEFINITION_DEPTH = STATE_DEFINITION_DEPTH;
        this.STATE_DEFINITION_WIDTH = STATE_DEFINITION_WIDTH;
        this.STATE_DEFINITION_HEIGHT = STATE_DEFINITION_HEIGHT;
        this.STATE_ACTIONS = STATE_ACTIONS;
    }

    public abstract double[][][] getCurrentState();




}
