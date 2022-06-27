package it.unibs.pajc;

import it.unibs.pajc.client.Client;
import it.unibs.pajc.view.StarterMenuView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp extends JFrame {

    private Client clientController;
    private StarterMenuView menu;
    private JTextField textIP;
    private JTextField txtPlayerName;
    
    private JButton btnConnect;
    private JButton btnExit;
    
    private DocumentListener jTextListener;

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
        getContentPane().setLayout(null);  //absolute layout
        
        JLabel lblNewLabel = new JLabel("<html><h1 style=\"font-size: 40px;\"><strong><i>Head Soccer</i></strong></h1><hr></html>");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(10, 10, 990, 132);
        getContentPane().add(lblNewLabel);
        
        //creazione label per immettere indirizzo ip del server al quale collegarsi
        textIP = new JTextField();
        textIP.setBackground(Color.LIGHT_GRAY);
        textIP.setFont(new Font("Lucida Sans", Font.BOLD, 16));
        textIP.setText("127.0.0.1");
        textIP.setHorizontalAlignment(SwingConstants.CENTER);
        textIP.setBounds(338, 238, 337, 39);
        getContentPane().add(textIP);
        textIP.setColumns(10);
        textIP.setBorder(new LineBorder(Color.BLACK, 2));
        
        //creazione label per immettere nome del giocatore
        txtPlayerName = new JTextField();
        txtPlayerName.setBackground(Color.LIGHT_GRAY);
        txtPlayerName.setText("Player Name");
        txtPlayerName.setFont(new Font("Lucida Sans", Font.BOLD, 16));
        txtPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
        txtPlayerName.setColumns(10);
        txtPlayerName.setBounds(338, 189, 337, 39);
        getContentPane().add(txtPlayerName);
        txtPlayerName.setBorder(new LineBorder(Color.BLACK, 2));
        
        //BOTTONE: CONNECT
        btnConnect = new JButton("CONNECT");
        btnConnect.setFont(new Font("Lucida Sans", Font.BOLD, 16));
        btnConnect.setBackground(new Color(124, 252, 0));
        btnConnect.setBounds(338, 300, 337, 39);
        getContentPane().add(btnConnect);
        
        btnConnect.addActionListener(this::connectToServer);
        
        //BOTTONE: EXIT
        btnExit = new JButton("EXIT");
        btnExit.setBackground(new Color(205, 92, 92));
        btnExit.setFont(new Font("Lucida Sans", Font.BOLD, 16));
        btnExit.setBounds(338, 349, 337, 39);
        getContentPane().add(btnExit);
        btnConnect.setEnabled(false);
        
        btnExit.addActionListener(this::exit);
        
        
        this.pack();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        //abilitazione bottone CONNECT -> quanto i jTecxt vengono riempiti
        
        jTextListener = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				enableConnectBotton();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				enableConnectBotton();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				enableConnectBotton();
			}
		};
		
		
	    txtPlayerName.getDocument().addDocumentListener(jTextListener);
        textIP.getDocument().addDocumentListener(jTextListener);
        
        
        
    }

    private void connectToServer(ActionEvent e) {
    	//controllo validità IP
    	String ipString = textIP.getText();
    	
    	if(checkIPvalidValidity(ipString)) {
    		clientController = new Client(this);
            clientController.startServerConnection();

            this.revalidate();
            this.repaint();
    	}
    	else {
    		textIP.setBorder(new LineBorder(Color.RED, 2));
    		textIP.setForeground(Color.RED);
    		textIP.setText("< INVALID IP >");
    	}
    	
    }

    /**
     * controlla se l'IP inserito è valido attraverso una regex
     */
    private boolean checkIPvalidValidity(String ip) {
    	String IPv4Pattern = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    	
    	Pattern pattern = Pattern.compile(IPv4Pattern);
    	Matcher matcher = pattern.matcher(ip);
        return matcher.matches();

	}

	private void exit(ActionEvent e) {
        System.exit(0);
    }
    
    /**
     * Controlla che i due JtextField non siano vuoti: solo quando vengono riempiti entrambi viene abilitato
     * il bottone CONNECT
     */
    private void enableConnectBotton() {
    	if(textIP.getText().equals("") || txtPlayerName.getText().equals("")) {
            btnConnect.setEnabled(false);
        }else {
            btnConnect.setEnabled(true);
        }
    }
    
}
