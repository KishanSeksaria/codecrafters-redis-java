package redis.commands;

import redis.protocol.RedisResponse;
import redis.storage.RedisStore;

public class SetCommand implements Command {
  @Override
  public RedisResponse execute(String[] args) {
    String result = "";

    if (args.length < 7) {
      result = "ERR wrong number of arguments for 'set' command";
    }

    String key = args[4];
    String value = args[6];

    if (args.length > 7) {
      String px = args[8];
      if (px.equalsIgnoreCase("px")) {
        long milliseconds = Long.parseLong(args[10]);
        result = RedisStore.getInstance().set(key, value, milliseconds);
      }
    } else {
      result = RedisStore.getInstance().set(key, value);
    }

    return new RedisResponse(result);
  }
}