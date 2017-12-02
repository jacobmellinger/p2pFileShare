import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectionHandler extends Thread{
    private int myPeerID;
    private peerProcess myPeer;
    private String theirPeerID;

    private Messages incomingMessage;    //incomingMessage received from the client
    private Messages outgoingMessage;    //uppercase incomingMessage send to the client
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
                Handshake h = new Handshake();
                establishConnection(h);
                Handshake m = new Handshake();
                Messages newIncomingMsg = new Handshake();
                while(/*!everyoneHasEverything*/ true) {
                    //Sending each other their respective PeerID to identify each other
                    int myPeerIndex = -1;
                    for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
                        if (myPeer.peerInfoVector.get(i).peerId == myPeerID) {
                            myPeerIndex = i;
                            break;
                        }
                    }

                    //Send Message
                    synchronized (myPeer.peerInfoVector.get(myPeerIndex)) {
                        System.out.println("Entering sync send msg!!");
                        if (this.theirPeerID == null) {
                            outgoingMessage = m.createMessage(myPeerID, -1, myPeer);
                        } else {
                            outgoingMessage = outgoingMessage.createMessage(myPeerID, Integer.parseInt(theirPeerID), myPeer);
                        }
                        System.out.println("printing message type: " + outgoingMessage.getMessageType() + " and error set as: " + outgoingMessage.errorMsg);
                        System.out.println("lastMsgReceived: " + myPeer.peerInfoVector.get(myPeerIndex).lastMessageReceivedFromPeer);
                        System.out.println("lastMsgSent: " + myPeer.peerInfoVector.get(myPeerIndex).lastMessageSentToPeer);
//                        if (outgoingMessage.errorMsg == false && outgoingMessage != null) {
                            sendMessage(outgoingMessage);
                            System.out.println("sent message");
//                        }
                        synchronized (myPeer.writer) {
                            myPeer.writer.println("SENT A MESSAGE!!");
                        }
                        int otherPeerIndex = -1;
                        for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
                            if (theirPeerID != null){
                                if (myPeer.peerInfoVector.get(i).peerId == Integer.parseInt(theirPeerID)) {
                                    otherPeerIndex = i;
                                    break;
                                }
                            }
                        }
                        System.out.println("a");
                        incomingMessage = (Messages) in.readObject();
                        System.out.println("b");
//                        switch (incomingMessage.getMessageType())
//                        {
//                            case 0:
//                                System.out.println("case 0");
//                                newIncomingMsg = (ChokeMessage) incomingMessage;
//                                break;
//                            case 1:
//                                System.out.println("case 1");
//                                newIncomingMsg = (UnchokeMessage) incomingMessage;
//                                break;
//                            case 2:
//                                System.out.println("case 2");
//                                newIncomingMsg = (InterestedMessage) incomingMessage;
//                                break;
//                            case 3:
//                                System.out.println("case 3");
//                                newIncomingMsg = (NotInterestedMessage) incomingMessage;
//                                break;
//                            case 4:
//                                System.out.println("case 4");
//                                newIncomingMsg = (HaveMessage) incomingMessage;
//                                break;
//                            case 5:
//                                System.out.println("case 5");
//                                newIncomingMsg = (BitfieldMessage) incomingMessage;
//                                break;
//                            case 6:
//                                System.out.println("case 6");
//                                newIncomingMsg = (RequestMessage) incomingMessage;
//                                break;
//                            case 7:
//                                System.out.println("case 7");
//                                newIncomingMsg = (PieceMessage) incomingMessage;
//                                break;
//                            case 8:
//                                newIncomingMsg = (Handshake) incomingMessage;
//                                break;
//                            default: System.out.println("Something bad happned!");
//                        }

                        System.out.println("c");
                        synchronized (myPeer.writer) {
                            incomingMessage.handleMessage(newIncomingMsg, myPeer, otherPeerIndex, myPeer.writer);
                        }

                        //writer.println("[" + myPeerID + "] Receive incomingMessage: " + incomingMessage + " from " + theirPeerID);
                        System.out.println("end of sync block ");
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
                            System.out.println("Entering sync send msg!!");
                            synchronized ("Peer " + myPeer.myPeerID + " had downloaded the complete file.") {
                                myPeer.writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeer.myPeerID + " had downloaded the complete file.");
                            }
                            break;
                        }
                    }
//                    else{
//                        System.out.println("its not done :(");
//
//                    }
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
    public void establishConnection(Messages msg)
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
            System.out.println(" Peer " + myPeerID + " makes a connection to Peer " + theirPeerID);
            synchronized (myPeer.writer) {
                myPeer.writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeerID + " makes a connection to Peer " + theirPeerID);
            }
            //writer.println("[" + myPeerID + "] Successful connection to " + theirPeerID + "!!!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
