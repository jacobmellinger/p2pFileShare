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
	public int handleMessage(Messages msg, peerProcess myPeer, int myPeerIndex) {
		ChokeMessage message = (ChokeMessage) msg;

		myPeer.peerInfoVector.get(myPeerIndex).themChokingMe = true;
		myPeer.peerInfoVector.get(myPeerIndex).lastMessageReceived = 0;
		return 1;
	}
}
