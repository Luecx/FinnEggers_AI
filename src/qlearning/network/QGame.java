package qlearning.network;

import network.tools.ArrayTools;
import qlearning.basic.dimensions_2_2.QTable;
import qlearning.basic.vectors.Vector2i;
import qlearning.basic.vectors.Vector3i;

/**
 * Created by finne on 11.02.2018.
 */
public abstract class QGame {

    protected QState currentState;
    protected Vector3i dimension;


    public QGame(Vector3i dimension) {
        this.currentState = new QState(new double[dimension.x][dimension.y][dimension.z]);
        this.dimension = dimension;
    }

    public QState getCurrentState() {
        return new QState(ArrayTools.copyArray(currentState.getIn()));
    }

    /**
     * Change the currentState.
     * Return the reward
     * @return
     */
    public abstract double move(int action);

    public abstract void printState();

    public void setCurrentState(QState currentState) {
        this.currentState = currentState;
    }

    public abstract void printActionMap(QNetwork qNetwork);

    public Vector3i getDimension() {
        return dimension;
    }
}

