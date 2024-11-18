package redis.commands;

import redis.protocol.RedisResponse;

public class PingCommand implements Command {
  @Override
  public RedisResponse execute(String[] args) {
    if (args.length > 3) {
      return new RedisResponse("ERR wrong number of arguments for 'ping' command");
    }
    return new RedisResponse("PONG");
  }
}