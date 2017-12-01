public class UnchokeMessage extends Messages {
	
	private int messageLength;
	private int messageType;
	
	UnchokeMessage() {
		messageType = 1;
	}
	
	UnchokeMessage(int newLength) {
		messageType = 1;
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
		UnchokeMessage message = (UnchokeMessage) msg;

		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceived = 1;
		myPeer.peerInfoVector.get(neighborPeerIndex).themChokingMe = false;

		return 1;
	}

}
