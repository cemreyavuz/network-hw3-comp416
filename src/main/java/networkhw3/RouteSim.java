package networkhw3;

import javafx.util.Pair;
import networkhw3.utils.input.Input;
import networkhw3.utils.logging.Logger;
import networkhw3.utils.logging.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class RouteSim {
  private Input input = Input.getInstance();
  private Logger logger = LoggerFactory.getInstance().getLogger(getClass());
  private int numNode;
  private ArrayList<Node> nodeList;
  private ArrayList<Pair> dynamicLinks;
  private Random r = new Random();

  public RouteSim() {

  }

  public void run() throws IOException {
    logger.i("RouteSim is working!");
    logger.i("RouteSim started to read the input file.");
    Pair<ArrayList, ArrayList> p = input.readFile();
    ArrayList pairList = p.getKey();
    dynamicLinks = p.getValue();
    initializeNodes(pairList);
    runLoop();
  }

  private void runLoop() {
    Boolean isChanged = true;
    int counter = 0;
    while(isChanged) {
      isChanged = false;
      updateDynamicLinks();
      for (Node n : nodeList) {
        if (n.sendUpdate())
          isChanged = true;
      }
      counter++;
    }
    System.out.println(counter);
  }

  private void updateDynamicLinks() {
    for(Pair<Integer, Integer> p: dynamicLinks) {
      if(r.nextBoolean()) {
        int dynamicCost = r.nextInt(10)+1;
        nodeList.get(p.getKey()).updateDynamicLinks(p.getValue(), dynamicCost);
        nodeList.get(p.getValue()).updateDynamicLinks(p.getKey(), dynamicCost);
        // TODO: duplicate changes (not crucial but do it if you have time)
      }
    }
  }

  private void initializeNodes(ArrayList<Pair> pairList) {
    nodeList = new ArrayList<Node>();
    for(Pair<Integer, Hashtable<Integer, Integer>> p: pairList) {
      nodeList.add(new Node(p.getKey(), p.getValue(), pairList.size()));
    }
    for(Node n: nodeList) {
      n.addNeighbors(nodeList);
    }
  }
}
