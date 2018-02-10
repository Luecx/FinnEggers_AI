package qlearning.basic.dimensions_1;

import network.tools.ArrayTools;
import qlearning.basic.vectors.Vector1i;

import java.util.Arrays;

/**
 * Created by finne on 10.02.2018.
 */
public class QTable {

    private final int STATES, ACTIONS;
    private double[][] qTable;


    public QTable(Vector1i STATES, int ACTIONS) {
        this.STATES = STATES.x;
        this.ACTIONS = ACTIONS;
        qTable = ArrayTools.createRandomArray(STATES.x, ACTIONS, -0.3,0.3);
    }

    public void print(){
        System.out.println("QTable:");
        String format = "%-3s %8s %8s %n";
        for(int i = 0; i < qTable.length; i++) {
            System.out.format(format, i +"",
                    new String(qTable[i][0] + "   ").substring(0,5),
                    new String(qTable[i][1] + "   ").substring(0,5));
        }
        System.out.println("");
    }

    public double getQValue(Vector1i state, int action) {
        return qTable[state.x][action];
    }

    public void increaseQValue(Vector1i state, int action, double delta) {
        qTable[state.x][action] += delta;
    }

    public void setQValue(Vector1i state, int action, double v) {
        qTable[state.x][action] = v;
    }

    public int randomAction() {
        return (int)(Math.random() * ACTIONS);
    }

    public int getBestAction(Vector1i state) {
        return ArrayTools.indexOfHighestValue(qTable[state.x]);
    }

    public double getHighestQValue(Vector1i state) {
        double max = -10000;
        for(double d:qTable[state.x]){
            if(d > max) max = d;
        }
        return max;
    }


}
