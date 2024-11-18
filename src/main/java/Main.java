import redis.server.RedisServer;
import redis.storage.ArgStore;

public class Main {
  public static void main(String[] args) {
    // Initialize ArgStore first
    ArgStore argStore = ArgStore.getInstance();
    for (int i = 0; i < args.length - 1; i++) {
      if (args[i].startsWith("--")) {
        argStore.setArg(args[i], args[i + 1]);
        i++; // Skip next arg since we used it as value
      }
    }

    int port = 6379;
    int threadPoolSize = 10;

    // Create and start the server
    RedisServer server = new RedisServer(port, threadPoolSize);
    server.start();
  }
}