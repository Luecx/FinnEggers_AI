package qlearning.neuralnetwork.qgames;

import network.Network;
import network.NetworkBuilder;
import network.layers.DenseLayer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;
import qlearning.neuralnetwork.QGame;
import qlearning.neuralnetwork.QNetwork;
import qlearning.neuralnetwork.console.Console;
import qlearning.neuralnetwork.qnetworktables.vectors.Vector2i;

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
            case 0: currentIndex.y ++; break;
            case 1: currentIndex.x ++;break;
            case 2: currentIndex.y --;break;
            case 3: currentIndex.x --;break;
        }
        this.currentState[0][currentIndex.x][currentIndex.y] = 1;

        if(currentIndex.x == 0 || currentIndex.x == size.x - 1 ||
                currentIndex.y == 0 || currentIndex.y == size.y - 1 ||
                currentIndex.equals(negativeIndex)){
            this.currentStateReward = -1;
            reset();
        }else if(currentIndex.equals(positiveIndex)){
            this.currentStateReward = +1;
            System.out.println("GReat!");
            reset();
        }
    }

    @Override
    public void printGameState() {
        System.out.print('z');

        Vector2i k = new Vector2i(0,0);
        for(int i = 0; i < this.size.y; i++) {
            String t = "";
            for(int n = 0; n < this.size.x; n++) {
                if(i == 0 || n == 0 || i == size.y - 1 || n == size.x - 1){
                    t += "# ";
                }else {
                    k.x = n;
                    k.y = i;
                    if(k.equals(currentIndex)) t+="M ";
                    else if(k.equals(negativeIndex)) t+="# ";
                    else if(k.equals(positiveIndex)) t+="* ";
                    else t+= ". ";
                }
            }
            System.out.println(t);
        }
    }

    public void printRecommendationField(Network network){
        System.out.print('z');
        Vector2i k = new Vector2i(0,0);
        for(int i = 0; i < this.size.y; i++) {
            String t = "";
            for(int n = 0; n < this.size.x; n++) {
                if(i == 0 || n == 0 || i == size.y - 1 || n == size.x - 1){
                    t += "# ";
                }else {
                    k.x = n;
                    k.y = i;
                    double[][] data = new double[this.size.x][this.size.y];
                    data[n][i] = 1;

                    if(k.equals(negativeIndex)) t+="# ";
                    else if(k.equals(positiveIndex)) t+="* ";
                    //else t+= Arrays.toString(network.calculate(new double[][][]{data})[0][0]) + " ";
                    else t+= ArrayTools.indexOfHighestValue(network.calculate(new double[][][]{data})[0][0]) + " ";
                }
            }
            System.out.println(t);
        }
    }

    private void reset() {
        this.currentState[0][currentIndex.x][currentIndex.y] = 0;

        currentIndex.x = (int)((size.x - 2) * Math.random() + 1);
        currentIndex.y = (int)((size.y - 2) * Math.random() + 1);

        currentIndex.x = size.x / 2;
        currentIndex.y = size.y / 2;

        this.currentState[0][currentIndex.x][currentIndex.y] = 1;

    }

    public static void main(String[] args) throws Exception {
        Cheese2D cheese2d= new Cheese2D(new Vector2i(10,10), new Vector2i(3,4), new Vector2i(6,5), new Vector2i(3,7));

        NetworkBuilder builder = new NetworkBuilder(1,10,10);
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(4).weightsRange(-2,2).biasRange(0,0));
        Network network = builder.buildNetwork();



        QNetwork qNetwork = new QNetwork( network, cheese2d);
        qNetwork.batch_size = 200;
        qNetwork.buffer_size = 1000;
        qNetwork.threshold = 0.9;
        qNetwork.learning_rate = 0.3;


        qNetwork.train(2000);

        Console c = new Console();

        qNetwork.validate(100);
        cheese2d.printRecommendationField(qNetwork.getNetwork());
    }

}
