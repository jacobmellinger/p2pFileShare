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
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex) {
		HaveMessage message = (HaveMessage) msg;

		// Update our representation of their bitMap with the peice they just said they have.
		myPeer.peerInfoVector.get(neighborPeerIndex).bitMap.set(messagePayload, 1);

		myPeer.peerInfoVector.get(neighborPeerIndex).hasReceivedHaveMessage = true;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceived = 4;
		return 1;
	}

}
