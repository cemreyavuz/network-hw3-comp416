package networkhw3;

import networkhw3.utils.input.Input;
import networkhw3.utils.logging.Logger;
import networkhw3.utils.logging.LoggerFactory;

import java.io.IOException;

public class RouteSim {
  private Input input = Input.getInstance();
  private Logger logger = LoggerFactory.getInstance().getLogger(getClass());

  public RouteSim() {

  }

  public void run() throws IOException {
    logger.i("RouteSim is working!");
    logger.i("RouteSim started to read the input file.");
    System.out.println(input.readFile());
  }
}
