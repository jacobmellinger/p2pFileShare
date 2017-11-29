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
	public void handleMessage(Messages msg, peerProcess myPeer) {
		UnchokeMessage message = (UnchokeMessage) msg;

	}

}
