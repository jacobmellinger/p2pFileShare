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
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex) {
		Handshake message = (Handshake) msg;

		if (message.handshakeHeader == "P2PFILESHARINGPROJ") {
			myPeer.peerInfoVector.get(neighborPeerIndex).hasReceivedHandshake = true;
			myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceived = 8;
			return message.peerID;
		}
		else {
			return -1;
		}
	}
}
