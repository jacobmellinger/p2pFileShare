import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Vector;

class Server extends Thread {

    public String peerID;
    public int sPort;
    public peerProcess2 peer;

    Server(String peerID, int sPort, peerProcess2 peer) {
        this.peerID = peerID;
        this.sPort = sPort;
        this.peer = peer;
    }

    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(sPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                new ConnectionHandler(listener.accept(), peerID, peer).startTimedConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

class Client extends Thread {

    public String peerID;
    public int cPort;
    public peerProcess2 peer;
    Socket requestSocket;           //socket connect to the server

    Client(){
        peerID = "";
        cPort = 0;
        peer = null;
    }

    Client(String peerID, int cPort, peerProcess2 peer) {
        this.peerID = peerID;
        this.cPort = cPort;
        this.peer = peer;
    }

    public void run() {
        try {
            requestSocket = new Socket("localhost", cPort);
            new ConnectionHandler(requestSocket, peerID, peer).startTimedConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class peerProcess {
    // private static int sPort = 8000;   //The server will be listening on this port number
    //private static int numOfClients = 0;
    //private static String peerID;   //The server will be listening on this port number

    //ARGs: peerID, int sPort, int numOfClients, ports
    public int myPeerID;
    public int NumberOfPreferredNeighbors;
    public int UnchokingInterval;
    public int OptimisticUnchokingInterval;
    public String FileName;
    public int FileSize;
    public int PieceSize;
    public Vector<OtherPeerInfo> peerInfoVector;
    public OtherPeerInfo myPeerInfo;
    public LinkedList<Integer> pendingRequests;

    peerProcess(int myPeerID){
        this.myPeerID = myPeerID;
    }

    public void getCommonConfiguration()
    {
        String st;
        String path = System.getProperty("user.dir");
        try {
            BufferedReader in = new BufferedReader(new FileReader(path + "\\" + "Common.cfg"));
            while((st = in.readLine()) != null) {
                String[] tokens = st.split("\\s+");
                if(tokens[0].equals("NumberOfPreferredNeighbors")){
                    this.NumberOfPreferredNeighbors = Integer.parseInt(tokens[1]);
                }
                else if(tokens[0].equals("UnchokingInterval")){
                    this.UnchokingInterval = Integer.parseInt(tokens[1]);
                }
                else if(tokens[0].equals("OptimisticUnchokingInterval")){
                    this.OptimisticUnchokingInterval = Integer.parseInt(tokens[1]);
                }
                else if(tokens[0].equals("FileName")){
                    this.FileName = tokens[1];
                }
                else if(tokens[0].equals("FileSize")){
                    this.FileSize = Integer.parseInt(tokens[1]);
                }
                else if(tokens[0].equals("PieceSize")){
                    this.PieceSize = Integer.parseInt(tokens[1]);
                }
            }
            in.close();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void getPeerConfiguration(int sizeOfBitMap)
    {
        String st;
        this.peerInfoVector = new Vector<OtherPeerInfo>();
        String path = System.getProperty("user.dir");
        try {
            BufferedReader in = new BufferedReader(new FileReader(path + "\\" + "PeerInfo.cfg"));
            while((st = in.readLine()) != null) {
                String[] tokens = st.split("\\s+");
                if(Integer.parseInt(tokens[0]) == this.myPeerID){
                    this.myPeerInfo = new OtherPeerInfo(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), sizeOfBitMap);
                    this.peerInfoVector.addElement(this.myPeerInfo);
                }else{
                    this.peerInfoVector.addElement(new OtherPeerInfo(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), sizeOfBitMap));
                }
            }
            in.close();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public static void main(String [] args) throws IOException {
//        peerProcess2 myPeerProcess2 = new peerProcess2(Integer.parseInt(args[0]));
        peerProcess2 myPeerProcess2 = new peerProcess2(1001); //USE THIS FOR TESTING IN IDE

        myPeerProcess2.getCommonConfiguration();

        int sizeOfBitMap = myPeerProcess2.FileSize/ myPeerProcess2.PieceSize;
        if(myPeerProcess2.FileSize% myPeerProcess2.PieceSize > 0) sizeOfBitMap++;

        myPeerProcess2.getPeerConfiguration(sizeOfBitMap);

        int numOfClients = myPeerProcess2.peerInfoVector.indexOf(myPeerProcess2.myPeerInfo);
        int sPort = myPeerProcess2.myPeerInfo.peerPort;
        String cPort = "";

        System.out.println(myPeerProcess2.myPeerID + " is running!");

        Server2 server = new Server2(Integer.toString(myPeerProcess2.myPeerID), sPort, myPeerProcess2);
        server.start();
//        startServer(Integer.toString(myPeerProcess2.myPeerID), sPort, myPeerProcess2);
        Vector<Client2> client2s = new Vector<>();

        for (int i=0; i<numOfClients; i++)
        {
            cPort = Integer.toString(myPeerProcess2.peerInfoVector.get(i).peerPort);
            client2s.addElement(new Client2(Integer.toString(myPeerProcess2.myPeerID), Integer.parseInt(cPort), myPeerProcess2));
            client2s.get(i).start();

//            cPort = Integer.toString(myPeerProcess2.peerInfoVector.get(i).peerPort);
//            startClient(Integer.toString(myPeerProcess2.myPeerID), Integer.parseInt(cPort), myPeerProcess2);
        }

        try
        {
            server.join();
            for (int i=0; i<numOfClients; i++)
            {
                client2s.get(i).join();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // Up to here, the peer has all the config info and has started
        // client2s to connect to all peers started before it



    }

    public static void startServer(String peerID, int sPort, peerProcess2 peer) {
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                ServerSocket listener = null;
                try {
                    listener = new ServerSocket(sPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    while (true) {
                        new ConnectionHandler(listener.accept(), peerID, peer).startTimedConnection();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        listener.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        serverThread.start();
    }

    public static void startClient(String peerID, int cPort, peerProcess2 peer) {
        Thread clientThread = new Thread() {
            Socket requestSocket;           //socket connect to the server
            ObjectOutputStream out;         //stream write to the socket
            ObjectInputStream in;          //stream read from the socket
            @Override
            public void run() {
                try {
                    requestSocket = new Socket("localhost", cPort);
                    new ConnectionHandler(requestSocket, peerID, peer).startTimedConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientThread.start();
    }
}



