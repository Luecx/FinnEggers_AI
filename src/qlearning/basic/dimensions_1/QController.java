package qlearning.basic.dimensions_1;

import qlearning.basic.vectors.Vector1i;

/**
 * Created by finne on 10.02.2018.
 */
public class QController {

    private QGame qGame;
    private QTable qTable;

    private double threshold = 0.5;
    private double learning_rate = 0.5;
    private double discount_factor = 0.9;

    public QController(QGame qGame) {
        this.qGame = qGame;
        this.qTable = new QTable(qGame.getSize(), 2);
    }

    public void printActionMap() {
        this.qGame.printActionMap(this.getqTable());
    }

    public void train(int steps){
        for(int i = 0; i< steps; i++) {
            int action;
            Vector1i state = qGame.getState();

            if(Math.random() > threshold) {
                action = qTable.randomAction();
            }else{
                action = qTable.getBestAction(state);
            }

            double reward = qGame.move(action);
            Vector1i nextState = qGame.getState();

            double oldQValue = qTable.getQValue(state, action);

            double newValue = oldQValue + learning_rate * (
                    reward + discount_factor * qTable.getHighestQValue(nextState)
                            -oldQValue);

            qTable.setQValue(state, action, newValue);
        }
    }

    public void validate(int steps) {
        for(int i = 0; i< steps; i++) {
            int action = qTable.getBestAction(qGame.getState());
            qGame.move(action);
            qGame.printState();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        QGame game = new QGame(15,4,12);
        QController controller = new QController(game);

        controller.train(10000);
        controller.printActionMap();

        controller.getqTable().print();
        controller.getqGame().reset();

        controller.validate(100);
    }

    public QGame getqGame() {
        return qGame;
    }

    public void setqGame(QGame qGame) {
        this.qGame = qGame;
    }

    public QTable getqTable() {
        return qTable;
    }

    public void setqTable(QTable qTable) {
        this.qTable = qTable;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getLearning_rate() {
        return learning_rate;
    }

    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public double getDiscount_factor() {
        return discount_factor;
    }

    public void setDiscount_factor(double discount_factor) {
        this.discount_factor = discount_factor;
    }
}
