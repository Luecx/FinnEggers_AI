package neuroevolution.def.tic_tac_toe;

import network.Network;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends javax.swing.JFrame {

    private JButton[][] jButtons = new JButton[3][3];
    private Player opponent = new Player();
    private Board board = new Board();

    public Frame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(500, 500));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));
        getContentPane().setLayout(new java.awt.GridLayout(3, 3));

        opponent.setNetwork(Network.load_network("res/ttt-net0.txt"));

        for(int y = 0; y < 3; y++){
            for(int x = 0; x < 3; x++){
                final int a = x;
                final int b = y;

                JButton button = new JButton();
                button.setFont(new Font("Arial", 1,100));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        click(a,b);
                    }
                });

                jButtons[a][b] = button;
                getContentPane().add(button);
            }
        }

        pack();

//        this.opponent.let_npc_move(board);
        this.updateBoard();
    }

    private void click(int a, int b) {
        board.place(a,b);
        updateBoard();
        System.out.println(board);
        this.opponent.let_npc_move(board);
        updateBoard();
        System.out.println(board);
    }

    private void updateBoard(){

        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                int id = board.getFields()[a][b];
                jButtons[a][b].setText(id == 1? "X": (id == -1?"O":""));
            }
        }
    }


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

}
