import java.io.PrintWriter;

// This message Class encapsulates messages for Choke
public class ChokeMessage extends Messages {

	private int messageLength;
	private int messageType;
	
	ChokeMessage() {
		messageType = 0;
	}
	
	ChokeMessage(int newLength) {
		messageType = 0;
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
		ChokeMessage message = (ChokeMessage) msg;

		myPeer.peerInfoVector.get(neighborPeerIndex).themChokingMe = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 0;
		writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeer.myPeerID + " is choked by " + myPeer.peerInfoVector.get(neighborPeerIndex).peerId);

		return 1;
	}
}
