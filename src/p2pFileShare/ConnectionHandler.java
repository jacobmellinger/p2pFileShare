import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private String myPeerID;
    private String peerID = "-1";
    private String message;    //message received from the client
    private String MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket

    //todo remove
    private int secretMsgCounter = 1;

    private String secretMessage;

    public ConnectionHandler(Socket connection, String myPeerID, String secretMessage, String peerID) {
        this.connection = connection;
        this.peerID = peerID;
        this.myPeerID = myPeerID;
        this.secretMessage = secretMessage;
    }

    public ConnectionHandler(Socket connection, String myPeerID, String secretMessage) {
        this.connection = connection;
        this.myPeerID = myPeerID;
        this.secretMessage = secretMessage;
    }

    public void run() {
        try{
            //initialize Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            try{
                establishConnection(myPeerID);
                while(secretMsgCounter <= 10)
                {
                    //Send secret message
                    MESSAGE = secretMessage + " ~" + secretMsgCounter + "~";
                    sendMessage(MESSAGE);
                    secretMsgCounter++;

                    message = (String)in.readObject();
                    message = message.toUpperCase();
                    System.out.println("[" + myPeerID + "] Receive message: " + message + " from " + peerID);
                }
            }
            catch(ClassNotFoundException classnot){
                System.err.println("[" + myPeerID + "] Data received in unknown format");
            }
        }
        catch(IOException ioException){
            System.out.println("[" + myPeerID + "] Disconnect with " + peerID);
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                System.out.println("[" + myPeerID + "] Disconnect with " + peerID);
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("[" + myPeerID + "] Send message: " + msg + " to " + peerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //establish connection
    public void establishConnection(String msg)
    {
        try{
            System.out.println("[" + myPeerID + "] Creating a connection to new peer...");
            out.writeObject(msg);
            out.flush();
            try {
                message = (String)in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            peerID = message;
            System.out.println("[" + myPeerID + "] Receive message: Made connection to " + peerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
