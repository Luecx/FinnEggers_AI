package programs;

import network.Network;
import network.NetworkBuilder;
import network.data.TrainSet;
import network.functions.activation.LeakyReLU;
import network.functions.activation.ReLU;
import network.functions.activation.Softmax;
import network.functions.error.CrossEntropy;
import network.functions.error.MSE;
import network.layers.ConvLayer;
import network.layers.DenseLayer;
import network.layers.TransformationLayer;
import network.tools.ArrayTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by finne on 14.05.2018.
 */
public class ImageToText2 {

    private Network network;
    private final char[] chars = {'\\', '/',' ', '.', '-', '+', '$' ,'W'};
    private boolean inverse = true;

    public ImageToText2(Network network) {
        this.network = network;
    }

    public ImageToText2() {
        NetworkBuilder builder = new NetworkBuilder(1,5,5);
        builder.addLayer(new ConvLayer(5,3,2,0)
                .setActivationFunction(new LeakyReLU())
                .weightsRange(-1,1)
                .biasRange(0,1));
        builder.addLayer(new TransformationLayer());
        builder.addLayer(new DenseLayer(20)
                .biasRange(0,1)
                .setActivationFunction(new ReLU()));
        builder.addLayer(new DenseLayer(chars.length)
                .biasRange(0,1)
                .setActivationFunction(new Softmax()));
        this.network = builder.buildNetwork();
        this.network.setErrorFunction(new CrossEntropy());
    }

    private double[][][] extract(BufferedImage img, int x, int y, int w, int h){
        double[][][] vals = new double[1][w][h];
        for(int i = x; i < x + w; i++){
            for(int n = y; n < y+h; n++){
                if(i < img.getWidth() && n < img.getHeight()){
                         vals[0][i - x][n - y] = new Color(img.getRGB(i,n)).getRed() / 255d;
                }else{
                    return null;
                }
            }
        }
        return vals;
    }

    public void train(String img) {
        try {
            TrainSet trainSet = new TrainSet(1,5,5,1,1,chars.length);
            BufferedImage bufferedImage = ImageIO.read(new File(img));
            for(int i = 0; i < chars.length; i++){
                System.out.println(trainSet);
                double[][][] in = null;
                int index = 0;
                while((in = extract(bufferedImage, index * 5, i * 5, 5, 5)) != null){
                    double[][][] out = new double[1][1][chars.length];
                    out[0][0][i] = 1;
                    trainSet.addData(in, out);
                    index++;
                }
            }

            System.out.println(trainSet);

            network.train(trainSet,5000,10,0.001);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void calculate(String img, String result_file){
        try {
            BufferedImage b_img = ImageIO.read(new File(img));
            int out_w = b_img.getWidth() / 5;
            int out_h = b_img.getHeight() / 5;

            char[][] out = new char[out_w][out_h];

            for(int i = 0; i < out_w; i++){
                for(int n = 0; n < out_h; n++){
                    out[i][n] = outputToChar(network.calculate(extract(b_img, i * 5, n * 5, 5,5))[0][0]);
                }
            }

            PrintWriter writer = new PrintWriter(result_file);

            for(int i = 0; i < out_h; i++){
                String s = "";
                for(int n= 0; n < out_w;n++){
                    s += out[n][i];
                }
                writer.println(s);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        ImageToText2 toText = new ImageToText2();
        toText.train("res/train3.png");
        toText.calculate("res/eric.png", "res/test2.txt");
        //toText.network.analyseNetwork();
        //Layer.printArray(((ConvLayer)toText.network.getInputLayer().getNext_layer()).getFilter(0));
    }

    private char outputToChar(double[] o){
        if(inverse){
            return chars[chars.length-1-ArrayTools.indexOfHighestValue(o)];
        }
        return chars[ArrayTools.indexOfHighestValue(o)];
    }

}
