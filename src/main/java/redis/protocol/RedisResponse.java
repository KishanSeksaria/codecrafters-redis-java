package redis.protocol;

public class RedisResponse {
  private enum RedisResponseType {
    NULL("$-1\r\n"),
    INTEGER(":%s\r\n"),
    ERROR("-%s\r\n"),
    SIMPLE_STRING("+%s\r\n"),
    BULK_STRING("$%d\r\n%s\r\n"),
    ARRAY("*%d\r\n%s");

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
      if (this == ARRAY) {
        String[] array = (String[]) value;
        return String.format(format, array.length, String.join("", array));
      }
      return String.format(format, value);
    }
  }

  private final Object value;

  public RedisResponse(Object value) {
    this.value = value;
  }

  public RedisResponse(Object[] array) {
    this.value = array;
  }

  @Override
  public String toString() {
    if (value instanceof Object[]) {
      Object[] array = (Object[]) value;
      String[] elements = new String[array.length];
      for (int i = 0; i < array.length; i++) {
        elements[i] = RedisResponseType.BULK_STRING.format(array[i]);
      }
      return RedisResponseType.ARRAY.format(elements);
    }
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