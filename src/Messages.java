//package messages;


import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Messages implements Serializable {
	
	int messageType;
	public boolean errorMsg = false;
	public abstract int getMessageType();
	public abstract int handleMessage(Messages msg, peerProcess myPeer, int neighborPeerIndex);

	public Messages createMessage(int myPeerID, int otherPeerID, peerProcess myPeer) {



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


		// Haven't sent a Handshake? -> send one
		if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer == -1) {
			Handshake newMessage = new Handshake(myPeerID);
			myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 8;
			return newMessage;
		}

		// Sent a Handshake but haven't gotten anything from them yet -> don't need to send them anything
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == -1 && myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer == 8) {
			Handshake newMessage = new Handshake(myPeerID);

			// Don't need to send them anything
			newMessage.errorMsg = true;
			return newMessage;
		}

		// Just received a Handshake -> send a bitfield
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer != -1 && myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 8) {
			BitfieldMessage newMessage = new BitfieldMessage(myPeer.peerInfoVector.get(myPeerIndex).bitMap);
			myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 5;
			return newMessage;
		}

		// Just received Unchoke -> send a Request
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 1) {

			// get a random piece that my neighbor has but I don't
			boolean haveFound = false;
			int indexToRequest = -1;
			while (!haveFound) {
				int randomNumber = ThreadLocalRandom.current().nextInt(0, myPeer.peerInfoVector.get(myPeerIndex).bitMap.size());
				// Do they have the piece and we're missing the piece?
				if (myPeer.peerInfoVector.get(otherPeerIndex).bitMap.get(randomNumber) == 1 && myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(randomNumber) == 0) {

					// Make sure I don't already have a request out for this piece
					boolean haveRequestedPiece = false;
					for (int i = 0; i < myPeer.pendingRequests.size(); i++) {
						if (myPeer.pendingRequests.get(i) == randomNumber) {
							haveRequestedPiece = true;
							break;
						}
					}
					if (haveRequestedPiece == false) {
						haveFound = true;
						indexToRequest = randomNumber;
					}
				}
			}
			RequestMessage newMessage = new RequestMessage(indexToRequest);
			myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 6;
			return newMessage;
		}

		// Just received ChokeMessage -> don't need to send them anything
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 0) {
			Handshake newMessage = new Handshake();
			newMessage.errorMsg = true;
			return newMessage;
		}

		// Just received a bitfield or a Have message -> Send and Interested/NotInterested
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 5 || myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 4) {

			for (int i = 0; i < myPeer.peerInfoVector.get(otherPeerIndex).bitMap.size(); i++) {
				// Do they have a peice that I don't?
				if (myPeer.peerInfoVector.get(otherPeerIndex).bitMap.get(i) == 1 && myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(i) == 0) {
					InterestedMessage newMessage = new InterestedMessage();
					myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 2;
					return newMessage;
				}
				else {
					NotInterestedMessage newMessage = new NotInterestedMessage();
					myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 3;
					return newMessage;
				}
			}
		}

		// Just received a piece message -> possibly send notInterested to some of my neighbors && possibly send another request
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 7) {
			// Just got a new piece, am I still interested in any of their pieces?
			boolean stillInterested = false;
			for (int i = 0; i < myPeer.peerInfoVector.get(otherPeerIndex).bitMap.size(); i++) {
				if (myPeer.peerInfoVector.get(otherPeerIndex).bitMap.get(i) == 1 && myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(i) == 0) {
					stillInterested = true;
				}
			}

			// If they still have pieces that I want -> pick one randomly to request (same code as above :(  )
			if (stillInterested) {
				// get a random piece that my neighbor has but I don't
				boolean haveFound = false;
				int indexToRequest = -1;
				while (!haveFound) {
					int randomNumber = ThreadLocalRandom.current().nextInt(0, myPeer.peerInfoVector.get(myPeerIndex).bitMap.size());
					// Do they have the piece and we're missing the piece?
					if (myPeer.peerInfoVector.get(otherPeerIndex).bitMap.get(randomNumber) == 1 && myPeer.peerInfoVector.get(myPeerIndex).bitMap.get(randomNumber) == 0) {

						// Make sure I don't already have a request out for this piece
						boolean haveRequestedPiece = false;
						for (int i = 0; i < myPeer.pendingRequests.size(); i++) {
							if (myPeer.pendingRequests.get(i) == randomNumber) {
								haveRequestedPiece = true;
								break;
							}
						}
						if (haveRequestedPiece == false) {
							haveFound = true;
							indexToRequest = randomNumber;
						}
					}
				}
				RequestMessage newMessage = new RequestMessage(indexToRequest);
				myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 6;
				return newMessage;
			}
			else {
				NotInterestedMessage newMessage = new NotInterestedMessage();
				myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 3;
				return newMessage;
			}
		}

		// Just Received request message -> give them the piece they requested
		else if (myPeer.peerInfoVector.get(otherPeerIndex).lastMessageReceivedFromPeer == 6) {
			int index = myPeer.peerInfoVector.get(otherPeerIndex).indexTheyRequested;
			PieceMessage newMessage = new PieceMessage(index);
			myPeer.peerInfoVector.get(otherPeerIndex).lastMessageSentToPeer = 7;
			return newMessage;
		}

		// none of the above apply
		return null;
	}
}
