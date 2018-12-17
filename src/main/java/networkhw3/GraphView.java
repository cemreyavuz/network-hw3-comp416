package networkhw3;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
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

  private GraphView(Hashtable<String, Edge> edgeList, ArrayList<Node> nodeList) {
    this.edgeList = edgeList;
    this.nodeList = nodeList;
    initialize();
  }

  public static synchronized GraphView getInstance(Hashtable<String, Edge> edgeList, ArrayList<Node> nodeList) {
    if(_instance == null) {
      _instance = new GraphView(edgeList, nodeList);
    }
    return _instance;
  }

  private void initialize() {
    // Initializing the JFrame and Graph
    graph = new MultiGraph("Bazinga!");
    graph.addAttribute("ui.stylesheet", "node { size: 50px; fill-color: #dbdbdb; text-size: 30px; } edge { text-size: 30px; }");
    viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
    view = viewer.addDefaultView(false);
    viewer.enableAutoLayout();
    add((Component)view, BorderLayout.CENTER);
    setSize(new Dimension(1400, 720));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // Initializing Nodes and Edges
    initializeNodes();
    initializeEdges();

    initializeRightPanel();
  }

  private void initializeRightPanel() {
    // Adding calculate button
    JPanel jp = new JPanel();
    jp.setPreferredSize(new Dimension(300, 720));

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
    jp.add(b, BorderLayout.EAST);
    jp.add(c1, BorderLayout.EAST);
    jp.add(c2, BorderLayout.EAST);
    add(jp, BorderLayout.EAST);
    jp.setVisible(true);
  }

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

  private void clearPath() {
    for(String str: edgeList.keySet()) {
      Edge e = edgeList.get(str);
      e.addAttribute("ui.style", "fill-color: black; stroke-mode: plain; stroke-width: 1px;");
    }
  }

  private void initializeNodes() {
    for(Node n: nodeList) {
      org.graphstream.graph.Node no = graph.addNode(Integer.toString(n.nodeID));
      no.addAttribute("ui.label", n.nodeID);
    }
  }

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
