public class RequestMessage extends Messages {

	private int messageType;
	private int messageLength;
	private int messagePayload;
	
	RequestMessage() {
		messageType = 6;
	}
	
	RequestMessage(int newLength) {
		messageType = 6;
		messageLength = newLength;
	}
	
	RequestMessage (int newLength, int indexField) {
		messageType = 6;
		messageLength = newLength;
		messagePayload = indexField;
	}
	
	public void setMessagePayload(int indexField) {
		messagePayload = indexField;
	}
	
	public int getMessagePayload () {
		return messagePayload;
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public void handleMessage(Messages msg, peerProcess myPeer) {
		RequestMessage message = (RequestMessage) msg;

	}

}
