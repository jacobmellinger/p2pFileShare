import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Peer4 {
    private static int sPort = 8000;   //The server will be listening on this port number
    private static int numOfClients = 3;
    private static String peerID = "Peer4";   //The server will be listening on this port number
    private static String secretMessage = "greetings";

    public static void main(String[] args) throws IOException {
        int cPort = sPort;
        System.out.println(peerID + " is running!");
        sPort += numOfClients;
        startServer();
        for (int i=0; i<numOfClients; i++)
        {
            startClient(cPort + i);
        }
    }

    public static void startServer() {
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
                        new ConnectionHandler(listener.accept(), peerID, secretMessage).start();
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

    public static void startClient(int cPort) {
        Thread clientThread = new Thread() {
            Socket requestSocket;           //socket connect to the server
            ObjectOutputStream out;         //stream write to the socket
            ObjectInputStream in;          //stream read from the socket
            @Override
            public void run() {
                try {
                    requestSocket = new Socket("localhost", cPort);
                    new ConnectionHandler(requestSocket, peerID, secretMessage).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientThread.start();
    }
}


