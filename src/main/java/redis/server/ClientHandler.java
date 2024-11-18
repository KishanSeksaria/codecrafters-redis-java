package redis.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
          String echoMessage = message.substring(14, message.length());
          output.write(echoMessage.getBytes());
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
