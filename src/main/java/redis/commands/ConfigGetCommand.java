package redis.commands;

import redis.protocol.RedisResponse;
import redis.storage.ArgStore;

public class ConfigGetCommand implements Command {

  @Override
  public RedisResponse execute(String[] args) {
    if (args.length != 7) {
      throw new IllegalArgumentException("Invalid number of arguments");
    }

    String flag = args[6];
    String value = ArgStore.getInstance().getArg(flag);

    if (value == null) {
      return new RedisResponse(null);
    }

    return new RedisResponse(new String[] { flag, value });
  }

}
