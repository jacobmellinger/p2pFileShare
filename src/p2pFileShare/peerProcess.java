import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class peerProcess {
    // private static int sPort = 8000;   //The server will be listening on this port number
    //private static int numOfClients = 0;
    //private static String peerID;   //The server will be listening on this port number

    public static void main(String peerID, int sPort, int numOfClients, Vector<RemotePeerInfo> peerInfoVector) throws IOException {
        System.out.println(peerID + " is running!");
        sPort += numOfClients;
        startServer(peerID, sPort);
        RemotePeerInfo pInfo;
        for (int i=0; i<numOfClients; i++)
        {
            pInfo = (RemotePeerInfo) peerInfoVector.elementAt(i);
            startClient(peerID, Integer.parseInt(pInfo.peerPort));
        }
    }

    public static void startServer(String peerID, int sPort) {
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
                        new ConnectionHandler(listener.accept(), peerID).start();
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

    public static void startClient(String peerID, int cPort) {
        Thread clientThread = new Thread() {
            Socket requestSocket;           //socket connect to the server
            ObjectOutputStream out;         //stream write to the socket
            ObjectInputStream in;          //stream read from the socket
            @Override
            public void run() {
                try {
                    requestSocket = new Socket("localhost", cPort);
                    new ConnectionHandler(requestSocket, peerID).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientThread.start();
    }
}


