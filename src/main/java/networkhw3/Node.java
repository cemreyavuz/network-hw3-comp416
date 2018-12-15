package networkhw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

public class Node {
  private int nodeID;
  private Hashtable<Integer, Integer> linkCost;
  private ArrayList<Node> neighborList;
  private int [][] distanceTable;
  private int [] distanceVector = null;
  private int nodeNum;
  private ArrayList<Integer> dynamicLinks;
  private Random r = new Random();

  public Node(int nodeID, Hashtable<Integer, Integer> linkCost, int nodeNum) {
    this.nodeID = nodeID;
    this.linkCost = linkCost;
    this.nodeNum = nodeNum;
    dynamicLinks = new ArrayList<>();
    initializeTable();
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
    if(isChanged) {
      sendUpdate();
    }
  }

  public boolean sendUpdate() {
    updateDynamicLinks();
    if(updateDistanceVector()) {
      notifyNeighbors();
      return true;
    }
    return false;
  }

  private void notifyNeighbors() {
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

  public Hashtable<String, Integer> getForwardingTable() {
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
      distanceTable[i][i] = linkCost.get(i);
    }
  }

  private void updateDynamicLinks() {
    for(int i: dynamicLinks) {
      if(r.nextBoolean()) {
        linkCost.put(i, r.nextInt(10)+1);
      }
    }
  }

  public String toString() {
    String s = "";
    s += "\nNodeID: " + nodeID + "\n";
    s += "Link Costs: \n";
    for(Integer key: linkCost.keySet()) {
      s+= "\t" + "NeigborID: " + key + ", Cost: " + linkCost.get(key) + "\n";
    }
    printTable(distanceTable);
    System.out.println(getForwardingTable());
    return s;
  }

  private void printTable(int arr[][]) {
    for(int i = 0; i < arr.length; i++) {
      for(int j = 0; j < arr[i].length; j++) {
        System.out.print(arr[i][j] + ", ");
      }
      System.out.print("\n");
    }
    System.out.println("###############");
  }
}
