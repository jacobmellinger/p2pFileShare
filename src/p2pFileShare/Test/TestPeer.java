package p2pFileShare.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestPeer {
	
	private static int numOfClients = 0;
	private int sPort = 8000;   //The server will be listening on this port number
    private String peerID;   //The server will be listening on this port number
    private String message;
	
	TestPeer() throws IOException{
		
		setNumOfClients(getNumOfClients() + 1);
		peerID = "peer";
		peerID += getNumOfClients();
    	
		message = "Hello"; 
        System.out.println(peerID + " is running!");
		int cPort = sPort;
        sPort += getNumOfClients();
        sPort--;
        startServer();
        for (int i=0; i<getNumOfClients(); i++)
        {
        	// Don't want to connect to Peer0
        	if (i != 0) {
        		String otherPeerID = "";
        		if (peerID != null && peerID.length() >0 ) {
        			otherPeerID = peerID.substring(0, peerID.length() -1 );
            		otherPeerID += i;
            	}
                startClient(cPort + i, otherPeerID);
        	}
        }        
	}
	
	
	public void startServer() {
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
                        new TestConnectionHandler(listener.accept(), peerID, message, true).start();
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

    public void startClient(int cPort, String otherPeerID) {
        Thread clientThread = new Thread() {
            Socket requestSocket;           //socket connect to the server
            ObjectOutputStream out;         //stream write to the socket
            ObjectInputStream in;          //stream read from the socket
            @Override
            public void run() {
                try {
                    requestSocket = new Socket("localhost", cPort);
                    new TestConnectionHandler(requestSocket, peerID, message, otherPeerID).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientThread.start();
    }


	public static int getNumOfClients() {
		return numOfClients;
	}


	public static void setNumOfClients(int numOfClients) {
		Peer.numOfClients = numOfClients;
	}
	
	
}
