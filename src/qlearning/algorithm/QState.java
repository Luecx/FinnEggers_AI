package qlearning.algorithm;

import java.util.ArrayList;

/**
 * Created by finne on 02.02.2018.
 */
public class QState {

    private ArrayList<QAction> actions = new ArrayList<>();
    private double reward;
    private String identifier;

    public QState(double reward) {
        this.reward = reward;
    }

    public QAction highestRewardedAction() {
        QAction m = actions.get(0);
        for(QAction q:actions){
            if(q.getReward() > m.getReward()) {
                m = q;
            }
        }
        return m;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public QAction randomRewardedAction() {
        int index = (int)(Math.random() * (actions.size()));
        return actions.get(index);
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public ArrayList<QAction> getActions() {
        return actions;
    }

    public void addAction(QAction a) {
        actions.add(a);
    }
}
