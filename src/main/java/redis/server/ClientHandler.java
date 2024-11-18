package redis.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import redis.protocol.RedisParser;
import redis.protocol.RedisResponse;
import redis.commands.Command;

public class ClientHandler implements Runnable {
  private Socket clientSocket;
  private final RedisParser parser;

  public ClientHandler(Socket socket, RedisParser parser) {
    this.clientSocket = socket;
    this.parser = parser;
  }

  @Override
  public void run() {
    try (InputStream input = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream()) {

      byte[] buffer = new byte[1024];
      int bytesRead;

      while ((bytesRead = input.read(buffer)) != -1) {
        try {
          String message = new String(buffer, 0, bytesRead);
          System.out.println("Received: " + message);

          // Parse command and get handler
          Command command = parser.parseCommand(message);
          String[] args = parser.parse(message);
          System.out.println("Command: " + command + ", Args: " + String.join(", ", args));

          // Execute command and get response
          RedisResponse response = command.execute(args);
          System.out.println("Response: " + response);

          // Write formatted response
          output.write(response.toString().getBytes());
          output.flush();
        } catch (IllegalArgumentException e) {
          // Handle invalid commands
          RedisResponse errorResponse = new RedisResponse("ERR " + e.getMessage());
          output.write(errorResponse.toString().getBytes());
          output.flush();
        }
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
