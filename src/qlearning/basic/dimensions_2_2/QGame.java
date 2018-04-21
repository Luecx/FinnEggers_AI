package qlearning.basic.dimensions_2_2;


import qlearning.basic.vectors.Vector2i;

/**
 * Created by finne on 10.02.2018.
 */
public class QGame {

    private Vector2i size;
    private Vector2i state;
    private Vector2i[] positive_reward;
    private Vector2i[] negative_reward;

    public QGame(Vector2i size, Vector2i[] positive_reward, Vector2i[] negative_reward) {
        this.size = size;
        this.positive_reward = positive_reward;
        this.negative_reward = negative_reward;
        this.reset();
    }

    public double move(int action) {
        switch (action) {
            case 0:
                state.x++;
                break;
            case 1:
                state.y++;
                break;
            case 2:
                state.x--;
                break;
            case 3:
                state.y--;
                break;
        }
        if (isPositiveState(state)) {
            reset();
            return 1;
        } else if (isNegativeState(state) ||
                state.x == 0 || state.x == size.x - 1 ||
                state.y == 0 || state.y == size.y - 1) {
            reset();
            return -1;
        }
        return 0;
    }

    public void reset() {
        state = new Vector2i(
                (int) (1 + Math.random() * (this.size.x - 2)),
                (int) (1 + Math.random() * (this.size.x - 2)));
        while(isNegativeState(state) || isPositiveState(state)) {
            state = new Vector2i(
                    (int) (1 + Math.random() * (this.size.x - 2)),
                    (int) (1 + Math.random() * (this.size.x - 2)));
        }
    }

    private boolean isNegativeState(Vector2i state){
        for(Vector2i v:negative_reward){
            if(v.equals(state)){
                return true;
            }
        }
        return false;
    }

    private boolean isPositiveState(Vector2i state){
        for(Vector2i v:positive_reward){
            if(v.equals(state)){
                return true;
            }
        }
        return false;
    }

    public Vector2i getSize() {
        return size;
    }

    public Vector2i getState() {
        return new Vector2i(state.x, state.y);
    }

    public void printState() {
        System.out.println("z");
        for (int k = 0; k < size.y; k++) {
            String txt = "";
            for (int i = 0; i < size.x; i++) {
                Vector2i state = new Vector2i(i, k);
                if (state.x == 0 || state.x == size.x - 1 ||
                        state.y == 0 || state.y == size.y - 1 ||
                        isNegativeState(state)) {
                    txt += "# ";
                } else if (isPositiveState(state)) {
                    txt += "$ ";
                } else if (state.equals(this.state)) {
                    txt += "P ";
                } else {
                    txt += ". ";
                }
            }
            System.out.println(txt);
        }

    }

    public void printActionMap(QTable qTable) {
        System.out.println("z");
        for (int k = 0; k < size.y; k++) {
            String txt = "";
            for (int i = 0; i < size.x; i++) {
                Vector2i state = new Vector2i(i, k);
                if (state.x == 0 || state.x == size.x - 1 ||
                        state.y == 0 || state.y == size.y - 1 ||
                        isNegativeState(state)) {
                    txt += "# ";
                } else if (isPositiveState(state)) {
                    txt += "$ ";
                } else {
                    txt += qTable.getBestAction(state) + " ";
                }
            }
            System.out.println(txt);
        }
    }
}
