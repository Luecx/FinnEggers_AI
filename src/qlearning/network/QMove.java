package qlearning.network;

/**
 * Created by finne on 11.02.2018.
 */
public class QMove {

    private QState prevState;
    private int decision;
    private QState nextState;
    private double reward;

    public QMove(QState prevState, int decision, QState nextState, double reward) {
        this.prevState = prevState;
        this.decision = decision;
        this.nextState = nextState;
        this.reward = reward;
    }

    public QState getPrevState() {
        return prevState;
    }

    public void setPrevState(QState prevState) {
        this.prevState = prevState;
    }

    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public QState getNextState() {
        return nextState;
    }

    public void setNextState(QState nextState) {
        this.nextState = nextState;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }
}
