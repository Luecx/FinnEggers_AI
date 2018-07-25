package qlearning.network_v2;

import network.Network;
import network.NetworkBuilder;
import network.functions.activation.TanH;
import network.layers.DenseLayer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;
import qlearning.basic.console.Console;

import java.util.Arrays;

/**
 * Created by finne on 16.05.2018.
 */
public class CheeseGame {

    private int width = 6;
    private int height = 6;

    private int cheese_x = 3;
    private int cheese_y = 2;

    private int player_x = -1;
    private int player_y = -1;

    public CheeseGame(int width, int height, int cheese_x, int cheese_y) {
        this.width = width;
        this.height = height;
        this.cheese_x = cheese_x;
        this.cheese_y = cheese_y;
        this.reset();
    }

    public void reset() {
        while (field_index() != 0){
            this.player_x = (int) (Math.random() * width);
            this.player_y = (int) (Math.random() * height);
        }
    }

    /**
     * 1 = right
     * 2 = bottom
     * 3 = left
     * 4 = top
     *
     * @param index
     */
    public double move(int index) {
        switch (index) {
            case 0:
                player_x++; break;
            case 1:
                player_y++; break;
            case 2:
                player_x--; break;
            case 3:
                player_y--; break;
        }
        int i = field_index();
        switch (i) {
            case -1:
                reset();
                return -0.99;
            case 1:
                reset();
                return 0.99;
        }
        return -0.1;
    }

    public void printMap(Network... network) {
        if(network.length != 0){
            for (int n = 0; n < width + 2; n++) {
                System.out.print("# ");
            }
            for (int i = 0; i < height; i++) {
                String s = "\n# ";
                for (int n = 0; n < width; n++) {
                    if (n == cheese_x && i == cheese_y) {
                        s += "$ ";
                    } else {
                        double[][][] in = new double[1][width][height];
                        in[0][n][i] = 1;
                        s += ArrayTools.indexOfHighestValue(network[0].calculate(in)[0][0]) + " ";
                    }
                }
                System.out.print(s + "#");
            }
            System.out.println();
            for (int n = 0; n < width + 2; n++) {
                System.out.print("# ");
            }
            System.out.println();
            return;
        }
        for (int n = 0; n < width + 2; n++) {
            System.out.print("# ");
        }
        for (int i = 0; i < height; i++) {
            String s = "\n# ";
            for (int n = 0; n < width; n++) {
                if (n == player_x && i == player_y) {
                    s += "F ";
                } else if (n == cheese_x && i == cheese_y) {
                    s += "$ ";
                } else {
                    s += ". ";
                }
            }
            System.out.print(s + "#");
        }
        System.out.println();
        for (int n = 0; n < width + 2; n++) {
            System.out.print("# ");
        }
        System.out.println();
    }

    public int field_index() {
        if (player_x < 0 || player_x >= width || player_y < 0 || player_y >= height) {
            return -1;
        }
        if (cheese_x == player_x && cheese_y == player_y) return 1;
        return 0;
    }

    public static void main(String[] args) {
        CheeseGame game = new CheeseGame(8,8,4,4);
        QNetwork network = new QNetwork(game);
        network.train(5000);

        Console c = new Console();

        game.printMap(network.network);
        network.validate(100);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCheese_x() {
        return cheese_x;
    }

    public int getCheese_y() {
        return cheese_y;
    }

    public int getPlayer_x() {
        return player_x;
    }

    public int getPlayer_y() {
        return player_y;
    }
}
