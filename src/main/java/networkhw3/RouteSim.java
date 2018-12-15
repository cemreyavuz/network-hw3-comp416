package networkhw3;

import javafx.util.Pair;
import networkhw3.utils.input.Input;
import networkhw3.utils.logging.Logger;
import networkhw3.utils.logging.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class RouteSim {
  private Input input = Input.getInstance();
  private Logger logger = LoggerFactory.getInstance().getLogger(getClass());
  private int numNode;
  private ArrayList<Node> nodeList;

  public RouteSim() {

  }

  public void run() throws IOException {
    logger.i("RouteSim is working!");
    logger.i("RouteSim started to read the input file.");
    ArrayList pairList = input.readFile();
    initializeNodes(pairList);
    runLoop();
  }

  private void runLoop() {
    for(int i = 0; i < 5; i++) {
      for(Node n: nodeList) {
        n.sendUpdate();
      }
      System.out.println(nodeList);
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
