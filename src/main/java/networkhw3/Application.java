package networkhw3;

import java.io.IOException;

public class Application {

  private RouteSim rSim;

  public Application() {
    rSim = new RouteSim();
  }

  public static void main(String[] args) throws IOException {
    Application app = new Application();
    app.run();
  }

  public void run() throws IOException {
    System.out.println("===========================");
    System.out.println("=== Welcome to RouteSim ===");
    System.out.println("===========================");
    this.runRouteSim();
  }

  public void runRouteSim() throws IOException {
    rSim.run();
  }
}
