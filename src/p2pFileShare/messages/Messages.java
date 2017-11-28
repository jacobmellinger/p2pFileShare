package messages;

import java.io.Serializable;

public abstract class Messages implements Serializable {
	
	int messageType;
	public abstract int getMessageType();
	public abstract void handleMessage(Messages msg);
}
