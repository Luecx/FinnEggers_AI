package qlearning.algorithm;

/**
 * Created by finne on 04.02.2018.
 */
public class QGame {


    private QTable qTable = new QTable();

    private QState currentState;

    public QGame(int size, int negativeIndex, int positiveIndex) {
        for (int i = 0; i < size; i++) {
            QState qState = new QState(0);
            qState.setIdentifier("" +i);
            if (i == 0 || i == size - 1) qState.setReward(-1);
            if (i == negativeIndex) qState.setReward(-1);
            if (i == positiveIndex) qState.setReward(1);

            if (i == size / 2) {
                currentState = qState;
            }
            if (i == 0) {
                qState.addAction(new QAction(qState));
            } else {
                QState prevState = qTable.states.get(qTable.states.size() - 1);
                prevState.addAction(new QAction(qState));
                qState.addAction(new QAction(prevState));
            }
            if (i == size - 1) {
                qState.addAction(new QAction(qState));
            }

            qTable.addState(qState);
        }
    }

    public void print() {
        String t = "";
        int index = 0;
        for (QState qState : qTable.getStates()) {
            if(qState.equals(currentState)){
                t += "M";
            }else{
                switch ((int) qState.getReward()) {
                    case -1:
                        t += "#";
                        break;
                    case 0:
                        t += "-";
                        break;
                    case 1:
                        t += "O";
                        break;
                }
            }
            index++;
        }
        System.out.print("\r" + t);
    }

    public boolean play() {
        currentState = qTable.execute(currentState);
        if(currentState.getReward() != 0){
            currentState = qTable.states.get(qTable.states.size() / 2);
            return true;
        }return false;
    }

    public void validate(int games) {
        try{
            qTable.threshold = 1;
            int counter = 0;
            while(counter < games) {
                this.print();
                Thread.sleep(500);
                counter += play() ? 1:0;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void train(int it) {
        qTable.threshold = 0.6;
        for (int i = 0; i < it; i++) {
            play();
        }
        this.qTable.print();
    }

    public static void main(String[] args) {
        QGame game = new QGame(15, 3, 12);
        game.train(1000);

        game.validate(5);
    }
}
