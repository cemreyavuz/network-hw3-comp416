package networkhw3;

public class Message {
  private int senderID;
  private int receiverID;
  private int[] distanceVector;

  public Message(int senderID, int receiverID, int[] distanceVector) {
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.distanceVector = distanceVector;
  }

  public int getSenderID() {
    return this.senderID;
  }

  public int getReceiverID() {
    return this.receiverID;
  }

  public int[] getDistanceVector() {
    return this.distanceVector;
  }
}
