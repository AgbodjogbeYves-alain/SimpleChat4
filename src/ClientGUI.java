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
  private JTextField pseudoTF = new JTextField();
  private JTextField hostTF = new JTextField();
  private JTextField portTF = new JTextField();
  private JTextField messageTF = new JTextField();
  private JButton loginB = new JButton("Login");
  private JButton logoffB = new JButton("Logoff");
  private JButton hostB = new JButton("New Host? Click here");
  private JButton portB = new JButton("New Port? Click here");
  private JButton getPortB = new JButton("What is your Port? Click here");
  private JButton getHostB = new JButton("What is your Host? Click here");

  
  private JButton envoyerB = new JButton("Envoyer");
  private JPanel displayMessage = new JPanel();
  private JScrollPane panedisplay = new JScrollPane(displayMessage);
  private int hauteur = 400;
  private JTextArea tA = new JTextArea();
  private JButton validerCommand= new JButton("Valider");
  private JTextField newCommand = new JTextField();
  private JFrame frameCommand = new JFrame();
  private String command = null;
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
	
	loginB.addActionListener(this);
	logoffB.addActionListener(this);
	envoyerB.addActionListener(this);
	hostB.addActionListener(this);
	portB.addActionListener(this);
	getPortB.addActionListener(this);
	getHostB.addActionListener(this);
	
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
	
	frameCommand.setLayout(new BoxLayout(frameCommand.getContentPane(),BoxLayout.PAGE_AXIS));
	newCommand.setPreferredSize(new Dimension(120,30));
	
	panedisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    panedisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    panedisplay.setPreferredSize(new Dimension(400,400));
    panedisplay.setAutoscrolls(false);
	
	this.setSize(600, 600);

	messageTF.setPreferredSize(new Dimension(400, 30));
	messageTF.addActionListener(this);
	
    containerButton.add(loginB);
    containerButton.add(logoffB);
    containerButton.add(hostB);
    containerButton.add(portB);
    containerButton.add(getPortB);
    containerButton.add(getHostB);

    
    
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
  
	public void showNextFrame(String command) {
		
		JLabel label = null;
		JPanel displayCommand = new JPanel();
		
		
		if(command == "login ") {
			
			label = new JLabel("Pseudo : ");
			
		}else if(command == "sethost ") {
			
			label = new JLabel("Host : ");
			
		}else if(command == "setport ") {
			
			label = new JLabel("Port : ");
		}
		
		newCommand.addActionListener(this);
		displayCommand.add(label);
		displayCommand.add(newCommand);
		validerCommand.addActionListener(this);
		frameCommand.setContentPane(displayCommand);
		frameCommand.add(validerCommand);
		frameCommand.repaint();
		frameCommand.pack();
		frameCommand.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == loginB) {
			
			command = "login ";
			showNextFrame(command);
			
		}else if(source == logoffB){
			
			client.handleMessageFromClientUI("#logoff");
			
		}else if(source == envoyerB || source == messageTF){
			
			String message = messageTF.getText();
			if(message!="") {
				
				client.handleMessageFromClientUI(message); 
				
			}
			
		}else if(source == validerCommand || source == newCommand) {
			
			String message = newCommand.getText();
			client.handleMessageFromClientUI("#"+command+message);
			frameCommand.dispose();
			
		}else if(source == hostB) {
			
			command = "sethost ";
			showNextFrame(command);
			
		}else if(source == portB) {
			
			command = "setport ";
			showNextFrame(command);
			
		}else if(source == getPortB) {
			
			command = "getport";
			client.handleMessageFromClientUI("#"+command);
			
		}else if(source == getHostB) {
			
			command = "gethost";
			client.handleMessageFromClientUI("#"+command);
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
		newCommand.validate();
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
	    
	  }
	  
}

//End of ConsoleChat class










