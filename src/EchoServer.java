// This file contains material supporting section 3.7 of the textbook:// "Object Oriented Software Engineering" and is issued under the open-source// license found at www.lloseng.com import java.io.*;import java.util.Observable;import java.util.Observer;import java.util.StringTokenizer;import com.lloseng.ocsf.server.ConnectionToClient;import com.lloseng.ocsf.server.ObservableOriginatorServer;import com.lloseng.ocsf.server.ObservableServer;import com.lloseng.ocsf.server.OriginatorMessage;import common.ChatIF;import ocsf.server.*;/** * This class overrides some of the methods in the abstract  * superclass in order to give more functionality to the server. * * @author Dr Timothy C. Lethbridge * @author Dr Robert Lagani&egrave;re * @author Fran&ccedil;ois B&eacute;langer * @author Paul Holden * @version July 2000 */public class EchoServer implements Observer {  //Class variables *************************************************  	  /**   * The default port to listen on.   */  final public static int DEFAULT_PORT = 5555;   //Instance variables **********************************************    /**   * The interface type variable.  It allows the implementation of    * the display method in the client.   */  ChatIF serverUI;   ObservableOriginatorServer oos;    //Constructors ****************************************************    /**   * Constructs an instance of the echo server.   *   * @param port The port number to connect on.   */  public EchoServer(int port)   {	this.oos = new ObservableOriginatorServer(port);	oos.addObserver(this);	try {		oos.listen();	} catch (IOException e) {		// TODO Auto-generated catch block		e.printStackTrace();	}  }    //Instance methods ************************************************    /**   * This method handles any messages received from the client.   *   * @param msg The message received from the client.   * @param client The connection from which the message originated.   */  public void handleMessageFromClient    (Object msg, ConnectionToClient client)  {		if(((String)msg).charAt(0) == '#') {		String[] message_split = ((String) msg).split(" ");		if(message_split[0].equals("#login")) {	  		//System.out.println("fait");	  		client.setInfo("id", message_split[1]);	  		//System.out.println(client.getInfo("id"));	  	}else {	  		System.out.println("Message received: " + msg + " from " + client.getInfo("id"));	  	}	}else {		System.out.println("Message received: " + msg + " from " + client.getInfo("id"));		oos.sendToAllClients((String) client.getInfo("id") + ":"+ msg);	}      }       /**   * This method overrides the one in the superclass.  Called   * when the server starts listening for connections.   */  protected void serverStarted()  {    System.out.println      ("Server listening for connections on port " + oos.getPort());  }    /**   * This method overrides the one in the superclass.  Called   * when the server stops listening for connections.   */  protected void serverStopped()  {    System.out.println      ("Server has stopped listening for connections.");  }    /**   * Method called each time the server is closed.   * It notifies observers by sending an   * <code> OriginatorMessage </code> instance   * containing the message defined by the static variable SERVER_CLOSED.   * The originator is set to null.   */  synchronized protected void serverClosed()  {    System.out.println("Server is close for the moment.");  }    synchronized protected void clientClosed(ConnectionToClient client, Throwable exception) {	  oos.sendToAllClients("Connection will be closed by the server");	   }    synchronized protected void clientException(ConnectionToClient client, Throwable exception) {	  System.out.println("A client brutally disconnected");  }    protected void clientConnected(ConnectionToClient client) {	  System.out.println("A Client is connected");  }    synchronized protected void clientDisconnected(ConnectionToClient client) {	  System.out.println("A Client disconnected");  }  	/**   * Method called each time an exception is raised   * while listening.   * It notifies observers by sending an   * <code> OriginatorMessage </code> instance   * containing the message defined by the static variable LISTENING_EXCEPTION   * to which is appended the exception message.   * The originator is set to null.   *   * @param exception the exception raised.   */  protected synchronized void listeningException(Throwable exception) {	System.out.println(exception.getMessage());  }    /**   * This method handles all data coming from the UI               *   * @param message The message from the UI.       */  public void handleMessageFromServerUI(String message)  {	  String[] message_split = message.split(" ");  	if(message_split[0].charAt(0)!='#') {  		oos.sendToAllClients("Server MSG > " + message);  	}else {  		  		switch(message_split[0]) {  		case "#quit": try {  			oos.close();			} catch (IOException e2) {				e2.printStackTrace();			}  					  quit();  					  break;  					    		case "#stop": oos.stopListening();					  break;					    		case "#close": try {  			oos.close();			} catch (IOException e1) {				// TODO Auto-generated catch block				e1.printStackTrace();			}  					   break;  			  		case "#setport":if(!oos.isListening()) {  						oos.setPort(Integer.parseInt(message_split[1]));  						}		  						 break;  						   		case "#start": if(!oos.isListening()) {  			try {  				oos.listen();			} catch (IOException e) {				// TODO Auto-generated catch block				e.printStackTrace();			}  		}else {  			System.out.println("You are already connected to the server");  		}  		break;    		case "#getport": System.out.println("Port : " + oos.getPort());  						 break;  		}  }  }  	  	 /**     * This method terminates the server.     */    public void quit()    {      System.exit(0);    }	@Override	public void update(Observable arg0, Object arg1) {		OriginatorMessage om = (OriginatorMessage) arg1;		String[] message_split = new String[2];		//String[] message_split = (om.getMessage()).toString().split(".");		String parameter = ".";				StringTokenizer st = new StringTokenizer(om.getMessage().toString());				if(((String) om.getMessage()).charAt(0)=='#') {			if(st.countTokens() >2) {				message_split[0] = st.nextToken(parameter);				message_split[1] = st.nextToken(parameter);				message_split[0] = message_split[0]+".";			}else {//dans le cas ou on a pas de'exception derriere				message_split[0] = om.getMessage().toString();			}		}else {			message_split[0] = om.getMessage().toString();		}								if(message_split[0].equals(oos.CLIENT_CONNECTED)) {						clientConnected(om.getOriginator());					}else if(message_split[0].equals(oos.CLIENT_DISCONNECTED)) {			clientDisconnected(om.getOriginator());					}else if(message_split[0].equals(oos.CLIENT_EXCEPTION)) {						Exception exp= new Exception(message_split[0]); 			clientException(om.getOriginator(),exp);					}else if(message_split[0].equals(oos.LISTENING_EXCEPTION)) {						Exception exp= new Exception(message_split[1]); 			listeningException(exp);					}else if(message_split[0].equals(oos.SERVER_CLOSED)) {						serverClosed();					}else if(message_split[0].equals(oos.SERVER_STARTED)) {						serverStarted();					}else if(message_split[0].equals(oos.SERVER_STOPPED)) {						serverStopped();					}else {			handleMessageFromClient(message_split[0],om.getOriginator());						}	} }//End of EchoServer class