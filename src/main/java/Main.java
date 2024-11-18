import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");

    ServerSocket serverSocket = null;
    int port = 6379;
    ExecutorService threadPool = Executors.newFixedThreadPool(10); // Create a thread pool with 10 threads

    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server is listening on port " + port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New client connected");

        // Submit the client handling task to the thread pool
        threadPool.submit(new ClientHandler(clientSocket));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (serverSocket != null) {
        try {
          serverSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      threadPool.shutdown(); // Shutdown the thread pool
    }
  }
}

class ClientHandler implements Runnable {
  private Socket clientSocket;

  public ClientHandler(Socket socket) {
    this.clientSocket = socket;
  }

  @Override
  public void run() {
    try (InputStream input = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream()) {
      // Handle client request here
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = input.read(buffer)) != -1) {
        String message = new String(buffer, 0, bytesRead);
        if (message.contains("PING")) {
          output.write("+PONG\r\n".getBytes());
        } else if (message.contains("ECHO")) {
          String echoMessage = message.substring(5, message.length() - 2);
          output.write(("$" + echoMessage.length() + "\r\n" + echoMessage + "\r\n").getBytes());
        } else {
          output.write(":ERR - No such command\r\n".getBytes());
        }
        output.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
