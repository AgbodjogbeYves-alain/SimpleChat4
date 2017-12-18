// This file contains material supporting section 3.7 of the textbook:// "Object Oriented Software Engineering" and is issued under the open-source// license found at www.lloseng.com package client;import ocsf.client.*;import java.util.*;import com.lloseng.ocsf.client.ObservableClient;import common.*;import java.io.*;/** * This class overrides some of the methods defined in the abstract * superclass in order to give more functionality to the client. * * @author Dr Timothy C. Lethbridge * @author Dr Robert Lagani&egrave; * @author Fran&ccedil;ois B&eacute;langer * @version July 2000 */public class ChatClient implements Observer{	ObservableClient oc;  //Instance variables **********************************************    /**   * The interface type variable.  It allows the implementation of    * the display method in the client.   */  ChatIF clientUI;    //Constructors ****************************************************  /**   * Constructs an instance of the chat client.   *   * @param host The server to connect to.   * @param port The port number to connect on.   * @param clientUI The interface type variable.   */    public ChatClient(String host, int port,ChatIF clientUI)     throws IOException   {    this.oc = new ObservableClient(host,port);    oc.addObserver(this);    this.clientUI = clientUI;    //oc.openConnection();  }    //Instance methods ************************************************     /**   * This method handles all data that comes in from the server.   *   * @param msg The message from the server.   */  public void handleMessageFromServer(Object msg)   {    clientUI.display(msg.toString());  }  /**   * This method handles all data coming from the UI               *   * @param message The message from the UI.       */  public void handleMessageFromClientUI(String message)  {    try    {    	    	//System.out.println(message_split[0].charAt(0));    	if(message.charAt(0)!='#') {    		oc.sendToServer(message);    	}else {    		String[] message_split = message.split(" ");    		//System.out.println(message_split[1]);    		switch(message_split[0]) {    		case "#quit":  if(oc.isConnected()) {    			oc.closeConnection();    		    			}    			quit();    			break;    			    		case "#logoff": if(oc.isConnected()) {    			oc.closeConnection();    		}				break;    		case "#sethost": if(!oc.isConnected()) {    			oc.setHost(message_split[1]);    		}    			break;    			    		case "#setport": if(!oc.isConnected()) {    			oc.setPort(Integer.parseInt(message_split[1]));    		}    		break;    		case "#login": if(!oc.isConnected()) {    			System.out.println(message_split[1]);    				oc.openConnection();    				oc.sendToServer("#login " +  message_split[1]);    			}else {    			System.out.println("You are already connected to the server");    		}    		break;    		case "#gethost": clientUI.display("Host : " + oc.getHost());    		break;	    		case "#getport": clientUI.display("Port : " + oc.getPort());    		break;	    		}    	}    	    	    }    catch(IOException e)    {      clientUI.display        ("Could not send message to server.  Terminating client.");         }  }    /**Nicely**/  protected void connectionClosed() {	  clientUI.display("Connection was shut down by the server");  }  protected void connectionEstablished() {	  clientUI.display("Connection established");  }    /**Brutal**/  protected void connectionException(Exception exception) {	  clientUI.display("Connection was brutally shut down by the server");  }  /**   * This method terminates the client.   */  public void quit()  {    System.exit(0);  }@Overridepublic void update(Observable arg0, Object arg1) {	if(arg1 instanceof Exception) {				connectionException((Exception) arg1);			}else if(arg1.equals(oc.CONNECTION_ESTABLISHED)) {				connectionEstablished();			}else if(arg1.equals(oc.CONNECTION_CLOSED)) {		connectionClosed();	}else {		this.handleMessageFromServer(arg1);	}	}  }//End of ChatClient class