package it.unibs.pajc;

import it.unibs.pajc.client.Client;
import it.unibs.pajc.view.StarterMenuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainApp extends JFrame {

    private Client clientController;
    private StarterMenuView menu;

    public static void main(String[] args) {
        MainApp app = new MainApp();
    }

    public MainApp() {
        initialize();
    }

    private void initialize() {
        this.menu = new StarterMenuView();

        this.setContentPane(this.menu);
        this.getContentPane().setPreferredSize(new Dimension(1000, 561));
        this.pack();

        menu.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

        menu.add(new JLabel("<html><h1 style=\"font-size: 40px;\"><strong><i>Head Soccer</i></strong></h1><hr></html>"), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setOpaque(false);

        Insets padding = new Insets(10,40,10,40);

        JButton start = new JButton("Start");
        start.setMargin(padding);
        start.addActionListener(this::joinGame);

        JButton exit = new JButton("Exit");
        exit.setMargin(padding);
        exit.addActionListener(this::exit);

        gbc.insets = new Insets(10, 0, 10, 0);

        buttons.add(start, gbc);
        buttons.add(exit, gbc);

        gbc.weighty = 1;
        menu.add(buttons, gbc);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

    }

    private void joinGame(ActionEvent e) {
        clientController = new Client(this);
        clientController.startServerConnection();

        this.revalidate();
        this.repaint();
    }

    private void exit(ActionEvent e) {
        System.exit(0);
    }

}
