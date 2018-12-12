package networkhw3;

public class Message {
  private String senderID;
  private String receiverID;
  private int[] distanceVector;

  public Message(String senderID, String receiverID, int[] distanceVector) {
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.distanceVector = distanceVector;
    this.run();
  }

  private void run() {

  }
}
