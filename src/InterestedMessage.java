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
	public void handleMessage(Messages msg, peerProcess myPeer) {
		InterestedMessage message = (InterestedMessage) msg;

	}

}
