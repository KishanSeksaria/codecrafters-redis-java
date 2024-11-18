package redis.protocol;

import redis.commands.Command;
import redis.commands.EchoCommand;
import redis.commands.GetCommand;
import redis.commands.PingCommand;
import redis.commands.SetCommand;

public enum RedisCommand {
  PING("ping", new PingCommand()),
  ECHO("echo", new EchoCommand()),
  GET("get", new GetCommand()),
  SET("set", new SetCommand());

  private final String name;
  private final Command handler;

  RedisCommand(String name, Command handler) {
    this.name = name;
    this.handler = handler;
  }

  public static Command getHandler(String commandName) {
    for (RedisCommand cmd : values()) {
      if (cmd.name.equalsIgnoreCase(commandName)) {
        return cmd.handler;
      }
    }
    throw new IllegalArgumentException("Unknown command: " + commandName);
  }
}