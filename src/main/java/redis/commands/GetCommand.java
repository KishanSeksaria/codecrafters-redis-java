package redis.commands;

import redis.protocol.RedisResponse;
import redis.storage.RedisStore;

public class GetCommand implements Command {
  @Override
  public RedisResponse execute(String[] args) {
    if (args.length < 5) {
      return new RedisResponse("ERR wrong number of arguments for 'get' command");
    }

    String key = args[4];
    String value = RedisStore.getInstance().get(key);
    return new RedisResponse(value);
  }
}