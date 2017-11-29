import java.util.Vector;

public class BitfieldMessage extends Messages {

	private int messageType;
	private int messageLength;
	private Vector<Integer> messagePayload;

	BitfieldMessage(){
		messageType = 5;
	}
	
	BitfieldMessage(int newLength) {
		messageType = 5;
		messageLength = newLength;
	}
	
	BitfieldMessage(int newLength, Vector<Integer> bitmap) {
		messageType = 5;
		messageLength = newLength;
		messagePayload = bitmap;
	}

	BitfieldMessage(Vector<Integer> bitmap) {
		messageType = 5;
		messagePayload = bitmap;
	}
	
	public void setBitField(Vector<Integer> bitmap) {
		messagePayload = bitmap;
	}
	
	public Vector<Integer>getBitField() {
		return messagePayload;
	}
	
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public void handleMessage(Messages msg, peerProcess myPeer) {
		BitfieldMessage message = (BitfieldMessage) msg;

	}
}
