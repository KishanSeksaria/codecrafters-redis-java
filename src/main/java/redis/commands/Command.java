package redis.commands;

import redis.protocol.RedisResponse;

public interface Command {
  RedisResponse execute(String[] args);
}