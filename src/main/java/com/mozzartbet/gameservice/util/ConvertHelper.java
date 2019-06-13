package com.mozzartbet.gameservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class ConvertHelper {
  public static int tryParseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      System.out.println("Can't parse " + value + " to integer...");
      return 0;
    }
  }

  public static String returnPlayerId(String link) {
    return link.length()>20 ? link.substring(link.length() - 16, link.length() - 5) : null;
  }
  public static String returnTeamId(String link,boolean local) {
    return local?(link.substring(0, 3) + "/" + link.substring(4, link.length())).substring(5, link.length())
        :link.substring(link.length() - 13, link.length() - 5);
  }

  public static double roundDecimal(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }


}
