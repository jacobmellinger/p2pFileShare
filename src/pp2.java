import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Vector;

public class pp2 {
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
    public byte[][] fileByteArray = null;
    public String path = System.getProperty("user.dir");
    public int sizeOfBitMap = 0;
    public PrintWriter writer = new PrintWriter("log_peer_" + myPeerID + ".log", "UTF-8");

    pp2(int myPeerID) throws FileNotFoundException, UnsupportedEncodingException {
        this.myPeerID = myPeerID;
    }

    public void getCommonConfiguration()
    {
        String st;
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

    public void createByteArrayFromFile(int sizeOfBitMap) throws Exception {
        File file = new File(path + "\\peer_" + myPeerID +"\\" + FileName);
        if (!file.exists()) return;

        Path Path = file.toPath();
        byte[] fileArray;
        fileArray = Files.readAllBytes(Path);
        if(fileArray.length != FileSize)
        {
            throw new Exception("Not the right File!");
        }

        fileByteArray = new byte[sizeOfBitMap][PieceSize];
        for(int i=0; i<sizeOfBitMap; i++)
        {
            for(int j=i*PieceSize, k=0; j<(i+1)*PieceSize; j++, k++)
            {
                fileByteArray[i][k] =  fileArray[j];
                if (j == fileArray.length-1) {
                    break;
                }
            }
        }
    }

    public void createFileFromByteArray(int sizeOfBitMap) throws IOException {
        File file = new File(path + "\\peer_" + myPeerID +"\\" + FileName);
        if(file.exists()) return;

        fileByteArray = new byte[sizeOfBitMap][PieceSize];
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path + "\\peer_"+ myPeerID +"\\" + FileName);
            for (int i = 0; i < sizeOfBitMap; i++)
            {
                fileOutputStream.write(fileByteArray[i]);
            }
        } finally {
            fileOutputStream.close();
        }
    }

    public static void main(String [] args) throws IOException {
//        peerProcess myPeerProcess = new peerProcess(Integer.parseInt(args[0]));
        peerProcess myPeerProcess = new peerProcess(1002); //USE THIS FOR TESTING IN IDE

        myPeerProcess.writer.println("CREATING A LOG FILE!!");

        myPeerProcess.getCommonConfiguration();

        myPeerProcess.sizeOfBitMap = myPeerProcess.FileSize/ myPeerProcess.PieceSize;
        if(myPeerProcess.FileSize% myPeerProcess.PieceSize > 0) myPeerProcess.sizeOfBitMap++;

        myPeerProcess.getPeerConfiguration(myPeerProcess.sizeOfBitMap);
        myPeerProcess.fileByteArray = new byte[myPeerProcess.sizeOfBitMap][myPeerProcess.PieceSize];
        try {
            myPeerProcess.createByteArrayFromFile(myPeerProcess.sizeOfBitMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int numOfClients = myPeerProcess.peerInfoVector.indexOf(myPeerProcess.myPeerInfo);
        int sPort = myPeerProcess.myPeerInfo.peerPort;
        String cPort = "";

        System.out.println(myPeerProcess.myPeerID + " is running!");

        startServer(Integer.toString(myPeerProcess.myPeerID), sPort, myPeerProcess);

        for (int i=0; i<numOfClients; i++)
        {
            cPort = Integer.toString(myPeerProcess.peerInfoVector.get(i).peerPort);
            startClient(Integer.toString(myPeerProcess.myPeerID), Integer.parseInt(cPort), myPeerProcess);
        }

//        myPeerProcess.createFileFromByteArray(myPeerProcess.sizeOfBitMap);

        // Up to here, the peer has all the config info and has started
        // clients to connect to all peers started before it

    }

    public static void startServer(String peerID, int sPort, peerProcess peer) {
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
//                        new ConnectionHandler(listener.accept(), peerID, peer).startTimedConnection();
                        new ConnectionHandler(listener.accept(), peerID, peer).start();
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

    public static void startClient(String peerID, int cPort, peerProcess peer) {
        Thread clientThread = new Thread() {
            Socket requestSocket;           //socket connect to the server
            ObjectOutputStream out;         //stream write to the socket
            ObjectInputStream in;          //stream read from the socket
            @Override
            public void run() {
                try {
                    requestSocket = new Socket("localhost", cPort);
//                    new ConnectionHandler(requestSocket, peerID, peer).startTimedConnection();
                    new ConnectionHandler(requestSocket, peerID, peer).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientThread.start();
    }
}



