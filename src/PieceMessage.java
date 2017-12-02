import java.io.PrintWriter;

public class PieceMessage extends Messages {

	private int messageType;
	private int messageLength;
	private int indexField;
	private byte[] pieceContents;

	
	
	PieceMessage(){
		messageType = 7;
	}

	PieceMessage(int index){
		messageType = 7;
		indexField = index;
	}
	
	PieceMessage(int index, byte[] indexContents){
		messageType = 7;
		indexField = index;
		pieceContents = indexContents;
	}
	
	public byte[] getPieceContents() {
		return pieceContents;
	}
	
	public void setPieceContents(byte[] newContents) {
		pieceContents = newContents;
	}
	
	
	@Override
	public int getMessageType() {
		return messageType;
	}

	@Override
	public int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex, PrintWriter writer) {
		PieceMessage message = (PieceMessage) msg;

		int pieceIndex = message.indexField;

		// Just set the index in their bitMap even though it's probably already 1
		myPeer.peerInfoVector.get(neighborPeerIndex).bitMap.set(indexField, 1);

		// Store the new piece on our client machine
		myPeer.fileByteArray[message.indexField] = message.pieceContents;
		myPeer.peerInfoVector.get(neighborPeerIndex).lastMessageReceivedFromPeer = 7;
		myPeer.peerInfoVector.get(neighborPeerIndex).numberPiecesReceivedFromPeer++;
		writer.println("[" + System.currentTimeMillis() + "]: Peer " + myPeer.myPeerID + " has downloaded the piece " + message.indexField + " from " + myPeer.peerInfoVector.get(neighborPeerIndex).peerId);

		int myIndex = -1;
		for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
			if (myPeer.peerInfoVector.get(i).peerId == myPeer.myPeerID) {
				myIndex = i;
			}
		}
		int counter = 0;
		if (myIndex != -1) {
			for (int i = 0; i < myPeer.peerInfoVector.get(myIndex).bitMap.size(); i++) {
				if (myPeer.peerInfoVector.get(myIndex).bitMap.get(i) == 1) {
					counter++;
				}
			}
		}

		writer.println("Now the number of pieces it has is " + counter);
		return 1;
	}
}
