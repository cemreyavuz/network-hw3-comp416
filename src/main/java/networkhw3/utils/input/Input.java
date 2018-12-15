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
  private ArrayList<Pair> pairList;
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
      logger.e(e.toString());
    }
    return pairList;
  }

  private Pair processData(String s) {
    int indexID = getNodeID(s);
    int nodeID = Integer.parseInt(s.substring(0, indexID));
    Hashtable<Integer, Integer> linkCost = getLinkCost(s);
    return new Pair<Integer, Hashtable<Integer, Integer>>(nodeID, linkCost);
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
                try {
                  cost = Integer.parseInt(s.substring(j+1,k));
                }
                catch(NumberFormatException n) {
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
