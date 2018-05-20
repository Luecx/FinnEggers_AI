package qlearning.network_v3;

import network.Network;
import network.layers.Layer;
import network.tools.ArrayTools;

/**
 * Created by finne on 16.05.2018.
 */
public abstract class QGame {

    public final int DEPTH;
    public final int WIDTH;
    public final int HEIGHT;

    public final int OUTPUT_SIZE;

    public QGame(int DEPTH, int WIDTH, int HEIGHT, int OUTPUT_SIZE) {
        this.DEPTH = DEPTH;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
        init_variables();
        this.reset();
    }
    protected abstract void init_variables();

    public abstract void reset();

    protected abstract double[][][] getInputData();

    protected abstract double move(int output_index);

    protected abstract char getChar(int d, int w, int h);

    public void printMap(Network network){
        for(int i = 0; i < DEPTH; i++){
            for (int n = 0; n < WIDTH + 2; n++) {
                System.out.print("# ");
            }
            for (int j = 0; j < HEIGHT; j++) {
                String s = "\n# ";
                for (int n = 0; n < WIDTH; n++) {
                    s += network != null ? network_move(network) + " ":getChar(i,n,j) + " ";
                }
                System.out.print(s + "#");
            }
            System.out.println();
            for (int n = 0; n < WIDTH + 2; n++) {
                System.out.print("# ");
            }
        }
    }

    public void printNetworkInput() {
        Layer.printArray(getInputData());
    }

    public int network_move(Network network){
        return ArrayTools.indexOfHighestValue(network.calculate(this.getInputData())[0][0]);
    }

}
