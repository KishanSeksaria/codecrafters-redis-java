package redis.commands;

import redis.protocol.RedisResponse;

public class EchoCommand implements Command {
  @Override
  public RedisResponse execute(String[] args) {
    if (args.length < 5) {
      return new RedisResponse("ERR wrong number of arguments for 'echo' command");
    }
    return new RedisResponse(args[args.length - 1]);
  }
}