package qlearning.neuralnetwork;

/**
 * Created by finne on 05.02.2018.
 */
public class QStateTouple {

    public QState prev_state;
    public QState next_state;
    public int action;

    public QStateTouple(QState prev_state, QState next_state, int action) {
        this.prev_state = prev_state;
        this.next_state = next_state;
        this.action = action;
    }

    public QState getPrev_state() {
        return prev_state;
    }

    public void setPrev_state(QState prev_state) {
        this.prev_state = prev_state;
    }

    public QState getNext_state() {
        return next_state;
    }

    public void setNext_state(QState next_state) {
        this.next_state = next_state;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
