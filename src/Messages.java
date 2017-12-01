//package messages;


import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Messages implements Serializable {
	
	int messageType;
	public abstract int getMessageType();
	public abstract int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex);

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
		if (myPeer.peerInfoVector.get(otherPeerIndex).hasReceivedHandshake && !myPeer.peerInfoVector.get(otherPeerIndex).hasSentBitfield) {
			BitfieldMessage newMessage = new BitfieldMessage(myPeer.peerInfoVector.get(myPeerIndex).bitMap);
			return newMessage;
		}


		// Sending interested/notInterested Messages when I've already gotten their bitfield Message
		else if (myPeer.peerInfoVector.get(otherPeerIndex).hasReceivedBitfield && !myPeer.peerInfoVector.get(otherPeerIndex).hasSentHaveMessage) {
			for (int i = 0; i < myPeer.peerInfoVector.size(); i++) {

				// Do they have a peice that I don't?
				if (myPeer.peerInfoVector.get(otherPeerIndex).bitMap.get(i) == 1 && myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(i) == 0) {
					InterestedMessage newMessage = new InterestedMessage();
					return newMessage;
				}
				else {
					NotInterestedMessage newMessage = new NotInterestedMessage();
					return newMessage;
				}
			}
		}

		// If I just received an UnchokeMessage -> send them a RequestMessage
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceived == 1) {
			// pick a random peice that my neighbor peice has
			// check that we don't have the peice
			// check that we haven't already requested that peice  (in our pendingRequests LinkedList)
			// then create a new message with that index

			// get a random piece that my neighbor has but I don't
			boolean haveFound = false;
			while (!haveFound) {
				int randomNumber = ThreadLocalRandom.current().nextInt(0, myPeer.peerInfoVector.get(myPeerIndex).bitMap.size());
				// Do they have the piece and we're missing the piece?
				if (myPeer.peerInfoVector.get(otherPeerIndex).bitMap.get(randomNumber) == 1 && myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(randomNumber) == 0) {
					for (int i = 0; i < myPeer.pendingRequests.size(); i++) {
						if (myPeer.pendingRequests.get(i) == randomNumber) {
							haveFound = true;
						}
					}
				}
			}

			RequestMessage neweMessage = new RequestMessage();
		}

		//none of the above if statements caught
		return null;
	}
}
