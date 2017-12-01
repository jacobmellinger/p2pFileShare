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
	public void handleMessage(Messages msg, peerProcess2 myPeer, int neighborPeerIndex) {
		Handshake message = (Handshake) msg;

		myPeer.peerInfoVector.get(neighborPeerIndex).hasReceivedHandshake = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceived = 8;
	}
}
