package networkhw3;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

public class GraphView extends JFrame {

  private static volatile GraphView _instance;
  private Graph graph;
  private Viewer viewer;
  private View view;
  private Hashtable<String, Edge> edgeList;
  private ArrayList<Node> nodeList;
  private JComboBox c1;
  private JComboBox c2;
  private JPanel left;
  private JPanel right;

  /**
   * Private constructor of Singleton GraphView class.
   * @param edgeList
   * @param nodeList
   */
  private GraphView(Hashtable<String, Edge> edgeList, ArrayList<Node> nodeList) {
    this.edgeList = edgeList;
    this.nodeList = nodeList;
    initialize();
  }

  /**
   * getInstance method for Singleton GraphView class.
   * @param edgeList Hashtable with the key-value pairs of String-Edge
   * @param nodeList ArrayList which keeps the list of nodes.
   * @return a Singleton GraphView object
   */
  public static synchronized GraphView getInstance(Hashtable<String, Edge> edgeList, ArrayList<Node> nodeList) {
    if(_instance == null) {
      _instance = new GraphView(edgeList, nodeList);
    }
    return _instance;
  }

  /**
   * This function initializes the necessary GUI components.
   */
  private void initialize() {
    // Initializing panels
    left = new JPanel();
    left.setPreferredSize(new Dimension(990, 680));
    left.setBorder(BorderFactory.createEtchedBorder());
    left.setBackground(Color.white);
    this.right = new JPanel();
    this.right.setPreferredSize(new Dimension(270, 680));
    this.right.setBorder(BorderFactory.createEtchedBorder());

    // Preparing nodes array for combo boxes
    String[] nodes = new String[nodeList.size()];
    int counter = 0;
    for(Node n: nodeList) {
      nodes[counter] = Integer.toString(n.nodeID);
      counter++;
    }

    // Initializing button
    JButton b = new JButton("Calculate");
    b.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        visualizePath();
      }
    });

    // Initializing combo boxes
    c1 = new JComboBox(nodes);
    c2 = new JComboBox(nodes);

    // Adding components to panel
    right.add(b, BorderLayout.EAST);
    right.add(c1, BorderLayout.EAST);
    right.add(c2, BorderLayout.EAST);

    // Adding panels to frame
    add(left, BorderLayout.WEST);
    add(right, BorderLayout.EAST);

    // Initializing the JFrame and Graph
    graph = new MultiGraph("Bazinga!");
    graph.addAttribute("ui.stylesheet", "node { size: 50px; fill-color: #dbdbdb; text-size: 30px; } edge { text-size: 30px; }");
    viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
    view = viewer.addDefaultView(false);
    ((ViewPanel) view).setPreferredSize(new Dimension(980, 670));
    viewer.enableAutoLayout();
    left.add((Component)view);
    setSize(new Dimension(1280, 720));
    setResizable(false);
    setTitle("RouteSim");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // Functions calls for initializing Nodes and Edges
    initializeNodes();
    initializeEdges();
  }

  /**
   * This function visualizes the path selected in combo boxes.
   * It starts with two nodes (source and destination) and iterates
   * in while loop until source and destination nodes are the same nodes.
   * In each iteration in while loop, source node is changed with new node
   * which returns from the source node's forwarding table's entry for
   * destination node.
   */
  private void visualizePath() {
    clearPath();
    int source = c1.getSelectedIndex();
    int dest = c2.getSelectedIndex();
    Node s = nodeList.get(source);
    Node d = nodeList.get(dest);
    int oldIndex = source;
    while(s != d) {
      oldIndex = s.nodeID;
      s = nodeList.get(s.getForwardingTable().get(dest));
      String str = oldIndex + "-" + s.nodeID;
      // System.out.println(str);
      if(!edgeList.keySet().contains(str)) {
        Edge e = edgeList.get(s.nodeID + "-" + oldIndex);
        e.addAttribute("ui.style", "fill-color: red; stroke-mode: plain; stroke-width: 3px; ");
      }
      else {
        Edge e = edgeList.get(oldIndex + "-" + s.nodeID);
        e.addAttribute("ui.style", "fill-color: red; stroke-mode: plain; stroke-width: 3px; ");
      }
    }
  }

  /**
   * This function clear the previous indicated path on graph.
   * It iterates through 'edgeList' and clear all the paths.
   */
  private void clearPath() {
    for(String str: edgeList.keySet()) {
      Edge e = edgeList.get(str);
      e.addAttribute("ui.style", "fill-color: black; stroke-mode: plain; stroke-width: 1px;");
    }
  }

  /**
   * This function initializes the nodes on the graph.
   * It iterates through 'nodeList' and creates a new node
   * on the graph with node's nodeID for each node in the list.
   */
  private void initializeNodes() {
    for(Node n: nodeList) {
      org.graphstream.graph.Node no = graph.addNode(Integer.toString(n.nodeID));
      no.addAttribute("ui.label", n.nodeID);
    }
  }

  /**
   * This function initialize the edges on the graph.
   * It iterates through each node in the 'nodeList' and
   * checks whether there exists a edge between nodes' and
   * their neighbors. If there is, it continues the loop,
   * otherwise, it creates a new edge on graph and adds those
   * new edges to 'edgeList'.
   */
  private void initializeEdges() {
    for(Node n: nodeList) {
      int nodeID = n.nodeID;
      for(int i: n.linkCost.keySet()) {
        String str = i + "-" + nodeID;
        if(!edgeList.keySet().contains(str)) {
          Edge e = graph.addEdge(nodeID + "-" + i, nodeID, i);
          e.addAttribute("ui.label", n.linkCost.get(i));
          e.addAttribute("ui.style", "fill-color: black; stroke-mode: plain; stroke-width: 1px;");
          edgeList.put(nodeID + "-" + i, e);
        }
      }
    }
  }

  /**
   * This function updates the graph.
   * It iterates through each node in the 'nodeList' and
   * updates those nodes' link costs to their neighbors.
   */
  public void updateGraph() {
    for(Node n: nodeList) {
      int nodeID = n.nodeID;
      for(int i: n.linkCost.keySet()) {
        String str = i + "-" + nodeID;
        if(!edgeList.keySet().contains(str)) {
          Edge e = edgeList.get(nodeID + "-" + i);
          e.addAttribute("ui.label", n.linkCost.get(i));
        }
      }
    }
  }
}
