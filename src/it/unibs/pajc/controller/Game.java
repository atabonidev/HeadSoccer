package it.unibs.pajc.controller;

import it.unibs.pajc.view.GameView;
import javax.swing.*;
import java.awt.*;


/**
 * Classe principale del gioco
 */
public class Game {

    private JFrame frame;
    private GameView gameView;

    public Game() {
        initialize();
    }

    private void initialize() {
        this.frame = new JFrame();
        //gameView = new GameView();

        frame.setLayout(new BorderLayout());

        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();

        frame.setContentPane(gameView);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

}
