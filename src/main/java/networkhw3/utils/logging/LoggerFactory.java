package networkhw3.utils.logging;

public class LoggerFactory {
  private static volatile LoggerFactory _instance = null;

  private LoggerFactory() {

  }

  public static synchronized LoggerFactory getInstance() {
    if(_instance == null) {
      _instance = new LoggerFactory();
    }
    return _instance;
  }

  public Logger getLogger(Class<?> cls) {
    return new ClassLogger(cls);
  }

}
