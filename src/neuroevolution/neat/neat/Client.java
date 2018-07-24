package neuroevolution.neat.neat;


import neuroevolution.neat.calculation.Calculator;
import neuroevolution.neat.genes.Genome;
import neuroevolution.neat.species.Specie;

public class Client extends Genome {

    private double score;
    private Specie specie;

    private Calculator calculator;

    public Client(Neat neat) {
        super(neat);
    }

    public void generateCalculator(){
        this.calculator = new Calculator(this.getNode_genes(), this.getConnection_genes());
    }

    public double[] calculate(double... in){
        if(this.calculator != null){
            return this.calculator.calculate(in);
        }
        return null;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Specie getSpecie() {
        return specie;
    }

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }
}
