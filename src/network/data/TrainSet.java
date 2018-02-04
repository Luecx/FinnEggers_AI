package network.data;

import network.tools.ArrayTools;

import java.util.ArrayList;

/**
 * Created by finne on 26.01.2018.
 */
public class TrainSet {

    public final int INPUT_DEPTH, INPUT_WIDTH, INPUT_HEIGHT;
    public final int OUTPUT_DEPTH, OUTPUT_WIDTH, OUTPUT_HEIGHT;

    //double[][][][] <- index1: 0 = input, 1 = output || index2-4: index of element
    private ArrayList<double[][][][]> data = new ArrayList<>();

    public TrainSet(int INPUT_DEPTH, int INPUT_WIDTH, int INPUT_HEIGHT, int OUTPUT_DEPTH, int OUTPUT_WIDTH, int OUTPUT_HEIGHT) {
        this.INPUT_DEPTH = INPUT_DEPTH;
        this.INPUT_WIDTH = INPUT_WIDTH;
        this.INPUT_HEIGHT = INPUT_HEIGHT;
        this.OUTPUT_DEPTH = OUTPUT_DEPTH;
        this.OUTPUT_WIDTH = OUTPUT_WIDTH;
        this.OUTPUT_HEIGHT = OUTPUT_HEIGHT;
    }

    public void addData(double[][][] in, double[][][] expected) {
        if (in.length != INPUT_DEPTH ||
                in[0].length != INPUT_WIDTH ||
                in[0][0].length != INPUT_HEIGHT ||
                expected.length != OUTPUT_DEPTH ||
                expected[0].length != OUTPUT_WIDTH ||
                expected[0][0].length != OUTPUT_HEIGHT) return;
        data.add(new double[][][][]{in, expected});
    }

    public TrainSet extractBatch(int size) {
        if (size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(INPUT_DEPTH, INPUT_WIDTH, INPUT_HEIGHT, OUTPUT_DEPTH, OUTPUT_WIDTH, OUTPUT_HEIGHT);
            Integer[] ids = ArrayTools.randomValues(0, this.size() - 1, size);
            for (Integer i : ids) {
                set.addData(this.getInput(i), this.getOutput(i));
            }
            return set;
        } else return this;
    }


    @Override
    public String toString() {
        return "TrainSet{" +
                "INPUT_DEPTH=" + INPUT_DEPTH +
                ", INPUT_WIDTH=" + INPUT_WIDTH +
                ", INPUT_HEIGHT=" + INPUT_HEIGHT +
                ", OUTPUT_DEPTH=" + OUTPUT_DEPTH +
                ", OUTPUT_WIDTH=" + OUTPUT_WIDTH +
                ", OUTPUT_HEIGHT=" + OUTPUT_HEIGHT +
                ", network.network.data=" + data +
                '}';
    }

    public int size() {
        return data.size();
    }

    public double[][][] getInput(int index) {
        if (index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[][][] getOutput(int index) {
        if (index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    public int getINPUT_DEPTH() {
        return INPUT_DEPTH;
    }

    public int getINPUT_WIDTH() {
        return INPUT_WIDTH;
    }

    public int getINPUT_HEIGHT() {
        return INPUT_HEIGHT;
    }

    public int getOUTPUT_DEPTH() {
        return OUTPUT_DEPTH;
    }

    public int getOUTPUT_WIDTH() {
        return OUTPUT_WIDTH;
    }

    public int getOUTPUT_HEIGHT() {
        return OUTPUT_HEIGHT;
    }
}


