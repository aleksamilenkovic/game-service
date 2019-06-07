package com.mozzartbet.gameservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.ActionType;
import com.mozzartbet.gameservice.domain.actiontype.EntersOrLeft;
import com.mozzartbet.gameservice.domain.actiontype.FGAttempt;
import com.mozzartbet.gameservice.domain.actiontype.FreeThrow;
import com.mozzartbet.gameservice.domain.actiontype.OtherType;
import com.mozzartbet.gameservice.domain.actiontype.Rebound;
import com.mozzartbet.gameservice.domain.actiontype.ReboundType;

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
    return link != null ? link.substring(link.length() - 16, link.length() - 5) : null;
  }

  public static double roundDecimal(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  // Ovoj metodi ce biti prosledjena akcija kao string i proverice koji igraci su ucestvovali u
  // akciji i sta su uradili.
  public static ActionType[] returnActionType(String action, boolean pointsMade,
      Elements playersLink, float time) {
    ActionType[] type = null;
    String[] playersId;
    if (playersLink.isEmpty())
      return null;
    String firstPlayerId = playersLink.get(0).attr("href");
    firstPlayerId = returnPlayerId(firstPlayerId);
    if (playersLink.size() == 2) {
      playersId = new String[2];
      type = new ActionType[2];
      playersId[0] = firstPlayerId;
      playersId[1] = playersLink.get(1).attr("href");
      playersId[1] = returnPlayerId(playersId[1]);
    } else if (playersLink.size() == 1) {
      type = new ActionType[1];
      playersId = new String[1];
      playersId[0] = firstPlayerId;
    } else
      return null;

    if (action.contains("2-pt")) {
      if (pointsMade) {
        if (type.length == 2)
          // ako ima dva igraca i pogodak je onda je sigurno drugi igrac zaradio asistenciju
          type[1] = new ActionType(playersId[1], OtherType.ASSIST);
        // ovim oznacavamo da je nije promasen i da nije sut za 3 poena vec 2
        type[0] = new FGAttempt(false, false, playersId[0]);
      } else {// ako je promasen sut za 2
        if (type.length == 2) // ako ima dva igraca onda je sigurno drugi blokirao, a prvi promasio
          type[1] = new ActionType(playersId[1], OtherType.BLOCK);
        type[0] = new FGAttempt(true, false, playersId[0]);
      }
    } else if (action.contains("3-pt")) {
      if (pointsMade) {
        if (type.length == 2)
          type[1] = new ActionType(playersId[1], OtherType.ASSIST);
        type[0] = new FGAttempt(false, true, playersId[0]);
      } else {
        if (type.length == 2)
          type[1] = new ActionType(playersId[1], OtherType.BLOCK);
        type[0] = new FGAttempt(true, true, playersId[0]);
      }
    } else if (action.contains("rebound")) {
      if (action.contains("Defensive"))
        type[0] = new Rebound(ReboundType.DEFENSIVE, playersId[0]);
      else if (action.contains("Offensive"))
        type[0] = new Rebound(ReboundType.OFFENSIVE, playersId[0]);

    } else if (action.contains("free throw")) {
      if (pointsMade)
        type[0] = new FreeThrow(false, playersId[0]);
      else
        type[0] = new FreeThrow(true, playersId[0]);
    } else if (action.contains("Turnover")) {
      if (type.length == 2)
        type[1] = new ActionType(playersId[1], OtherType.STEAL);
      type[0] = new ActionType(playersId[0], OtherType.TURNOVER);
    } else if (action.contains("foul")) {
      type[0] = new ActionType(playersId[0], OtherType.FOUL);
    } else if (action.contains("enters")) {
      type[0] = new EntersOrLeft(playersId[0], time);
      type[1] = new EntersOrLeft(playersId[1], time);
    }

    return type;
  }

}
