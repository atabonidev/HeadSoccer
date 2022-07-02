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

/**
 * Classe che consente di avviare il gioco. È la classe principale
 */
public class MainApp extends JFrame {

    private Client clientController;
    private StarterMenuView menu;
    private JTextField textIP;
    private JTextField txtPlayerName;
    
    private JButton btnConnect;  //bottone che avvia la connessione con il server
    private JButton btnExit; //provoca l'uscita dal gioco e la chiusura del frame
    
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

        JLabel gameTitle = new JLabel(
        "<html>" +
                "<h1 style=\"font-size: 40px; color: #004d00; padding: 5px 10px; padding-top: 0; border-bottom: 3px solid #004d00;\">" +
                    "<strong><i>Head Soccer</i></strong>" +
                "</h1>" +
            "</html>"
        );
        gameTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gameTitle.setBounds(10, 10, 990, 132);
        getContentPane().add(gameTitle);
        
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
        
        //abilitazione bottone CONNECT -> solo quando i jText vengono modificati e riempiti
        
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

        this.pack();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    //metodo che, dopo aver controllato la validità dell'ip immesso, crea una nuova istanza del client da collegare al server
    private void connectToServer(ActionEvent e) {
    	//controllo validità IP
    	String ipString = textIP.getText();
    	
    	if(checkIPvalidValidity(ipString)) {
    		clientController = new Client(this, ipString);
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

    //controlla se l'IP inserito è valido attraverso una regex
    private boolean checkIPvalidValidity(String ip) {
    	String IPv4Pattern = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    	
    	Pattern pattern = Pattern.compile(IPv4Pattern);
    	Matcher matcher = pattern.matcher(ip);
        return matcher.matches();

	}

	private void exit(ActionEvent e) {
        System.exit(0);
    }
    
    //Controlla che i due JtextField non siano vuoti: solo quando vengono riempiti entrambi viene abilitato il bottone CONNECT
    private void enableConnectBotton() {
    	if(textIP.getText().equals("") || txtPlayerName.getText().equals("")) {
            btnConnect.setEnabled(false);
        }else {
            btnConnect.setEnabled(true);
        }
    }

    /* ===================
    GETTERS AND SETTERS
    ====================*/

    public JTextField getTxtPlayerName() {
        return txtPlayerName;
    }
    public JTextField getTextIP() {
        return textIP;
    }
}
