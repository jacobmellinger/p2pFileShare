import java.util.Vector;

public class OtherPeerInfo {
    public int peerId;
    public String peerAddress;
    public int peerPort;
    public Vector<Integer> bitMap;
    public boolean hasReceivedHandshake = false;

    public boolean hasReceivedBitfield = false;
    public boolean hasSentBitfield = false;

    public boolean hasSentHaveMessage;

    public boolean isInterested;

    public boolean isPreferredNeighbor;
    public boolean isOptimisticallyUnchoked;
    public int lastMessageReceived;
    public int lastMessageSent;

    public int numberPeicesReceived;

    public OtherPeerInfo() {

    }

    public OtherPeerInfo(int pId, String pAddress, int pPort, int hasFile, int sizeOfBitMap) {
        peerId = pId;
        peerAddress = pAddress;
        peerPort = pPort;
        Vector<Integer> bitMap = new Vector<>();
        for (int i = 0; i < sizeOfBitMap; i++) {
            if (hasFile == 1) {
                bitMap.addElement(1);
            } else {
                bitMap.addElement(0);
            }
        }
    }
}
