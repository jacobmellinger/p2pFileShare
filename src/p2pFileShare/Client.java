import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Client {
	int socketNumber;
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE;                //capitalized message read from the server

	boolean hasHandshaked = false;

	public Client(int socketNumber) {
		this.socketNumber = socketNumber;
	}

	void run()
	{
		try{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", socketNumber);
			System.out.println("[Client] Connected to localhost in port " + socketNumber);
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				if(!hasHandshaked)
				{
					System.out.println("[Client] Sending handshake...");
					message = "handshake";
					sendMessage(message);
					MESSAGE = (String)in.readObject();
					System.out.println("[Client] Receive message: " + MESSAGE);
					hasHandshaked = true;
				}
			}
		}
		catch (ConnectException e) {
			System.err.println("[Client] Connection refused. You need to initiate a server first.");
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			run();
		}
		catch ( ClassNotFoundException e ) {
			System.err.println("[Client] Class not found");
		}
		catch(UnknownHostException unknownHost){
			System.err.println("[Client] You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	//send a message to the output stream
	void sendMessage(String msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//main method
	public static void main(String args[])
	{
		Client client = new Client(8000);
		client.run();
	}

}
