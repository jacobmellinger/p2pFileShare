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
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex) {
		InterestedMessage message = (InterestedMessage) msg;

		myPeer.peerInfoVector.get(neighborPeerIndex).isInterested = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceived = 2;
		return 1;
	}

}
