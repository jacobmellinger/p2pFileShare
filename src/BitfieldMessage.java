import java.io.PrintWriter;
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
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex, PrintWriter writer) {
		BitfieldMessage message = (BitfieldMessage) msg;

		// All we need to do is update our knowledge of their bitMap
		myPeer.peerInfoVector.get(neighborPeerIndex).bitMap = message.messagePayload;
		myPeer.peerInfoVector.get(neighborPeerIndex).hasReceivedBitfield = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 5;
		return 1;
	}
}
