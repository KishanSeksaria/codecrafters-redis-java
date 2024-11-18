package redis.storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class RedisStore {
  private static volatile RedisStore instance;
  private final Map<String, String> store = new ConcurrentHashMap<>();

  private RedisStore() {
  } // private constructor

  public static RedisStore getInstance() {
    if (instance == null) {
      synchronized (RedisStore.class) {
        if (instance == null) {
          instance = new RedisStore();
        }
      }
    }
    return instance;
  }

  public String set(String key, String value) {
    store.put(key, value);
    return "OK";
  }

  public String get(String key) {
    return store.get(key);
  }

  // For testing purposes
  public static void reset() {
    instance = null;
  }
}