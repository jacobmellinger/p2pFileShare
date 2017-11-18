import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private String myPeerID;
    private String theirPeerID;
    private String message;    //message received from the client
    private String MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket

    public ConnectionHandler(Socket connection, String myPeerID) {
        this.connection = connection;
        this.myPeerID = myPeerID;
    }

    public void run()
    {
        try{
            //initialize Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());

            String workingDir = System.getProperty("user.dir");
            //FileHandler log = new FileHandler(workingDir + "/" + myPeerID +".log");


            try{
                //Sending each other their respective PeerID to identify each other
                establishConnection(myPeerID);

                //Send secret message
                MESSAGE = "testMsg";
                sendMessage(MESSAGE);

                message = (String)in.readObject();
                System.out.println("[" + myPeerID + "] Receive message: " + message + " from " + theirPeerID);

            }
            catch(ClassNotFoundException classnot){
                System.err.println("[" + myPeerID + "] Data received in unknown format");
            }
        }
        catch(IOException ioException){
            System.out.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                System.out.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("[" + myPeerID + "] Send message: " + msg + " to " + theirPeerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //establish connection (replace with Handshake)
    public void establishConnection(String msg)
    {
        try{
            System.out.println("[" + myPeerID + "] Creating a connection to new peer...");
            //Sending your PeerID to the other Peer being connected to
            out.writeObject(msg);
            out.flush();
            try {
                //Receiving from Peer that is being connected, their PeerID
                message = (String)in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            theirPeerID = message;
            System.out.println("[" + myPeerID + "] Successful connection to " + theirPeerID + "!!!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
