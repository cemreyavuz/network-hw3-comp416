package networkhw3.utils.input;

import javafx.util.Pair;
import networkhw3.Node;
import networkhw3.utils.logging.Logger;
import networkhw3.utils.logging.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Input {
  private Logger logger = LoggerFactory.getInstance().getLogger(getClass());
  private static volatile Input _instance = null;
  private Scanner input;
  private ArrayList<Node> nodeList;
  private final String INPUT_FILE_NAME = "input.txt";

  private Input() {

  }

  public static synchronized Input getInstance() {
    if(_instance == null) {
      _instance = new Input();
    }
    return _instance;
  }

  public ArrayList readFile() throws IOException {
    BufferedReader in = null;
    ArrayList<Node> nodeList = new ArrayList<>();
    try {
      in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

      String line;
      while((line = in.readLine()) != null) {
        Node n = processData(line);
        nodeList.add(n);
      }
    }
    catch (IOException e){
      logger.e(e.toString());
    }
    return nodeList;
  }

  private Node processData(String s) {
    int indexID = getNodeID(s);
    int nodeID = Integer.parseInt(s.substring(0, indexID));
    Hashtable<Integer, Integer> linkCost = getLinkCost(s);
    Node n = new Node(nodeID, linkCost);
    return n;
  }

  private int getNodeID(String s) {
    for(int i = 0; i < s.length(); i++) {
      if(s.charAt(i) == ',') {
        return i;
      }
    }
    logger.d("NodeID cannot found in following String: " + s);
    return -1;
  }

  private Hashtable<Integer, Integer> getLinkCost(String s) {
    Hashtable<Integer, Integer> linkCost = new Hashtable<>();
    Pair <Integer, Pair<Integer, Integer>> p;
    while((p = getNeighborCostPair(s)) != null) {
      linkCost.put(p.getValue().getKey(), p.getValue().getValue());
      s = s.substring(p.getKey());
    }
    return linkCost;
  }

  private Pair<Integer, Pair<Integer, Integer>> getNeighborCostPair(String s) {
    int neighborID;
    int cost;
    for(int i = 0; i < s.length(); i++) {
      if(s.charAt(i) == '(') {
        for(int j = i; j < s.length(); j++) {
          if(s.charAt(j) == ',') {
            neighborID = Integer.parseInt(s.substring(i+1,j));
            for(int k = j; k < s.length(); k++) {
              if(s.charAt(k) == ')') {
                cost = Integer.parseInt(s.substring(j+1,k));
                return new Pair<>(k ,new Pair<>(neighborID, cost));
              }
            }
          }
        }
      }
    }
    return null;
  }


}
