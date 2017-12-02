import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectionHandler extends Thread{
    private int myPeerID;
    private peerProcess myPeer;
    private String theirPeerID;

    private String incomingMessage;    //incomingMessage received from the client
    private byte[] incomingByteArrayMessage;
    private String outgoingMessage;    //uppercase incomingMessage send to the client
    private Socket connection;
    private ObjectInputStream in;	//stream read from the socket
    private ObjectOutputStream out;    //stream write to the socket


    public ConnectionHandler(Socket connection, String myPeerID, peerProcess myPeer) throws FileNotFoundException, UnsupportedEncodingException {
        this.myPeer = myPeer;
        this.connection = connection;
        this.myPeerID = Integer.parseInt(myPeerID);
        this.theirPeerID = null;
        //this.writer = new PrintWriter(myPeerID +"_log.txt", "UTF-8");
    }

    public void getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        //int millis = now.get(ChronoField.MILLI_OF_SECOND); // Note: no direct getter available.
        myPeer.writer.printf("%d-%02d-%02d %02d:%02d:%02d.%03d", year, month, day, hour, minute, second);
    }

//    public  void startTimedConnection() {
//        new java.util.Timer().schedule(
//                new java.util.TimerTask() {
//                    public void run()
//                    {
//                        try{
//                            //initialize Input and Output streams
//                            out = new ObjectOutputStream(connection.getOutputStream());
//                            out.flush();
//                            in = new ObjectInputStream(connection.getInputStream());
//
//                            //String workingDir = System.getProperty("user.dir");
//                            //FileHandler log = new FileHandler(workingDir + "/" + myPeerID +".log");
//                            //PrintWriter writer = new PrintWriter(myPeerID +"_log.txt", "UTF-8");
//                            writer.println("CREATING A LOG FILE!!");
//                            writer.close();
//
//                            //todo remove this
//                            int myPeerIndex = 0;
//                            for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
//                                if (myPeer.peerInfoVector.get(i).peerId == myPeerID){
//                                    myPeerIndex = i;
//                                    break;
//                                }
//                            }
//                            //todo~~~~
//
//                            try{
//                                while(/*!everyoneHasEverything*/ true) {
//                                    //Sending each other their respective PeerID to identify each other
//                                    //establishConnection(myPeerID);
//
//                                    //Send Message
//                                    synchronized (myPeer.peerInfoVector.get(myPeerIndex)) {
//                                        if (theirPeerID == null) {
//                                            outgoingMessage = outgoingMessage.createMessage(myPeerID, -1, myPeer);
//                                        } else {
//                                            outgoingMessage = outgoingMessage.createMessage(myPeerID, Integer.parseInt(theirPeerID), myPeer);
//                                        }
//
//                                        int otherPeerIndex = 0;
//                                        for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
//                                            if (myPeer.peerInfoVector.get(i).peerId == Integer.parseInt(theirPeerID)) {
//                                                otherPeerIndex = i;
//                                                break;
//                                            }
//
//                                        }
//                                        sendMessage(outgoingMessage);
//
//                                        incomingMessage = (Messages) in.readObject();
//                                        incomingMessage.handleMessage(incomingMessage, myPeer, otherPeerIndex);
//                                        //writer.println("[" + myPeerID + "] Receive incomingMessage: " + incomingMessage + " from " + theirPeerID);
//                                    }
//                                }
//                            }
//                            catch(ClassNotFoundException classnot){
//                                System.err.println("[" + myPeerID + "] Data received in unknown format");
//                            }
//                        }
//                        catch(IOException ioException){
//                            System.out.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
//                        }
//                        finally{
//                            //Close connections
//                            try{
//                                in.close();
//                                out.close();
//                                connection.close();
//                                synchronized (myPeer.fileByteArray)
//                                {
//                                    myPeer.createFileFromByteArray(myPeer.sizeOfBitMap);
//                                }
//                            }
//                            catch(IOException ioException){
//                                System.out.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
//                            }
//                        }
//                    }
//                }
//        , 0, myPeer.UnchokingInterval*1000);
//    }

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
           // writer.close();
            boolean isDone = false;


            try{

                establishConnection(Integer.toString(myPeerID));
                int myPeerIndex = -1;
                int otherPeerIndex = -1;
                for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
                    if (myPeer.peerInfoVector.get(i).peerId == myPeerID) {
                        myPeerIndex = i;
                    }
                    if (myPeer.peerInfoVector.get(i).peerId == Integer.parseInt(theirPeerID)) {
                        otherPeerIndex = i;
                    }
                }
                int counter = 0;

                while(/*!everyoneHasEverything*/ !isDone) {
                    synchronized (myPeer.peerInfoVector.get(myPeerIndex)) {
                        //Send Message
                        if(myPeer.peerInfoVector.get(myPeerIndex).hasFile == 1 && myPeerID != 1001)
                        {
                            outgoingMessage = "DONE";
                            sendMessage(outgoingMessage);
                            break;
                        }
                        else if(myPeer.peerInfoVector.get(myPeerIndex).hasFile == 1)
                        {
                            if(myPeer.peerInfoVector.get(otherPeerIndex).hasFile == 0)
                            {
                                if(counter < myPeer.fileByteArray.length) {
                                    byte[] piece = myPeer.fileByteArray[counter];
                                    System.out.println(piece);
                                    for (int i=0; i< piece.length; i++)
                                    {
                                        System.out.print(piece[i] + " ");
                                    }
                                    System.out.println();
                                    sendByteArrayMessage(piece);
                                    System.out.println("Sent piece " + counter + " to Peer" + theirPeerID);
                                    counter++;
                                }
                                else break;
                            }
                        }
                        else {
                            outgoingMessage = "testMsg";
                            sendMessage(outgoingMessage);
                        }

                    }

                    if(myPeer.peerInfoVector.get(myPeerIndex).hasFile == 0) {
                        if(counter < myPeer.fileByteArray.length) {
                            incomingByteArrayMessage = (byte[]) in.readObject();
                            System.out.println(incomingByteArrayMessage);
                            for (int i=0; i< incomingByteArrayMessage.length; i++)
                            {
                                System.out.print(incomingByteArrayMessage[i] + " ");
                            }
                            myPeer.fileByteArray[counter] = incomingByteArrayMessage;
                            myPeer.peerInfoVector.get(myPeerIndex).bitMap.set(counter, 1);
                            System.out.println("\nReceived piece " + counter + " from Peer" + theirPeerID);
                            counter++;
                        }
                        else break;
                    }
                    else{
                        incomingMessage = (String) in.readObject();
                        if (incomingMessage.equals("DONE")) break;
                    }


                    isDone = true;
                    System.out.println("checking that its done ");
                    for(int i=0; i<myPeer.peerInfoVector.get(myPeerIndex).bitMap.size(); i++){
                        if(myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(i) == 0 || myPeerID == 1001)
                        {
                            isDone = false;
                            break;
                        }
                    }
                    if(isDone)
                    {
                        System.out.println("its done!! ");
                        synchronized (myPeer.fileByteArray)
                        {
                            myPeer.createFileFromByteArray(myPeer.sizeOfBitMap);
                            myPeer.peerInfoVector.get(otherPeerIndex).hasFile = 1;
                            System.out.println("Peer " + myPeer.myPeerID + " had downloaded the complete file.");
                        }
                    }
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
                System.out.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
            }
            catch(IOException ioException){
                System.out.println("[" + myPeerID + "] Disconnect with " + theirPeerID);
            }
        }
    }


    //send a incomingMessage to the output stream
    public void sendMessage(String msg)
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

    public void sendByteArrayMessage(byte[] msg)
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
            System.out.println("[" + myPeerID + "] Creating a connection to new peer...");
            //Sending your PeerID to the other Peer being connected to
            out.writeObject(msg);
            out.flush();
            try {
                //Receiving from Peer that is being connected, their PeerID
                incomingMessage = (String)in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            theirPeerID = incomingMessage;
            System.out.println("[" + myPeerID + "] Successful connection to " + theirPeerID + "!!!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

}
