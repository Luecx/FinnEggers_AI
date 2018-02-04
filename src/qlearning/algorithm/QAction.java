package qlearning.algorithm;

/**
 * Created by finne on 02.02.2018.
 */
public class QAction {

    private double reward = 0;
    private QState nextState;

    public QAction(QState nextState) {
        this.nextState = nextState;
        this.reward = Math.random() * 2 - 1;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public QState getNextState() {
        return nextState;
    }

    public void setNextState(QState nextState) {
        this.nextState = nextState;
    }
}
