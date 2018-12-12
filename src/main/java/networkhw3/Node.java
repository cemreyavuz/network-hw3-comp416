package networkhw3;

import java.util.Hashtable;

public class Node {
  private int nodeID;
  private Hashtable<Integer, Integer> linkCost;
  private int [][] distanceTable;

  public Node(int nodeID, Hashtable<Integer, Integer> linkCost) {
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

  public String toString() {
    String s = "";
    s += "\nNodeID: " + nodeID + "\n";
    s += "Link Costs: \n";
    for(Integer key: linkCost.keySet()) {
      s+= "\t" + "NeigborID: " + key + ", Cost: " + linkCost.get(key) + "\n";
    }
    return s;
  }
}
