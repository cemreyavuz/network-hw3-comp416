package networkhw3;

import networkhw3.utils.input.Input;
import networkhw3.utils.logging.Logger;
import networkhw3.utils.logging.LoggerFactory;
import networkhw3.utils.Pair;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class RouteSim {
  private Input input = Input.getInstance();
  private Logger logger = LoggerFactory.getInstance().getLogger(getClass());
  private int numNode;
  private Hashtable<String, Edge> edgeList;
  private ArrayList<Node> nodeList;
  private ArrayList<Pair> dynamicLinks;
  private Random r = new Random();
  private Graph graph;
  private GraphView gv;

  public RouteSim() {
    edgeList = new Hashtable<>();
    System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
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
    int counter = 1;
    while(isChanged) {
      System.out.println("Iteration " + counter + " \n");
      isChanged = false;
      updateDynamicLinks();
      for (Node n : nodeList) {
        if (n.sendUpdate())
          isChanged = true;
      }
      for (Node n : nodeList) {
        System.out.println("Node " + n.nodeID);
        n.printDistanceTable();
        System.out.println("\n");
      }
      counter++;
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {

      }
      gv.updateGraph();
    }
    System.out.println("The algorithm converged in " + (counter - 1) + " iterations");
    System.out.println("The final distance tables and forwarding tables of nodes areas follows:");
    for (Node n : nodeList) {
      System.out.println("Node " + n.nodeID);
      n.printDistanceTable();
      n.printForwardingTable();
      System.out.println("\n");
    }
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
    gv = GraphView.getInstance(edgeList, nodeList);
  }
}
