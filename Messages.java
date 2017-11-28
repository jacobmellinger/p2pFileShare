//package messages;

import com.sun.xml.internal.xsom.XSWildcard;

import java.io.Serializable;
import java.util.Vector;

public abstract class Messages implements Serializable {
	
	int messageType;
	public abstract int getMessageType();
	public abstract void handleMessage(Messages msg, peerProcess myPeer);

	public Messages createMessage(int myPeerID, int otherPeerID, peerProcess myPeer) {
		if (otherPeerID == -1) {
			Handshake newMessage =  new Handshake(myPeerID);
			return newMessage;
		}

		// Figure out where in the peerInfoVector my info is and my neighbors info is
		int myPeerIndex = 0;
		int otherPeerIndex = 0;
		for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {
			if (myPeer.peerInfoVector.get(i).peerId == myPeerID){
				myPeerIndex = i;
			}
			else if (myPeer.peerInfoVector.get(i).peerId == otherPeerID) {
				otherPeerIndex = i;
			}
		}
		// should check to make sure the peerIndex's were initialized in the loop


		//Received a Handshake but no other messages
		if (myPeer.peerInfoVector.get(myPeerIndex).hasReceivedHandshake && !myPeer.peerInfoVector.get(myPeerIndex).hasSentBitfield) {
			BitfieldMessage newMessage = new BitfieldMessage(myPeer.peerInfoVector.get(myPeerIndex).bitMap);
			return newMessage;
		}

		// remove below line
		return new Handshake(myPeerID);
	}
}
