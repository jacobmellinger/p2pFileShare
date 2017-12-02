import java.io.PrintWriter;

public class InterestedMessage extends Messages {

	private int messageLength;
	private int messageType;
	
	InterestedMessage() {
		messageType = 2;
	}
	
	InterestedMessage(int newLength) {
		messageType = 2;
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
		InterestedMessage message = (InterestedMessage) msg;

		myPeer.peerInfoVector.get(neighborPeerIndex).isInterested = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 2;
		writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeer.myPeerID + " received the 'interested' message from " + myPeer.peerInfoVector.get(neighborPeerIndex).peerId);

		return 1;
	}

}
