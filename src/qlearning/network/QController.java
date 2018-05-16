package qlearning.network;

/**
 * Created by finne on 11.02.2018.
 */
public class QController {

    private double learning_rate = 0.05;
    private double threshold = 0.6;
    private double discount_factor = 0.9;

    private QNetwork network;
    private QGame qGame;
    private QSampleBuffer buffer;

    private int buffer_size = 100;
    private int batch_size = 100;

    public QController(QNetwork network, QGame qGame) {
        this.network = network;
        this.qGame = qGame;
        if(!network.getInputDimensions().equals(qGame.getDimension())){
            System.exit(-1);
        }
        this.buffer = new QSampleBuffer(buffer_size);
    }

    public void printActionMap() {
        this.qGame.printActionMap(this.network);
    }


    public void validate(int steps) {
        for(int i = 0; i< steps; i++) {
            int action = network.bestQMove(qGame.getCurrentState());
            qGame.move(action);
            qGame.printState();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getLearning_rate() {
        return learning_rate;
    }

    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getDiscount_factor() {
        return discount_factor;
    }

    public void setDiscount_factor(double discount_factor) {
        this.discount_factor = discount_factor;
    }

    public int getBuffer_size() {
        return buffer_size;
    }

    public void setBuffer_size(int buffer_size) {
        this.buffer_size = buffer_size;
        this.buffer.setMax_size(buffer_size);
    }

    public int getBatch_size() {
        return batch_size;
    }

    public void setBatch_size(int batch_size) {
        this.batch_size = batch_size;
    }

    public void train(int steps) {
        buffer.clear();

        QState cur = qGame.getCurrentState();

        for(int i = 0; i < steps; i++) {

            System.out.println(i);

            int decision;
            if(Math.random() > threshold) {
                decision = (int)(Math.random() * network.getDecisions());
            }else{
                decision = network.bestQMove(cur);
            }

            double reward = qGame.move(decision);

            QState newState = qGame.getCurrentState();

            buffer.push(new QMove(cur, decision, newState,reward));
            if(buffer.size() == buffer.getMax_size()){
                QMove[] batch = buffer.extractRandomBatch(batch_size);
                for(int k = 0; k < 10; k++){
                    for(QMove c:batch){
                        network.trainQMove(c, discount_factor, learning_rate);
                    }
                }
            }
            cur = newState;
        }


    }
}
