package qlearning.network_v3.games;

import network.tools.ArrayTools;
import qlearning.basic.console.Console;
import qlearning.network_v2.QNetwork;
import qlearning.network_v3.QController;
import qlearning.network_v3.QGame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by finne on 17.05.2018.
 */
public class CheeseGame extends QGame {

    public ArrayList<int[]> positiveRewards;
    public ArrayList<int[]> negativeRewards;
    private int[] player_position;

    public CheeseGame(int WIDTH, int HEIGHT) {
        super(1, WIDTH, HEIGHT, 4);
    }

    protected void init_variables() {
        positiveRewards = new ArrayList<>();
        negativeRewards = new ArrayList<>();
        player_position = new int[2];
        player_position[0] = -1;
        player_position[1] = -1;
    }

    @Override
    public void reset() {

        player_position = new int[2];

        player_position[0] = -1;
        player_position[1] = -1;
        while (field_index() != 0) {
            this.player_position[0] = (int) (Math.random() * WIDTH);
            this.player_position[1] = (int) (Math.random() * HEIGHT);
        }

    }

    @Override
    protected double[][][] getInputData() {
        double[][][] ar = new double[1][WIDTH][HEIGHT];
        ar[0][player_position[0]][player_position[1]] = 1;
        return ar;
    }

    private int field_index() {
        if (player_position[0] < 0 || player_position[0] >= WIDTH || player_position[1] < 0 || player_position[1] >= HEIGHT) {
            return -1;
        }
        for (int[] v : negativeRewards) {
            if (v[0] == player_position[0] && v[1] == player_position[1]) {
                return -1;
            }
        }
        for (int[] v : positiveRewards) {
            if (v[0] == player_position[0] && v[1] == player_position[1]) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    protected double move(int output_index) {
        switch (output_index) {
            case 0:
                player_position[0]++;
                break;
            case 1:
                player_position[1]++;
                break;
            case 2:
                player_position[0]--;
                break;
            case 3:
                player_position[1]--;
                break;
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


    @Override
    protected char getChar(int d, int w, int h) {
        for (int[] v : negativeRewards) {
            if (v[0] == w && v[1] == h) {
                return '#';
            }
        }
        for (int[] v : positiveRewards) {
            if (v[0] == w && v[1] == h) {
                return '$';
            }
        }
        if (w == player_position[0] && h == player_position[1]) {
            return 'F';
        }
        return '.';
    }

    public static void main(String[] args) {
        CheeseGame game = new CheeseGame(20, 20);

        for(int i = 1; i < game.WIDTH; i+=2){
            for(int n = 1; n < game.HEIGHT; n+=2){
                game.negativeRewards.add(new int[]{i, n});
            }
        }

        for (int i = 0; i < 50; i++) {
            int x = -1 + 2 * (int) (Math.random() * game.WIDTH / 2);
            int y = (int) (Math.random() * game.HEIGHT);
            game.negativeRewards.add(new int[]{x, y});
        }for (int i = 0; i < 50; i++) {
            int x = -1 + 2 * (int) (Math.random() * game.WIDTH / 2);
            int y = (int) (Math.random() * game.HEIGHT);
            game.negativeRewards.add(new int[]{y,x});
        }

        game.printMap(null);

        game.reset();

        QController controller = new QController(game);
        controller.train(4000);

        Console c = new Console();
        controller.validate(200);
    }
}
