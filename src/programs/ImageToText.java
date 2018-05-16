package programs;

import network.Network;
import network.NetworkBuilder;
import network.data.TrainSet;
import network.functions.activation.LeakyReLU;
import network.functions.activation.ReLU;
import network.functions.activation.Sigmoid;
import network.functions.activation.TanH;
import network.functions.error.CrossEntropy;
import network.functions.error.MSE;
import network.layers.ConvLayer;
import network.layers.Layer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by finne on 14.05.2018.
 */
public class ImageToText {

    private Network network;
    private char[] chars = {'#','/', '\\','-','_'};

    public ImageToText(Network network) {
        this.network = network;
    }

    public ImageToText() {
        NetworkBuilder builder = new NetworkBuilder(1,15,15);
        builder.addLayer(new ConvLayer(12,3,3,0)
                .setActivationFunction(new LeakyReLU())
                .weightsRange(0.111,0.1111)
                .biasRange(0,0));
        builder.addLayer(new ConvLayer(1,1,1,0)
                .setActivationFunction(new LeakyReLU())
                .weightsRange(1,1)
                .biasRange(0,0));
        this.network = builder.buildNetwork();
        this.network.setErrorFunction(new MSE());
    }

    public void train(String img, String result_file) {
        try {
            BufferedImage b_img = ImageIO.read(new File(img));
            double[][][] in = new double[1][b_img.getWidth()][b_img.getHeight()];
            for (int i = 0; i < b_img.getWidth(); i++) {
                for (int n = 0; n < b_img.getHeight(); n++) {
                    in[0][i][n] = new Color(b_img.getRGB(i, n)).getRed() / 256d;
                }
            }

            BufferedReader reader = new BufferedReader(new FileReader(new File(result_file)));
            ArrayList<double[]> lines = new ArrayList<>();
            String s;
            while ((s = reader.readLine()) != null) {
                lines.add(new double[s.length()]);
                int index = 0;
                for (char c : s.toCharArray()) {
                    if(c == ' ') break;
                    lines.get(lines.size() - 1)[index] = charToOutput(c);
                    index++;
                }
            }
            double[][][] output = new double[1][lines.size()][lines.get(0).length];
            for (int i = 0; i < output[0].length; i++) {
                for (int n = 0; n < output[0][0].length; n++) {
                    output[0][i][n] = lines.get(i)[n];
                }
            }


            for(int i = 0; i < 10; i++){
                for(int n = 0; n < 1000; n++){
                    this.network.train(in, output, 0.0001);
                }
                System.out.println(network.overall_error(in, output));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculate(String img, String result_file){
        try {
            BufferedImage b_img = ImageIO.read(new File(img));
            double[][][] in = new double[1][b_img.getWidth()][b_img.getHeight()];
            for (int i = 0; i < b_img.getWidth(); i++) {
                for (int n = 0; n < b_img.getHeight(); n++) {
                    in[0][i][n] = new Color(b_img.getRGB(i, n)).getRed() / 256d;
                }
            }
            double[][][] o = this.network.calculate(in);
            for(int i = 0; i < o[0][0].length; i++){
                String s = "";
                for(int n= 0; n < o[0].length;n++){
                    //System.out.println(Math.min(Math.max(0,o[0][i][n]),1));
                    //System.out.println(outputToChar(Math.min(Math.max(0,o[0][i][n]),1)));
                    s+=outputToChar(Math.min(Math.max(0,o[0][n][i]),1));
                }
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        ImageToText toText = new ImageToText();
        toText.train("res/in3.png", "res/out3.txt");
        toText.network.analyseNetwork();

        toText.calculate("res/in3.png", "res/out2.txt");
        //Layer.printArray(((ConvLayer)toText.network.getInputLayer().getNext_layer()).getFilter(0));
    }

    private double charToOutput(char c) {
        int index = 0;
        for (char k : chars) {
            if (k == c) {
                return index / ((double) chars.length - 1);
            }
            index++;
        }
        return index;
    }

    private char outputToChar(double o){
        return chars[Math.min((int)(Math.round(o * chars.length)), chars.length-1)];
    }

}
