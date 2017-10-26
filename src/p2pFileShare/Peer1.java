import java.io.IOException;
import java.net.ServerSocket;

public class Peer1 {
    public static void main(String[] args) throws IOException {

        startServer();
        startClient();
    }

    public static void startClient() {
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                System.out.println("The server for Peer1 is running.");
                ServerSocket listener = null;
                try {
                    listener = new ServerSocket(8000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int clientNum = 1;
                try {
                    while (true) {
                        new Server.Handler(listener.accept(), clientNum).start();
                        System.out.println("[Server] Client " + clientNum + " is connected!");
                        clientNum++;
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
    
    public static void startServer() {
        Thread clientThread = new Thread() {
            @Override
            public void run() {
                Client client = new Client(8000);
                client.run();

            }
        };
        clientThread.start();
    }
}


