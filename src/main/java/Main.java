import redis.server.RedisServer;

public class Main {
  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");

    int port = 6379;
    int threadPoolSize = 10;

    // Create and start the server
    RedisServer server = new RedisServer(port, threadPoolSize);
    server.start();
  }
}