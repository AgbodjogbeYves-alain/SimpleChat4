// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lloseng.ocsf.client.ObservableClient;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientGUI extends JFrame implements ChatIF,ActionListener
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  final public static String DEFAULT_HOST = "localhost";
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  private  JTextField pseudoTF = new JTextField();
  private JTextField hostTF = new JTextField();
  private JTextField portTF = new JTextField();
  private JTextField messageTF = new JTextField();
  private JButton login = new JButton("Login");
  private JButton logoff = new JButton("Logoff");
  private JButton envoyerB = new JButton("Envoyer");
  private JPanel displayMessage = new JPanel();
  private JScrollPane panedisplay = new JScrollPane(displayMessage);
  private int hauteur = 400;
  private JTextArea tA = new JTextArea();
  private JButton valider= new JButton("Valider");
  private JTextField newCommand = new JTextField();
  //  private JButton login = new JButton("Login");
//  private JButton login = new JButton("Login");
  private JFrame fra = new JFrame();
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientGUI(String host,int port) 
  {
	super("Client Console"); // ou setTitle("...");
	 // affiche la fenêtre
//	JPanel containerPseudo = new JPanel();
//	JPanel containerHost = new JPanel();
//	JPanel containerPort = new JPanel();
	JPanel containerButton = new JPanel();
	JPanel containerMessage = new JPanel();
	
	
	
		
//	JLabel labelPseudo = new JLabel("Pseudo : ");
//	JLabel labelHost = new JLabel("Host : ");
//	JLabel labelPort = new JLabel("Port : ");
//	
	
//	Font font = new Font("Arial",Font.BOLD,20);
//	labelPseudo.setFont(font);
//	labelHost.setFont(font);
//	labelPort.setFont(font);
	
	login.addActionListener(this);
	logoff.addActionListener(this);
	envoyerB.addActionListener(this);
	
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
	panedisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    panedisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    panedisplay.setPreferredSize(new Dimension(400,400));
    panedisplay.setAutoscrolls(false);
	//this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
	
	this.setSize(600, 600);
//	pseudoTF.setPreferredSize(new Dimension(400, 40));
//    hostTF.setPreferredSize(new Dimension(400, 40));
//    portTF.setPreferredSize(new Dimension(400, 40));
	messageTF.setPreferredSize(new Dimension(400, 30));
	
    //containerPseudo.add(labelPseudo);
    //containerPseudo.add(pseudoTF);
    //containerHost.add(labelHost);
    //containerHost.add(hostTF);
    //containerPort.add(labelPort);
    //containerPort.add(portTF);
	
    containerButton.add(login);
    containerButton.add(logoff);
    
	containerMessage.add(messageTF);
	containerMessage.add(envoyerB);
    displayMessage.setPreferredSize(new Dimension(600,hauteur));
    displayMessage.setLayout(new BoxLayout(displayMessage, BoxLayout.PAGE_AXIS));
    displayMessage.setBackground(Color.WHITE);
    
    
    //JScrollPane pane= new JScrollPane(displayMessage);  
    //pane.setBackground(Color.WHITE);
    //this.add(containerPseudo);
    //this.add(containerHost);
    //this.add(containerPort);
    this.add(containerButton);
    this.add(panedisplay);
    this.add(containerMessage);
    
    this.setIconImage(new ImageIcon(this.getClass().getResource("unnamed.png")).getImage());
    this.setVisible(true);            
	
	// affiche la fenêtre
    this.pack();
    
    try 
    {
      client= new ChatClient(host,port,this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }
  
	//Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {
	      BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	      String message;
	
	      while (true) 
	      {
	        message = fromConsole.readLine();
	        
	        	client.handleMessageFromClientUI(message); 
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("Unexpected error while reading from console!");
	    }
	  }


	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		JPanel displayCommand = new JPanel();
		if(source == login) {
			//client.handleMessageFromClientUI("#login" + pseudoTF);
			fra.setLayout(new BoxLayout(fra.getContentPane(),BoxLayout.PAGE_AXIS));
			JLabel login = new JLabel("Pseudo : ");
			newCommand.setPreferredSize(new Dimension(120,30));
			displayCommand.add(login);
			displayCommand.add(newCommand);
			valider.addActionListener(this);
			fra.add(displayCommand);
			fra.add(valider);
			fra.pack();
			fra.setVisible(true);
		}else if(source == logoff){
			System.out.println("logoff");
		}else if(source == envoyerB){
			String message = messageTF.getText();
			if(message!="") {
				client.handleMessageFromClientUI(message); 
			}
		}else if(source == valider) {
			String message = newCommand.getText();
			client.handleMessageFromClientUI(message);
			fra.dispose();
		}
	}

	@Override
	public void display(String message) {
		JLabel messageReceived = new JLabel(message);
		hauteur = hauteur + 15;
		displayMessage.setPreferredSize(new Dimension(displayMessage.getWidth(),hauteur));
		displayMessage.add(messageReceived);
		displayMessage.validate();
		panedisplay.validate();
		this.getContentPane().validate();
		System.out.println(message);
		newCommand.setText("");
	}
	
	
	//Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of the Client UI.
	   *
	   * @param args[0] The host to connect to.
	   */
	  public static void main(String[] args) 
	  {
		
	    String host = "localhost";
	    int port; //The port number

		try
	    {
	      host = args[0];
	      
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	     
	    }
	    
	    try {
	    	port = Integer.parseInt(args[1]);
	    }
	    catch(ArrayIndexOutOfBoundsException e) {
	    	port = 5555;
	    }
	    ClientGUI chat= new ClientGUI(host,port);
	    
	    chat.accept();  //Wait for console data
	  }
	  
}

//End of ConsoleChat class










