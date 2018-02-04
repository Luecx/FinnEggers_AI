package qlearning.algorithm;

import java.util.ArrayList;

/**
 * Created by finne on 02.02.2018.
 */
public class QTable {

    public ArrayList<QState> states = new ArrayList<>();

    public double threshold = 0.9;
    public double learning_rate = 0.2;
    public double discount_factor = 0.9;

    public QTable() {
    }

    public ArrayList<QState> getStates() {
        return states;
    }

    public void addState(QState s) {
        states.add(s);
    }

    public void setStates(ArrayList<QState> states) {
        this.states = states;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public QState execute(QState state){
        QAction action;
        double r = Math.random();
        if(r > threshold) {
            action = state.randomRewardedAction();
        }else{
            action = state.highestRewardedAction();
        }
        QState nextState = action.getNextState();
        action.setReward(
                action.getReward() + learning_rate * (
                        nextState.getReward() +
                        discount_factor * nextState.highestRewardedAction().getReward() -
                        action.getReward()
                )
        );
        return nextState;
    }

    public void print() {
        for(int i = 0; i < this.states.size(); i++) {
            String ret = i + "      ";
            for(QAction action:states.get(i).getActions()){
                ret += (action.getReward() + "00000").substring(0,4) + "   ";
            }
            System.out.println(ret);
        }
    }
}
