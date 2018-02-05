package qlearning.neuralnetwork.qgames;

import network.layers.Layer;
import qlearning.neuralnetwork.QGame;
import qlearning.neuralnetwork.QState;

/**
 * Created by finne on 05.02.2018.
 */
public class Cheese1D extends QGame {

    int size;
    int currentIndex;
    int negativeIndex;
    int positiveIndex;


    public Cheese1D(int size, int negativeIndex, int positiveIndex) {
        super(1,1,size,2);
        this.currentIndex = size / 2;
        this.size = size;
        this.negativeIndex = negativeIndex;
        this.positiveIndex = positiveIndex;
        reset();
    }

    @Override
    public void performAction(int action) {
        this.currentState[0][0][currentIndex] = 0;
        if(action == 0) {
            this.currentIndex --;
        }else{
            this.currentIndex ++;
        }
        this.currentState[0][0][currentIndex] = 1;

        if(currentIndex == 0 || currentIndex == size-1 || currentIndex == negativeIndex){
            this.currentStateReward = -1;
            reset();
        }else if(currentIndex == positiveIndex){
            this.currentStateReward = +1;
            reset();
        }
    }

    @Override
    public void printGameState() {
        String t = "#";
        for(int i = 1; i < this.size - 1; i++) {
            if(this.negativeIndex == i){
                t += "#";
            }else if(this.positiveIndex == i) {
                t += "O";
            }else if(this.currentIndex == i) {
                t += "M";
            }else{
                t += "-";
            }
        }
        System.out.print("\r" + t +"#");
    }


    private void reset() {
        this.currentState[0][0][currentIndex] = 0;
        this.currentState[0][0][size / 2] = 1;
    }
}
