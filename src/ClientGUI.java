// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.awt.Dimension;
import java.awt.Font;
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
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientGUI(String host, int port) 
  {
	super("Client Console"); // ou setTitle("...");
	 // affiche la fen�tre
	JPanel containerPseudo = new JPanel();
	JPanel containerHost = new JPanel();
	JPanel containerPort = new JPanel();
	JPanel containerButtonLog = new JPanel();
		
		
	JLabel labelPseudo = new JLabel("Pseudo : ");
	JLabel labelHost = new JLabel("Host : ");
	JLabel labelPort = new JLabel("Port : ");
	
	Font font = new Font("Arial",Font.BOLD,20);
	labelPseudo.setFont(font);
	labelHost.setFont(font);
	labelPort.setFont(font);

	JButton login = new JButton("Login");
	
	
	JTextField pseudoTF = new JTextField();
	JTextField hostTF = new JTextField();
	JTextField portTF = new JTextField();
	
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
	
	this.setSize(600, 600);
	pseudoTF.setPreferredSize(new Dimension(400, 40));
    hostTF.setPreferredSize(new Dimension(400, 40));
    portTF.setPreferredSize(new Dimension(400, 40));
   
    containerPseudo.add(labelPseudo);
    containerPseudo.add(pseudoTF);
    containerHost.add(labelHost);
    containerHost.add(hostTF);
    containerPort.add(labelPort);
    containerPort.add(portTF);
    containerButtonLog.add(login);
    
    
    this.add(containerPseudo);
    this.add(containerHost);
    this.add(containerPort);
    this.add(containerButtonLog);
    
    login.addActionListener(this);
    
    this.setIconImage(new ImageIcon(this.getClass().getResource("unnamed.png")).getImage());
    this.setVisible(true);            
	
	// affiche la fenetre
    pack();
    
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

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  /**
   * This method parse the message and get the first letter
   */
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	
    int port; //The port number

	String host;
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

@Override
public void actionPerformed(ActionEvent e) {
	// TODO lancer main envoyer message #login avec l id
}
}
//End of ConsoleChat class
