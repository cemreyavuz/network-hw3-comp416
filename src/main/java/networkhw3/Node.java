package networkhw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

import networkhw3.utils.logging.Logger;
import networkhw3.utils.logging.LoggerFactory;

public class Node {
  public int nodeID;
  public Hashtable<Integer, Integer> linkCost;
  private ArrayList<Node> neighborList;
  private int [][] distanceTable;
  private int [] distanceVector = null;
  private int nodeNum;
  private ArrayList<Integer> dynamicLinks;
  private Random r = new Random();
  private Logger logger = LoggerFactory.getInstance().getLogger(getClass());

  public Node(int nodeID, Hashtable<Integer, Integer> linkCost, int nodeNum) {
    this.nodeID = nodeID;
    this.linkCost = linkCost;
    this.nodeNum = nodeNum;
    dynamicLinks = new ArrayList<>();
    initializeTable();
    this.toString();
  }

  public void addNeighbors(ArrayList<Node> nodeList) {
    neighborList = new ArrayList<>();
    for(int k: linkCost.keySet()) {
      this.neighborList.add(nodeList.get(k));
    }
  }

  public void receiveUpdate(Message m) {
    int[] newVector = m.getDistanceVector();
    boolean isChanged = false;
    for(int i = 0; i < nodeNum; i++) {
      int tmpDistance = newVector[i] + linkCost.get(m.getSenderID());
      if(distanceTable[i][m.getSenderID()] > tmpDistance) {
        distanceTable[i][m.getSenderID()] = tmpDistance;
        isChanged = true;
      }
    }
    System.out.println("Them message sent from " + m.getSenderID() + " to " + m.getReceiverID() + "\n");
    System.out.println("Node " + nodeID);
    printDistanceTable();
    if(isChanged){
      System.out.println("\nThe distance table is changed\n");
    }else{
      System.out.println("\nThe distance table isn't changed\n");
    }
  }

  public boolean sendUpdate() {
    if(updateDistanceVector()) {
      notifyNeighbors();
      return true;
    }
    return false;
  }

  private void notifyNeighbors() {
    System.out.print("Sending message from " + nodeID+ " to ");
    for(Node n: neighborList) {
      System.out.print(n.nodeID + " ");
    }
    System.out.println("The message is: \n");
    printDistanceVector();
    System.out.println("\n");
    for(Node n: neighborList) {
      n.receiveUpdate(new Message(this.nodeID, n.nodeID, distanceVector));
    }
  }

  private boolean updateDistanceVector() {
    int tempDistanceVector[] = new int[nodeNum];
    for(int i = 0; i < distanceTable.length; i++) {
      int minNum = 999;
      for(int j = 0; j < distanceTable[i].length; j++) {
        minNum = Math.min(distanceTable[i][j], minNum);
      }
      tempDistanceVector[i] = minNum;
    }
    if(!Arrays.equals(distanceVector, tempDistanceVector)) {
      distanceVector = tempDistanceVector;
      return true;
    }
    else {
      return false;
    }
  }

  public Hashtable<Integer, Integer> getForwardingTable() {
    // TODO: nodeID'leri String yap
    Hashtable forwardingTable = new Hashtable<Integer, Integer>();
    for(int i = 0; i < distanceTable.length; i++) {
      int minNum = 999;
      int via = -1;
      for(int j = 0; j < distanceTable[i].length; j++) {
        if(distanceTable[i][j] < minNum) {
          minNum = distanceTable[i][j];
          via = j;
        }
      }
      forwardingTable.put(i, via);
    }
    return forwardingTable;
  }

  private void initializeTable() {
    distanceTable = new int[nodeNum][nodeNum];
    for(int i = 0; i < distanceTable.length; i++) {
      for(int j = 0; j < distanceTable[i].length; j++) {
        if(i == nodeID && j == nodeID) {
          distanceTable[i][j] = 0;
        }
        else {
          distanceTable[i][j] = 999;
        }
      }
    }
    for(int i: linkCost.keySet()) {
      if(linkCost.get(i) == -1) {
        distanceTable[i][i] = r.nextInt(10) + 1;
        dynamicLinks.add(i);
      }
      else {
        distanceTable[i][i] = linkCost.get(i);
      }
    }
  }

  public void updateDynamicLinks(int neighborID, int cost) {
    linkCost.put(neighborID, cost);
    distanceTable[neighborID][neighborID] = cost;
  }

  public String toString() {
    /*
    String s = "";
    s += "\nNodeID: " + nodeID + "\n";
    s += "Link Costs: \n";
    for(Integer key: linkCost.keySet()) {
      s+= "\t" + "NeigborID: " + key + ", Cost: " + linkCost.get(key) + "\n";
    }
    */
    System.out.println("\nNode " + nodeID + ": ");

    System.out.println("\nDistance Table:\n");
    printDistanceTable();

    System.out.println("\nForwarding Table:\n");
    printForwardingTable();
    return "";
  }

  public void printDistanceTable() {
    System.out.println("Via:      0       1       2       3       4");
    System.out.println("----     ---     ---     ---     ---     ---");
    for(int i = 0; i < distanceTable.length; i++) {
      System.out.print("To " + i + ": ");
      for(int j = 0; j < distanceTable[i].length; j++) {
        System.out.printf("|%5d  ", distanceTable[i][j]);
      }
      System.out.print("\n");
    }
  }

  public void printForwardingTable() {
    System.out.println(" ___________");
    System.out.println("| To | From |");
    System.out.println("|____|______|");
    ArrayList<Integer> keys = new ArrayList<Integer>(getForwardingTable().keySet());
    Collections.reverse(keys);
    for(int i: keys){
      System.out.printf("|%2d  |  %2d  |\n", i, getForwardingTable().get(i));
      System.out.println("|____|______|");
    }    
  }

  private void printDistanceVector() {
    System.out.println("To:        0     1     2     3     4");
    System.out.println("---       ---   ---   ---   ---   ---");
    System.out.print("From " + nodeID + "  ");
    for(int i = 0; i < distanceVector.length; i++) {
        System.out.printf("|%3d  ", distanceVector[i]);
    }   
    System.out.print("|");
  }
}
