package it.unibs.pajc.view;

import javax.swing.*;
import java.awt.*;

public class StartingView extends JPanel {

    public StartingView() {
        super();
        this.setFocusable(true);
        this.requestFocusInWindow();
        initialize();
    }

    private void initialize() {
        this.setOpaque(true);
        this.setBackground(Color.lightGray);

        JButton playBtn = new JButton("Play");
        playBtn.setBackground(Color.WHITE);
        playBtn.setForeground(Color.BLACK);

        this.add(playBtn, BorderLayout.CENTER);
    }


}
