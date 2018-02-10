package qlearning.neuralnetwork.console;

import network.layers.ConvLayer;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Created by finne on 05.02.2018.
 */
public class Console extends JFrame {

    public Console() {
        super();
        this.setDefaultCloseOperation(3);
        this.setSize(800,800);
        this.setLayout(new BorderLayout());

        area = new JTextArea();
        area.setFont(new Font("monospaced", Font.BOLD, 32));
        PrintStream out = new PrintStream( new TextAreaOutputStream(area));
        PrintStream err = new PrintStream( new TextAreaOutputStream(area));
        System.setOut(out);
        System.setErr(err);

        this.add(area, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void clear() {
        this.area.setText("");
    }

    private JTextArea area;




    public static void main(String[] args) {
        Console c = new Console();

        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(300);
                if(i == 5) {
                    c.clear();
                }
                System.out.println("iaujwdijawid");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
