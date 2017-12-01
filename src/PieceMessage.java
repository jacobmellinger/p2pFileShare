public class PieceMessage extends Messages {

	private int messageType;
	private int messageLength;
	private int indexField;
	private String pieceContents;	
	
	
	PieceMessage(){
		messageType = 7;
	}

	PieceMessage(int index){
		messageType = 7;
		indexField = index;
	}
	
	PieceMessage(int index, String indexContents){
		messageType = 7;
		indexField = index;
		pieceContents = indexContents;
	}
	
	public String getPieceContents() {
		return pieceContents;
	}
	
	public void setPieceContents(String newContents) {
		pieceContents = newContents;
	}
	
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public void handleMessage(Messages msg, peerProcess2 myPeer, int neighborPeerIndex) {
		PieceMessage message = (PieceMessage) msg;

		int pieceIndex = message.indexField;

		// Just set the index in their bitMap even though it's probably already 1
		myPeer.peerInfoVector.get(neighborPeerIndex).bitMap.set(indexField, 1);

		// How to store the stream of bits that were sent?

		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceived = 7;
	}
}
