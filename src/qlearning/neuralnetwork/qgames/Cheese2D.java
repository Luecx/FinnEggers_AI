package qlearning.neuralnetwork.qgames;

import qlearning.neuralnetwork.QGame;

/**
 * Created by finne on 05.02.2018.
 */
public class Cheese2D extends QGame {


    Vector2i size;
    Vector2i currentIndex;
    Vector2i negativeIndex;
    Vector2i positiveIndex;


    public Cheese2D(Vector2i size, Vector2i currentIndex, Vector2i negativeIndex, Vector2i positiveIndex) {
        super(1,size.x, size.y, 4);
        this.size = size;
        this.currentIndex = currentIndex;
        this.negativeIndex = negativeIndex;
        this.positiveIndex = positiveIndex;
        reset();
    }


    @Override
    public void performAction(int action) {
        this.currentState[0][currentIndex.x][currentIndex.y] = 0;
        currentStateReward = 0;

        switch (action){
            case 0: currentIndex.y ++;
            case 1: currentIndex.x ++;
            case 2: currentIndex.y --;
            case 3: currentIndex.x --;
        }
        this.currentState[0][currentIndex.x][currentIndex.y] = 1;

        if(currentIndex.x == 0 || currentIndex.x == size.x - 1 ||
                currentIndex.y == 0 || currentIndex.y == size.y - 1 ||
                currentIndex.equals(negativeIndex)){
            this.currentStateReward = -1;
            reset();
        }else if(currentIndex == positiveIndex){
            this.currentStateReward = +1;
            reset();
        }
    }

    private void reset() {
        this.currentState[0][currentIndex.x][currentIndex.y] = 0;

        currentIndex.x = (int)((size.x - 2) * Math.random() + 1);
        currentIndex.y = (int)((size.y - 2) * Math.random() + 1);

        while(currentIndex.equals(negativeIndex) == true || currentIndex.equals(positiveIndex) == true){
            currentIndex.x = (int)((size.x - 2) * Math.random() + 1);
            currentIndex.y = (int)((size.y - 2) * Math.random() + 1);
        }

        this.currentState[0][currentIndex.x][currentIndex.y] = 1;

    }

}
