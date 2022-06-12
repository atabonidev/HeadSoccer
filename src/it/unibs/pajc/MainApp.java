package it.unibs.pajc;

import it.unibs.pajc.client.Client;
import it.unibs.pajc.server.Server;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainApp {

    private Client clientController;
    private Server serverController;

    private JFrame frame;
    private JPanel menu;

    public static void main(String[] args) {
        MainApp app = new MainApp();
    }

    public MainApp() {
        initialize();
    }

    private void initialize() {

        this.frame = new JFrame();
        this.menu = new JPanel();

        frame.setContentPane(this.menu);
        frame.getContentPane().setPreferredSize(new Dimension(1000, 561));
        frame.pack();

        menu.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

        menu.add(new JLabel("<html><h1 style=\"font-size: 40px;\"><strong><i>Head Soccer</i></strong></h1><hr></html>"), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JPanel buttons = new JPanel(new GridBagLayout());

        Insets padding = new Insets(10,40,10,40);

        JButton start = new JButton("Start");
        start.setMargin(padding);
        start.addActionListener(this::joinGame);

        JButton exit = new JButton("Exit");
        exit.setMargin(padding);

        gbc.insets = new Insets(10, 0, 10, 0);

        buttons.add(start, gbc);
        buttons.add(exit, gbc);

        gbc.weighty = 1;
        menu.add(buttons, gbc);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

    }

    private void joinGame(ActionEvent e) {
        clientController = new Client(frame);
        clientController.startServerConnection();

        frame.revalidate();
        frame.repaint();
    }

}
