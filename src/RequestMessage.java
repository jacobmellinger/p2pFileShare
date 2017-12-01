public class RequestMessage extends Messages {

	private int messageType;
	private int messageLength;
	private int messagePayload;
	
	RequestMessage() {
		messageType = 6;
	}
	
	RequestMessage(int indexFeild) {
		messageType = 6;
		messagePayload = indexFeild;
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
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex) {
		RequestMessage message = (RequestMessage) msg;

		return 1;
	}

}
