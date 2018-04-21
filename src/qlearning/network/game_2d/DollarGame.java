package qlearning.network.game_2d;

import network.Network;
import network.NetworkBuilder;
import network.functions.activation.LeakyReLU;
import network.layers.DenseLayer;
import network.layers.Layer;
import network.layers.TransformationLayer;
import qlearning.basic.console.Console;
import qlearning.basic.dimensions_2_2.QTable;
import qlearning.basic.vectors.Vector2i;
import qlearning.basic.vectors.Vector3i;
import qlearning.network.QController;
import qlearning.network.QGame;
import qlearning.network.QNetwork;
import qlearning.network.QState;

import java.util.Arrays;

/**
 * Created by finne on 11.02.2018.
 */
public class DollarGame extends QGame {

    private Vector2i currentIndex;
    private Vector2i[] positive_reward;
    private Vector2i[] negative_reward;

    public DollarGame(Vector2i dimension,
                      Vector2i[] positiveReward,
                      Vector2i[] negativeReward) {
        super(new Vector3i(1, dimension.x, dimension.y));
        this.positive_reward = positiveReward;
        this.negative_reward = negativeReward;
        this.currentIndex = new Vector2i(0, 0);
        reset();
    }

    public double move(int action) {
        this.currentState.getIn()[0][currentIndex.x][currentIndex.y] = 0;
        switch (action) {
            case 0:
                currentIndex.x++;
                break;
            case 1:
                currentIndex.y++;
                break;
            case 2:
                currentIndex.x--;
                break;
            case 3:
                currentIndex.y--;
                break;
        }

        if (isPositiveState(currentIndex)) {
            reset();
            return 500;
        } else if (isNegativeState(currentIndex) ||
                currentIndex.x == 0 || currentIndex.x == dimension.y - 1 ||
                currentIndex.y == 0 || currentIndex.y == dimension.z - 1) {
            reset();
            return -500;
        }
        this.currentState.getIn()[0][currentIndex.x][currentIndex.y] = 1;
        return 0;
    }

    private boolean isNegativeState(Vector2i state) {
        for (Vector2i v : negative_reward) {
            if (v.equals(state)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPositiveState(Vector2i state) {
        for (Vector2i v : positive_reward) {
            if (v.equals(state)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.currentState.getIn()[0][currentIndex.x][currentIndex.y] = 0;
        currentIndex = new Vector2i(
                (int) (1 + Math.random() * (this.dimension.y - 2)),
                (int) (1 + Math.random() * (this.dimension.z - 2)));
        while (isNegativeState(currentIndex) || isPositiveState(currentIndex)) {
            currentIndex = new Vector2i(
                    (int) (1 + Math.random() * (this.dimension.y - 2)),
                    (int) (1 + Math.random() * (this.dimension.z - 2)));
        }
        this.currentState.getIn()[0][currentIndex.x][currentIndex.y] = 1;
    }

    public void printState() {
        System.out.println("z");
        for (int k = 0; k < dimension.z; k++) {
            String txt = "";
            for (int i = 0; i < dimension.y; i++) {
                Vector2i state = new Vector2i(i, k);
                if (state.x == 0 || state.x == dimension.y - 1 ||
                        state.y == 0 || state.y == dimension.z - 1 ||
                        isNegativeState(state)) {
                    txt += "# ";
                } else if (isPositiveState(state)) {
                    txt += "$ ";
                } else if (state.equals(this.currentIndex)) {
                    txt += "P ";
                } else {
                    txt += ". ";
                }
            }
            System.out.println(txt);
        }

    }

    private QState createQState(Vector2i position) {
        QState qState = new QState(new double[1][this.dimension.y][this.dimension.z]);
        qState.getIn()[0][position.x][position.y] = 1;
        return qState;
    }

    @Override
    public void printActionMap(QNetwork network) {
        for (int k = 0; k < dimension.z; k++) {
            String txt = "";
            for (int i = 0; i < dimension.y; i++) {
                Vector2i state = new Vector2i(i, k);
                if (state.x == 0 || state.x == dimension.y - 1 ||
                        state.y == 0 || state.y == dimension.z - 1 ||
                        isNegativeState(state)) {
                    txt += "# ";
                } else if (isPositiveState(state)) {
                    txt += "$ ";
                } else {
                    txt += network.bestQMove(createQState(state)) + " ";
                }
            }
            System.out.println(txt);
        }

    }

    public static void main(String[] args) throws InterruptedException {

        NetworkBuilder builder = new NetworkBuilder(1, 10, 10);
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(4).weightsRange(-1, 1).biasRange(0, 0));

        Network neural = builder.buildNetwork();

        QNetwork qNetwork = new QNetwork(neural);
        DollarGame qGame = new DollarGame(
                new Vector2i(10, 10),
                new Vector2i[]{
                        new Vector2i(4, 4)
                },
                new Vector2i[0]);
        QController controller = new QController(qNetwork, qGame);
        controller.setBatch_size(300);
        controller.setBuffer_size(500);

        controller.setThreshold(0.6);
        controller.setDiscount_factor(0.9);
        controller.setLearning_rate(1);

        controller.train(50000);


        Console c = new Console();

        controller.validate(50);
    }

}
