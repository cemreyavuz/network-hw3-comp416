package networkhw3;

import java.util.Hashtable;

public class Node {
  private int nodeID;
  private Hashtable<String, Integer> linkCost;
  private int [][] distanceTable;

  public Node(int nodeID, Hashtable<String, Integer> linkCost) {
    this.nodeID = nodeID;
    this.linkCost = linkCost;
  }

  public void receiveUpdate(Message m) {

  }

  public boolean sendUpdate() {
    return false; // placeholder
  }

  public Hashtable<String, Integer> getForwardingTable() {
    return null; // placeholder
  }
}
