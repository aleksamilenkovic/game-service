package com.mozzartbet.gameservice.util;

public abstract class ConvertHelper {
  public static boolean tryParseInt(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static String returnPlayerId(String link) {
    String id = null;
    id = link.substring(link.length() - 16, link.length() - 5);
    return id;
  }

}
