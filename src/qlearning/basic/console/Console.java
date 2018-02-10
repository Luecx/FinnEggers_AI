package qlearning.basic.console;

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


}
