import java.io.PrintWriter;

public class Handshake extends Messages {
	
	String handshakeHeader;
	String zeroBits = "0000000000";
	int peerID;
	
	
	Handshake(){
		messageType = 8;
		handshakeHeader = "P2PFILESHARINGPROJ";
	}
	
	Handshake(int ID) {
		messageType = 8;
		handshakeHeader = "P2PFILESHARINGPROJ";
		peerID = ID;
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex, PrintWriter writer) {
		Handshake message = (Handshake) msg;

		if(message.errorMsg == false){

			if (message.handshakeHeader == "P2PFILESHARINGPROJ") {
				myPeer.peerInfoVector.get(neighborPeerIndex).hasReceivedHandshake = true;
				myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 8;
				System.out.println("I got handshake from " + message.peerID);
				return message.peerID;
			}
			else {
				return -1;
			}
		}
		else {
			return -1;
		}
	}
}
