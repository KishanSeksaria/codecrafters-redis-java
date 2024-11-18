package redis.protocol;

import redis.commands.Command;

public class RedisParser {
  private static final String CRLF = "\r\n";

  public String[] parse(String input) {
    if (input == null || input.isEmpty()) {
      throw new IllegalArgumentException("Input cannot be null or empty");
    }

    // Simple space-based parsing for now
    // Can be extended to support full RESP protocol
    return input.trim().split("\r\n");
  }

  public Command parseCommand(String input) {
    String[] parts = parse(input);
    String length = parts[0].substring(1);

    if (length == "0") {
      throw new IllegalArgumentException("Invalid command");
    }

    String commandName = parts[2].toLowerCase();
    System.out.println("Command Name: " + commandName);

    return RedisCommand.getHandler(commandName);
  }
}