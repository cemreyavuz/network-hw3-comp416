package networkhw3.utils.input;

import networkhw3.utils.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Input {
  private static volatile Input _instance = null;
  private int nodeID;
  private ArrayList<Pair> pairList;
  private ArrayList<Pair> dynamicLinks = new ArrayList<>();
  private final String INPUT_FILE_NAME = "input.txt";

  private Input() {

  }

  /**
   * getInstance method for Singleton Input class.
   * @return a Singleton Input object
   */
  public static synchronized Input getInstance() {
    if(_instance == null) {
      _instance = new Input();
    }
    return _instance;
  }

  /**
   * This function controls the process of reading the file.
   * It reads the input file line by line and passes those
   * lines to 'processData()' function.
   * @return a pair of ArrayList
   * @throws IOException if an exception occurs during the process of reading the file
   */
  public Pair readFile() throws IOException {
    BufferedReader in = null;
    pairList = new ArrayList<>();
    try {
      in = new BufferedReader(new FileReader(INPUT_FILE_NAME));
      String line;
      while((line = in.readLine()) != null) {
        Pair p = processData(line);
        pairList.add(p);
      }
    }
    catch (IOException e){
    }
    return new Pair<ArrayList, ArrayList>(pairList, dynamicLinks);
  }

  /**
   * This function parses the lines in the input file.
   * @param s lines in the input file
   * @return a pair of Integer-Hashtable (nodeID, linkCost)
   */
  private Pair processData(String s) {
    int indexID = getNodeID(s);
    nodeID = Integer.parseInt(s.substring(0, indexID));
    Hashtable<Integer, Integer> linkCost = getLinkCost(s);
    return new Pair<Integer, Hashtable<Integer, Integer>>(nodeID, linkCost);
  }

  /**
   * This function searches the nodeID in given String
   * @param s String to find nodeID
   * @return nodeID if it finds the nodeID, otherwise -1.
   */
  private int getNodeID(String s) {
    for(int i = 0; i < s.length(); i++) {
      if(s.charAt(i) == ',') {
        return i;
      }
    }
    return -1;
  }

  /**
   * This function creates the linkCost table by parsing given String
   * @param s String to creates the linkCost table from
   * @return a HashTable for link costs
   */
  private Hashtable<Integer, Integer> getLinkCost(String s) {
    Hashtable<Integer, Integer> linkCost = new Hashtable<>();
    Pair <Integer, Pair<Integer, Integer>> p;
    while((p = getNeighborCostPair(s)) != null) {
      linkCost.put(p.getValue().getKey(), p.getValue().getValue());
      s = s.substring(p.getKey());
    }
    return linkCost;
  }

  /**
   * This function searches the Neighbor-Cost pairs in given String
   * @param s String to find pairs
   * @return a pair of Integer-(Pair of Integer-Integer)
   */
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
                try {
                  cost = Integer.parseInt(s.substring(j+1,k));
                }
                catch(NumberFormatException n) {
                  dynamicLinks.add(new Pair<Integer, Integer>(nodeID, neighborID));
                  cost = -1;
                }
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
