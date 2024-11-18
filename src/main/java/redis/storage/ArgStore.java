package redis.storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ArgStore {
  private static volatile ArgStore instance;
  private final Map<String, String> args = new ConcurrentHashMap<>();

  private ArgStore() {
  } // private constructor

  public static ArgStore getInstance() {
    if (instance == null) {
      synchronized (ArgStore.class) {
        if (instance == null) {
          instance = new ArgStore();
        }
      }
    }
    return instance;
  }

  public void setArg(String flag, String value) {
    if (flag.startsWith("--")) {
      flag = flag.substring(2); // Remove -- prefix
    }
    args.put(flag, value);
  }

  public String getArg(String flag) {
    return args.get(flag);
  }

  public boolean hasArg(String flag) {
    return args.containsKey(flag);
  }

  // For testing purposes
  public static void reset() {
    instance = null;
  }
}