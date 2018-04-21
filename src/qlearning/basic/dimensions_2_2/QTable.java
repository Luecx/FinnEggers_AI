package qlearning.basic.dimensions_2_2;

import network.layers.Layer;
import network.tools.ArrayTools;
import qlearning.basic.vectors.Vector2i;

/**
 * Created by finne on 10.02.2018.
 */
public class QTable {

    private final int STATES_DEPTH, STATES_WIDTH, ACTIONS;
    private double[][][] qTable;


    public QTable(Vector2i STATES, int ACTIONS) {
        this.STATES_DEPTH = STATES.x;
        this.STATES_WIDTH = STATES.y;
        this.ACTIONS = ACTIONS;
        qTable = ArrayTools.createRandomArray(STATES.x, STATES.y, ACTIONS, -0.3,0.3);
    }

    public void print(){
        System.out.println("QTable:");
        Layer.printArray(this.qTable);
        System.out.println("");
    }

    public double getQValue(Vector2i state, int action) {
        return qTable[state.x][state.y][action];
    }

    public void increaseQValue(Vector2i state, int action, double delta) {
        qTable[state.x][state.y][action] += delta;
    }

    public void setQValue(Vector2i state, int action, double v) {
        qTable[state.x][state.y][action] = v;
    }

    public int randomAction() {
        return (int)(Math.random() * ACTIONS);
    }

    public int getBestAction(Vector2i state) {
        return ArrayTools.indexOfHighestValue(qTable[state.x][state.y]);
    }

    public double getHighestQValue(Vector2i state) {
        double max = -10000;
        for(double d:qTable[state.x][state.y]){
            if(d > max) max = d;
        }
        return max;
    }


}
