package networkhw3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

public class Node {
  public int nodeID;
  public Hashtable<Integer, Integer> linkCost;
  private ArrayList<Node> neighborList;
  private int [][] distanceTable;
  private ArrayList<Integer> updatedLinks;
  private int [] distanceVector = null;
  private int nodeNum;
  private ArrayList<Integer> dynamicLinks;
  private Random r = new Random();

  public Node(int nodeID, Hashtable<Integer, Integer> linkCost, int nodeNum) {
    this.nodeID = nodeID;
    this.linkCost = linkCost;
    this.nodeNum = nodeNum;
    dynamicLinks = new ArrayList<>();
    updatedLinks = new ArrayList<>();
    initializeTable();
    this.toString();
  }

  /**
   * Creates the neighbor node list
   * @param nodeList the list of neighbor nodes
   */
  public void addNeighbors(ArrayList<Node> nodeList) {
    neighborList = new ArrayList<>();
    for(int k: linkCost.keySet()) {
      this.neighborList.add(nodeList.get(k));
    }
  }

  /**
   * Recieves the distance vector from another node and updates itself accordingly
   * @param m message
   */
  public void receiveUpdate(Message m) {
    int[] newVector = m.getDistanceVector();
    boolean isChanged = false;
    for(int i = 0; i < nodeNum; i++) {
      int tmpDistance = newVector[i] + distanceTable[m.getSenderID()][m.getSenderID()];
      if(distanceTable[i][m.getSenderID()] != tmpDistance) {
        isChanged = true;
      }
      distanceTable[i][m.getSenderID()] = tmpDistance;
    }
    
    System.out.println("..................................................\n");
    System.out.println("The message sent from " + m.getSenderID() + " to " + m.getReceiverID() + "\n");
    System.out.println("Node " + nodeID);
    printDistanceTable();
    if(isChanged){
      System.out.println("\nThe distance table is changed\n");
    }else{
      System.out.println("\nThe distance table isn't changed\n");
    }
  }

  /**
   * Sends update to each neighbour node
   * @return a boolean designating if the distance vector is changed
   */
  public boolean sendUpdate() {
    if(updateDistanceVector()) {
      notifyNeighbors();
      return true;
    }
    return false;
  }

  /**
   * Notifies neighbour nodes
   */
  private void notifyNeighbors() {
    System.out.println("..................................................\n");
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

  /**
   * Updates the distance vector
   * @return a boolean designating wheter the distance vector is updated
   */
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

  /**
   * Creates the forwarding table
   * @return the forwarding table
   */
  public Hashtable<Integer, ArrayList<Integer>> getForwardingTable() {
    Hashtable<Integer, ArrayList<Integer>> forwardingTable = new Hashtable<>();
    for(int i = 0; i < distanceTable.length; i++) {
      int minNum = 999;
      ArrayList<Integer> via = new ArrayList<>(nodeID);
      for(int j = 0; j < distanceTable[i].length; j++) {
        if(distanceTable[i][j] < minNum) {
          minNum = distanceTable[i][j];
          via.removeAll(via);
          via.add(j);
        }else if(distanceTable[i][j] == minNum){
          via.add(j);
        }
      }
      forwardingTable.put(i, via);
    }
    return forwardingTable;
  }

  /**
   * Initializes the distance table
   */
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

  /**
   * Sets the dynamic links new cost
   * @param neighborID Id of the neighbour
   * @param cost the new cost to the neighbour
   */
  public void updateDynamicLinks(int neighborID, int cost) {
    if(!updatedLinks.contains(neighborID)){
      updatedLinks.add(neighborID);
      linkCost.put(neighborID, cost);
      distanceTable[neighborID][neighborID] = cost;
    }
  }
  
  /**
   * Resets the updated nodes
   */
  public void resetUpdates(){
    updatedLinks.removeAll(updatedLinks);
  }

  public String toString() {
    System.out.println("\nNode " + nodeID + ": ");

    System.out.println("\nDistance Table:\n");
    printDistanceTable();

    System.out.println("\nForwarding Table:\n");
    printForwardingTable();
    return "";
  }

  /**
   * Prints distance table
   */
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

  /**
   * Prints forwarding table
   */
  public void printForwardingTable() {
    System.out.println(" ______________");
    System.out.println("| To |   From  |");
    System.out.println("|____|_________|");
    ArrayList<Integer> keys = new ArrayList<Integer>(getForwardingTable().keySet());
    Collections.reverse(keys);
    for(int i: keys){
      System.out.printf("|%2d  |   ", i);
      for(int j: getForwardingTable().get(i)){
        System.out.printf("%d  ", j);
      }
      System.out.print("\n");
      System.out.println("|____|_________|");
    }    
  }

  /**
   * Prints distance vector
   */
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
