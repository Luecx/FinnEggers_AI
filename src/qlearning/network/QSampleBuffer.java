package qlearning.network;

import network.tools.ArrayTools;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by finne on 11.02.2018.
 */
public class QSampleBuffer extends Stack<QMove> {

    private int max_size = 500;

    public QSampleBuffer(int max_size) {
        this.max_size = max_size;
    }

    public QSampleBuffer() {
    }

    public QMove[] extractRandomBatch(int size) {
        if(size > this.size()) {

            QMove[] ar = this.toArray(new QMove[0]);
            ar = ArrayTools.extractBatch(ar, new QMove[0],this.size());
            ArrayTools.shuffleArray(ar);
            return ar;
        }else{

            QMove[] ar = this.toArray(new QMove[0]);
            ar = ArrayTools.extractBatch(ar, new QMove[0],size);
            ArrayTools.shuffleArray(ar);
            return ar;
        }
    }

    public void print() {
        System.out.println("QSampleBuffer:        size: " +this.size());
        Iterator<QMove> it = this.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    @Override
    public QMove push(QMove item) {
        while(this.size() >= max_size) pop();
        return super.push(item);
    }

    public int getMax_size() {
        return max_size;
    }

    public void setMax_size(int max_size) {
        this.max_size = max_size;
    }

    public static void main(String[] args) {
        QSampleBuffer b = new QSampleBuffer();
        b.add(new QMove(null, 1, null, 1));
        b.add(new QMove(null, 1, null, 1));
        b.add(new QMove(null, 1, null, 1));
        b.add(new QMove(null, 1, null, 1));

        System.out.println(b.extractRandomBatch(2).length);
    }
}
