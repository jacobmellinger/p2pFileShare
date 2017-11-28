public class BitfieldMessage extends Messages {

	private int messageType;
	private int messageLength;
	private int[] messagePayload;
	
	
	BitfieldMessage(){
		messageType = 5;
	}
	
	BitfieldMessage(int newLength) {
		messageType = 5;
		messageLength = newLength;
	}
	
	BitfieldMessage(int newLength, int[] bitmap) {
		messageType = 5;
		messageLength = newLength;
		messagePayload = bitmap;
	}
	
	public void setBitField(int[] bitmap) {
		messagePayload = bitmap;
	}
	
	public int[] getBitField() {
		return messagePayload;
	}
	
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public void handleMessage(Messages msg) {
		BitfieldMessage message = (BitfieldMessage) msg;

	}
}
