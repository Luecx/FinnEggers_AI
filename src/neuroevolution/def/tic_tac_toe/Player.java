package neuroevolution.def.tic_tac_toe;

import network.Network;
import network.NetworkBuilder;
import network.layers.DenseLayer;
import network.layers.TransformationLayer;
import neuroevolution.def.GeneticClient;

import java.util.ArrayList;

public class Player implements GeneticClient {

    private Network network;
    private int score;

    private int wins;
    private int loses;
    private int draws;

    public Player() {
        NetworkBuilder network_builder = new NetworkBuilder(1,3,3);
        network_builder.addLayer(new TransformationLayer());
        network_builder.addLayer(new DenseLayer(15));
        network_builder.addLayer(new DenseLayer(15));
        network_builder.addLayer(new DenseLayer(1));
        this.network = network_builder.buildNetwork();
    }

    public void let_npc_move(Board b){
        int id = b.getCurrent_id();

        this.minimax(b,1);
        int[] m = this.savedMove;
        if(m != null){
            b.place(m[0],m[1]);
        }

    }

    @Override
    public double getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    private double evaluate(Board b){
        double[][][] in = new double[1][3][3];
        for(int i = 0; i < 3; i++){
            for(int n = 0; n< 3; n++){
                in[0][i][n] = b.getFields()[i][n];
            }
        }
        return this.network.calculate(in)[0][0][0];
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int[] savedMove;
    private int max_depth;

    private void minimax(Board b, int depth){
        savedMove = null;
        max_depth = depth;
        if(b.getCurrent_id() == 1){
            max(b, 1, depth);
        }else{
            min(b, -1, depth);
        }
    }

    private double max(Board b, int spieler, int tiefe) {
        ArrayList<int[]> moves = b.possibleMoves();
        if (tiefe == 0 || moves.size() == 0)
            return evaluate(b);
        double maxWert = -10;
        for(int[] move:moves){
            Board g = b.copyBoard();
            g.place(move[0], move[1]);
            double wert = min(g, -spieler, tiefe-1);
            if (wert > maxWert) {
                maxWert = wert;
                if (tiefe == max_depth){
                    savedMove = move;
                }
            }
        }
        return maxWert;
    }

    private double min(Board b, int spieler, int tiefe) {
        ArrayList<int[]> moves = b.possibleMoves();
        if (tiefe == 0 || moves.size() == 0)
            return evaluate(b);
        double minWert = 10;
        for(int[] move:moves){
            Board g = b.copyBoard();
            g.place(move[0], move[1]);
            double wert = max(g, -spieler, tiefe-1);
            if (wert < minWert) {
                minWert = wert;
                if (tiefe == max_depth)
                    savedMove = move;
            }
        }
        return minWert;
    }
}
