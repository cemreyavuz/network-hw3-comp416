package networkhw3.utils.logging;

public abstract class Logger {
  protected abstract void print(String logLevel, String message);

  public void d(String msg) {
    print("DEBUG", msg);
  }

  public void i(String msg) {
    print("INFO", msg);
  }

  public void e(String msg) {
    print("EXCEPTION", msg);
  }
}
