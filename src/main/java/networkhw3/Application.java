package networkhw3;

public class Application {

  private RouteSim rSim;

  public Application() {
    rSim = new RouteSim();
  }

  public static void main(String[] args) {
    Application app = new Application();
    app.run();
  }

  public void run() {
    System.out.println("===========================");
    System.out.println("=== Welcome to RouteSim ===");
    System.out.println("===========================");
    this.runRouteSim();
  }

  public void runRouteSim() {
    rSim.run();
  }
}
