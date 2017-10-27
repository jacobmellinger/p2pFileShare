package p2pFileShare.messages;

public class Handshake extends Messages{
	
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
}
