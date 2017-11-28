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
	public void handleMessage(Messages msg) {
		NotInterestedMessage message = (NotInterestedMessage) msg;

	}

}
