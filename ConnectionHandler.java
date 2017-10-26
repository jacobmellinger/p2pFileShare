import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private String myPeerID;
    private String peerID;
    private String message;    //message received from the client
    private String MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket

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
                while(true)
                {
                    message = (String)in.readObject();
                    System.out.println("[" + myPeerID + "] Receive message: " + message + " from Peer " + peerID);

                    //Send secret message
                    MESSAGE = secretMessage.toUpperCase();
                    sendMessage(MESSAGE);

                }
            }
            catch(ClassNotFoundException classnot){
                System.err.println("[" + myPeerID + "] Data received in unknown format");
            }
        }
        catch(IOException ioException){
            System.out.println("[" + myPeerID + "] Disconnect with Peer " + peerID);
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                System.out.println("[" + myPeerID + "] Disconnect with Peer " + peerID);
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("[" + myPeerID + "] Send message: " + msg + " to Peer " + peerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
