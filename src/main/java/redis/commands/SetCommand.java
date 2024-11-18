package redis.commands;

import redis.protocol.RedisResponse;
import redis.storage.RedisStore;

public class SetCommand implements Command {
  @Override
  public RedisResponse execute(String[] args) {
    if (args.length < 7) {
      return new RedisResponse("ERR wrong number of arguments for 'set' command");
    }

    String key = args[4];
    String value = args[6];

    String result = RedisStore.getInstance().set(key, value);
    return new RedisResponse(result);
  }
}