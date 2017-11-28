import messages.Messages;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private String myPeerID;
    private peerProcess myPeer;
    private String theirPeerID;
    private Messages message;    //message received from the client
    private Messages MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    private PrintWriter writer;

    public ConnectionHandler(Socket connection, String myPeerID, peerProcess myPeer) throws FileNotFoundException, UnsupportedEncodingException {
        this.myPeer = myPeer;
        this.connection = connection;
        this.myPeerID = myPeerID;
        this.writer = new PrintWriter(myPeerID +"_log.txt", "UTF-8");
    }

    public void run()
    {
        try{
            //initialize Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());

            //String workingDir = System.getProperty("user.dir");
            //FileHandler log = new FileHandler(workingDir + "/" + myPeerID +".log");
            //PrintWriter writer = new PrintWriter(myPeerID +"_log.txt", "UTF-8");
            writer.println("CREATING A LOG FILE!!");
            writer.close();


//            try{
//                //Sending each other their respective PeerID to identify each other
//                establishConnection(myPeerID);
//
//                //Send secret message
//                //MESSAGE = new Handshake(5);
//                //sendMessage(MESSAGE);
//
//                //message = (String)in.readObject();
//                //writer.println("[" + myPeerID + "] Receive message: " + message + " from " + theirPeerID);
//
//
//            }
//            catch(ClassNotFoundException classnot){
//                System.err.println("[" + myPeerID + "] Data received in unknown format");
//            }
        }
        catch(IOException ioException){
            writer.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                writer.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            writer.println("[" + myPeerID + "] Send message: " + msg + " to " + theirPeerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //establish connection (replace with Handshake)
    public void establishConnection(String msg)
    {
        try{
            System.out.println("[" + Integer.toString(myPeer.myPeerID) + "] Creating a connection to new peer...");
            //Sending your PeerID to the other Peer being connected to
            out.writeObject(msg);
            out.flush();
            try {
                //Receiving from Peer that is being connected, their PeerID
                message = (Messages) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //message
            writer.println("[" + myPeerID + "] Successful connection to " + theirPeerID + "!!!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
