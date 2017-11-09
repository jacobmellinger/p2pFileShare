package p2pFileShare.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestConnectionHandler extends Thread{
    private String myPeerID;
    private String otherPeerID = "idk";
    private String message;    //message received from the client
    private String MESSAGE;    //uppercase message send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    boolean testBool = false;

    //todo remove
    private int secretMsgCounter = 1;

    private String secretMessage;

    public TestConnectionHandler(Socket connection, String myPeerID, String secretMessage, String peerID) {
        this.connection = connection;
        this.otherPeerID = peerID;
        this.myPeerID = myPeerID;
        this.secretMessage = secretMessage;
    }

    public TestConnectionHandler(Socket connection, String myPeerID, String secretMessage) {
        this.connection = connection;
        this.myPeerID = myPeerID;
        this.secretMessage = secretMessage;
        
    }
    
    public TestConnectionHandler(Socket connection, String myPeerID, String secretMessage, boolean test) {
        this.connection = connection;
        this.myPeerID = myPeerID;
        this.secretMessage = secretMessage;
        testBool = test;
    }

    public void run() {
        try{
            //initialize Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            try{
                establishConnection(myPeerID);
                System.out.println("[" + myPeerID + "] Connection established with " + otherPeerID);
                while(secretMsgCounter <= 10)
                {
                    //Send secret message
                    //MESSAGE = secretMessage + " ~" + secretMsgCounter + "~";
                	MESSAGE = secretMessage;
                    sendMessage(MESSAGE);

                    message = (String)in.readObject();
                    message = message.toUpperCase();
                    System.out.println("[" + myPeerID + "] RECEIVING MESSAGE " + secretMsgCounter + ": " + message + " from " + otherPeerID + testBool);
                   // System.out.println("[" + myPeerID + "] Receive message: " + message + " from " + peerID);
                    secretMsgCounter++;

                }
            }
            catch(ClassNotFoundException classnot){
                System.err.println("[" + myPeerID + "] Data received in unknown format");
            }
        }
        catch(IOException ioException){
            System.out.println("[" + myPeerID + "] Disconnect with " + otherPeerID);
        }
        finally{
            //Close connections
            try{
                in.close();
                out.close();
                connection.close();
            }
            catch(IOException ioException){
                System.out.println("[" + myPeerID + "] Disconnect with " + otherPeerID);
            }
        }
    }

    //send a message to the output stream
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("[" + myPeerID + "] SENDING MESSAGE " + secretMsgCounter + ":   " + msg  + " from " + myPeerID + " to " + otherPeerID + testBool);
            //System.out.println("[" + myPeerID + "] Send message: " + msg + " to " + peerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //establish connection
    public void establishConnection(String msg)
    {
        try{
            //System.out.println("[" + myPeerID + "] Creating a connection to new peer... -> to " + otherPeerID + testBool);
            
            //Send message then try to read?
            out.writeObject(msg);
            out.flush();
            try {
                message = (String)in.readObject();
                //System.out.println("[" + myPeerID + "] Receive message: Made connection to " + otherPeerID + " " + testBool);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //Why this?
            //peerID = message;
           // System.out.println("[" + myPeerID + "] Receive message: Made connection to " + otherPeerID);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
