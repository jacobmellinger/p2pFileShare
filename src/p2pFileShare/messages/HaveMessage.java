package p2pFileShare.messages;

public class HaveMessage extends Messages{

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

}
