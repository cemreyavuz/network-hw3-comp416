package llamas.utils;

import java.time.Instant;

public class ClassLogger extends Logger {
  private Class<?> cls;

  public ClassLogger(Class <?> cls) {
    this.cls = cls;
  }

  @Override
  protected void print(String logLevel, String msg) {
    System.out.println(String.format("[%s] [%s] [%s] %s",
            Instant.now(),
            cls.getSimpleName(),
            logLevel,
            msg));

  }
}
