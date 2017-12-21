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

	JPanel containerButton = new JPanel();
	JPanel containerMessage = new JPanel();
	
	//On édite les paramètre de la fenêtre principale
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));//Type de fenêtre
	this.setSize(600, 600);
	
	//On fixe les paramètre pour le panel de scroll lorsqu'on écris des messages
	panedisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    panedisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    panedisplay.setPreferredSize(new Dimension(400,400));
    panedisplay.setAutoscrolls(false);

	//Pour tout les boutons on rajoute les actions listeners
	loginB.addActionListener(this); //Actionlistener bouton login
	logoffB.addActionListener(this); //Actionlistener bouton logoff
	envoyerB.addActionListener(this);//Actionlistener bouton envoyerB pour les messages
	hostB.addActionListener(this);//Actionlistener bouton hostB pour changer le host
	portB.addActionListener(this);//Actionlistener bouton portB pour changer le port
	getPortB.addActionListener(this);//Actionlistener bouton getPortB pour récupérer le port sur lequel écoute le client
	getHostB.addActionListener(this);//Actionlistener bouton getHostB pour récupérer l'adresse sur laquelle le client écoute
	
	
	//Paramètre fenêtre secondaire de mise à jour des informations (Pseudo,Host,Port) 
	frameCommand.setLayout(new BoxLayout(frameCommand.getContentPane(),BoxLayout.PAGE_AXIS));
	newCommand.setPreferredSize(new Dimension(120,30));
	
	
	
	
	//Le text area de message qui serviras au client pour entrer ses messages
	messageTF.setPreferredSize(new Dimension(400, 30));
	messageTF.addActionListener(this);
	
	
	//On rajoute les boutons au container de bouton
    containerButton.add(loginB);
    containerButton.add(logoffB);
    containerButton.add(hostB);
    containerButton.add(portB);
    containerButton.add(getPortB);
    containerButton.add(getHostB);

    
    //On rajoute les composantes au container qui va avoir la zone de saisie de texte par le client
	containerMessage.add(messageTF);
	containerMessage.add(envoyerB);
	
	//Paramétrage container de message envoyer par le serveur et par les autres clients
    displayMessage.setPreferredSize(new Dimension(600,hauteur));
    displayMessage.setLayout(new BoxLayout(displayMessage, BoxLayout.PAGE_AXIS));
    displayMessage.setBackground(Color.WHITE);
    
    
   //On rajoute les differents container crées dans notre Frame principale
    this.add(containerButton);
    this.add(panedisplay);
    this.add(containerMessage);
    
               
	
	// rajoute l'icone de l'app et affiche la fenêtre
    this.setIconImage(new ImageIcon(this.getClass().getResource("logo.png")).getImage());
    this.setVisible(true);
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
  
  	/**
  	 * Fonction d'affichage de fenêtre secondaire
  	 * @param command : String -> Le paramètre qui va être changé (Host,Port,Pseudo)
  	 */
	public void showNextFrame(String command) {
		
		JLabel label = null;
		JPanel displayCommand = new JPanel();
		
		//Sert à la création du label en fonction du paramètre à modifier
		if(command == "login ") {
			
			label = new JLabel("Pseudo : ");
			
		}else if(command == "sethost ") {
			
			label = new JLabel("Host : ");
			
		}else if(command == "setport ") {
			
			label = new JLabel("Port : ");
		}
		
		//On ajoute le label, le text field où sera rentré la nouvelle valeur du paramètre a la fenêtre secondaire et on affiche cette fenêtre secondaire
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
			messageTF.setText("");
			messageTF.validate();
			
		}else if(source == validerCommand || source == newCommand) {
			
			String message = newCommand.getText();
			client.handleMessageFromClientUI("#"+command+message);
			frameCommand.dispose();
			newCommand.setText("");
			newCommand.validate();
			
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
		hauteur = hauteur + 15;//Pour mettre à jour le barre de scroll on doit mettre à jour le displayMessage
		displayMessage.setPreferredSize(new Dimension(displayMessage.getWidth(),hauteur));
		displayMessage.add(messageReceived);
		displayMessage.validate();
		
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










