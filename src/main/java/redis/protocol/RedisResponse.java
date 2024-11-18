package redis.protocol;

public class RedisResponse {
  private enum RedisResponseType {
    NULL("$-1\r\n"),
    INTEGER(":%s\r\n"),
    ERROR("-%s\r\n"),
    SIMPLE_STRING("+%s\r\n"),
    BULK_STRING("$%d\r\n%s\r\n");

    private final String format;

    RedisResponseType(String format) {
      this.format = format;
    }

    String format(Object value) {
      if (this == NULL)
        return format;
      if (this == BULK_STRING) {
        String str = value.toString();
        return String.format(format, str.length(), str);
      }
      return String.format(format, value);
    }
  }

  private final Object value;

  public RedisResponse(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    if (value == null) {
      return RedisResponseType.NULL.format(null);
    }
    if (value instanceof Long) {
      return RedisResponseType.INTEGER.format(value);
    }
    if (value instanceof String) {
      String str = (String) value;
      if (str.startsWith("ERR")) {
        return RedisResponseType.ERROR.format(str);
      }
      return RedisResponseType.SIMPLE_STRING.format(str);
    }
    return RedisResponseType.BULK_STRING.format(value);
  }
}