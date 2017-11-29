import java.io.*;
import java.net.Socket;

public class ConnectionHandler extends Thread{
    private int myPeerID;
    private peerProcess myPeer;
    private String theirPeerID;

    private Messages incomingMessage;    //incomingMessage received from the client
    private Messages outgoingMessage;    //uppercase incomingMessage send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket
    private PrintWriter writer;

    public ConnectionHandler(Socket connection, String myPeerID, peerProcess myPeer) throws FileNotFoundException, UnsupportedEncodingException {
        this.myPeer = myPeer;
        this.connection = connection;
        this.myPeerID = Integer.parseInt(myPeerID);
        this.theirPeerID = null;
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


            try{
                while(/*!everyoneHasEverything*/ true) {
                    //Sending each other their respective PeerID to identify each other
                    //establishConnection(myPeerID);

                    //Send Message
                    if(this.theirPeerID == null){
                        outgoingMessage = outgoingMessage.createMessage(myPeerID, -1, myPeer);
                    } else{
                        outgoingMessage = outgoingMessage.createMessage(myPeerID, Integer.parseInt(theirPeerID), myPeer);
                    }

                    sendMessage(outgoingMessage);

                    incomingMessage = (Messages)in.readObject();
                    incomingMessage.handleMessage(incomingMessage, myPeer);
                    //writer.println("[" + myPeerID + "] Receive incomingMessage: " + incomingMessage + " from " + theirPeerID);

                }
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

    //send a incomingMessage to the output stream
    public void sendMessage(Messages msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("[" + myPeerID + "] Send incomingMessage: " + msg + " to " + theirPeerID);
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
                incomingMessage = (Messages) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //incomingMessage
            writer.println("[" + myPeerID + "] Successful connection to " + theirPeerID + "!!!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
