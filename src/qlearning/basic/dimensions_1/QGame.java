package qlearning.basic.dimensions_1;


import qlearning.basic.vectors.Vector1i;

/**
 * Created by finne on 10.02.2018.
 */
public class QGame {

    private Vector1i size;
    private Vector1i state;
    private Vector1i positive_reward;
    private Vector1i negative_reward;

    public QGame(int size, int positive_reward, int negative_reward) {
        this.size = new Vector1i(size);
        this.positive_reward = new Vector1i(positive_reward);
        this.negative_reward = new Vector1i(negative_reward);
        this.state = new Vector1i(this.size.x / 2);
    }

    public double move(int action) {
        if(action == 0) {
            state.x --;
        }else{
            state.x ++;
        }
        if(state.equals(positive_reward)){
            reset();
            return 1;
        }else if(state.equals(negative_reward) || state.x == 0 || state.x == size.x-1){
            reset();
            return -1;
        }
        return 0;
    }

    public void reset() {
        this.state = new Vector1i(this.size.x / 2);
    }

    public Vector1i getSize() {
        return size;
    }

    public Vector1i getState() {
        return new Vector1i(state.x);
    }

    public Vector1i getPositive_reward() {
        return positive_reward;
    }

    public Vector1i getNegative_reward() {
        return negative_reward;
    }

    public void printState() {
        String txt = "z";
        for(int i = 0; i < size.x; i++) {
            Vector1i state = new Vector1i(i);

            if(state.x == 0 ||state.x == size.x-1 || state.equals(negative_reward)){
                txt += "# ";
            }else if(state.equals(positive_reward)){
                txt += "$ ";
            }else if(state.equals(this.state)){
                txt += "P ";
            }else{
                txt += ". ";
            }
        }
        System.out.println(txt);
    }

    public void printActionMap(QTable qTable) {
        String txt = "z";
        for(int i = 0; i < size.x; i++) {
            Vector1i state = new Vector1i(i);

            if(state.x == 0 ||state.x == size.x-1 || state.equals(negative_reward)){
                txt += "# ";
            }else if(state.equals(positive_reward)){
                txt += "$ ";
            }else {
                txt += qTable.getBestAction(state) +" ";
            }
        }
        System.out.println(txt);
    }
}
