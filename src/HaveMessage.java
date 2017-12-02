import java.io.PrintWriter;

public class HaveMessage extends Messages {

	private int messageType;
	private int messageLength;
	private int messagePayload;
	
	HaveMessage(){
		messageType = 4;
	}
	
	HaveMessage(int newLength) {
		messageType = 4;
		messageLength = newLength;
	}
	
	HaveMessage (int newLength, int newPayload){
		messageType = 4;
		messageLength = newLength;
		messagePayload = newPayload;
	}
	
	public int getPayload () {
		return messagePayload;
	}
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex, PrintWriter writer) {
		HaveMessage message = (HaveMessage) msg;

		// Update our representation of their bitMap with the peice they just said they have.
		myPeer.peerInfoVector.get(neighborPeerIndex).bitMap.set(messagePayload, 1);

		myPeer.peerInfoVector.get(neighborPeerIndex).hasReceivedHaveMessage = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 4;
		writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeer.myPeerID + " received the 'have' message from " + myPeer.peerInfoVector.get(neighborPeerIndex).peerId + " for the piece " + message.messagePayload);
		return 1;
	}

}
