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
	public void handleMessage(Messages msg) {
		ChokeMessage message = (ChokeMessage) msg;

	}
}
