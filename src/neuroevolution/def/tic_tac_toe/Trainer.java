package neuroevolution.def.tic_tac_toe;

import neuroevolution.def.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Comparator;

public class Trainer {

    private GeneticAlgorithm algorithm;
    private ArrayList<Player> players = new ArrayList<>();

    public Trainer(int size) {

        this.algorithm = new GeneticAlgorithm();
        this.algorithm.MUTATION_RATE = 0.1;
        this.algorithm.MUTATION_STENGTH = 0.25;
        this.algorithm.AMOUNT_SURVIVORS = 40;

        for(int i = 0; i < size; i++){
            players.add(new Player());
        }
    }

    private void generation(){
        for(Player p:players){
            p.setScore(0);
            p.setWins(0);
            p.setLoses(0);
            p.setDraws(0);
        }
        for(Player p:players){
            for(Player c:players){
                if(p != c){
                    Match m = new Match(p,c);
                    int id = m.play();

                    if(id == 1){
                        p.setWins(p.getWins() + 1);
                        c.setLoses(c.getLoses() + 1);
                    }else if(id == -1){
                        c.setWins(c.getWins() + 1);
                        p.setLoses(p.getLoses() + 1);
                    }else{
                        c.setDraws(c.getDraws() + 1);
                        p.setDraws(p.getDraws() + 1);
                    }
                    p.setScore(players.size() * 2 - p.getLoses());
                    c.setScore(players.size() * 2 - c.getLoses());

//                    p.setScore(players.size() * 2 - p.getLoses());
//                    c.setScore(players.size() * 2- c.getLoses());

//                    p.setScore(p.getDraws());
//                    c.setScore(c.getDraws());

//                    p.setScore(p.getWins() + p.getDraws() - 10 * p.getLoses());
//                    c.setScore(c.getWins() + c.getDraws() - 10 * c.getLoses());
                }
            }
        }
        players.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if(o2.getScore() > o1.getScore()) return 1;
                if(o1.getScore() > o2.getScore()) return -1;
                return 0;
            }
        });

        System.out.println("  ");
        for(Player p:players){
            System.out.format("score: %-8s   wins: %-8s   draws: %-8s   loses: %-8s\n",p.getScore(),p.getWins(),p.getDraws(),p.getLoses());
        }
        algorithm.evolve(players);
    }

    public void evolve(int generations){
        for(int i = 0; i < generations; i++){
            generation();
        }

        Match m = new Match(players.get(0), players.get(1));
        m.validate();
        for(int i = 0; i < 5; i++){
            players.get(i).getNetwork().save_network("res/ttt-net"+i+".txt");
        }
    }

    public static void main(String[] args){
        new Trainer(100).evolve(300);

    }

}
