package messages;

public class PieceMessage extends Messages{

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
	public void handleMessage(Messages msg) {
		PieceMessage message = (PieceMessage) msg;

	}
}
