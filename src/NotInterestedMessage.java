import java.io.PrintWriter;

public class NotInterestedMessage extends Messages {

	private int messageLength;
	private int messageType;
	
	NotInterestedMessage() {
		messageType = 3;
	}
	
	NotInterestedMessage(int newLength) {
		messageType = 3;
		messageLength = newLength;
	}
	
	public int getMessageLength() {
		return messageLength;
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex, PrintWriter writer) {
		NotInterestedMessage message = (NotInterestedMessage) msg;

		myPeer.peerInfoVector.get(neighborPeerIndex).isInterested = false;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 3;
		writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeer.myPeerID + " received the 'not interested' message from " + myPeer.peerInfoVector.get(neighborPeerIndex).peerId);

		return 1;
	}

}
