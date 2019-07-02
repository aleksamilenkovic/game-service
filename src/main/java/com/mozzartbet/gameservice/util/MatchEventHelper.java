package com.mozzartbet.gameservice.util;

import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Player;

public class MatchEventHelper {

  // Ovoj metodi ce biti prosledjena akcija kao string i proverice koji igraci su ucestvovali u
  // akciji i sta su uradili.
  public static MatchEventType returnActionType(String action, boolean pointsMade, float time) {
    MatchEventType type = null;
    if (action.contains("2-pt")) {
      type = MatchEventType.SHOOTFOR2;
    } else if (action.contains("3-pt")) {
      type = MatchEventType.SHOOTFOR3;
    } else if (action.contains("free throw")) {
      type = MatchEventType.FREETHROW;
    } else if (action.contains("rebound")) {
      type = MatchEventType.REBOUND;
      // ako je ofanzivni skok salji mu true, u suprotnom false
    } else if (action.contains("Turnover")) {
      type = MatchEventType.TURNOVER;
    } else if (action.contains("foul")) {
      if (!action.contains("Technical"))
        type = MatchEventType.FOUL;
    } /*
       * else if (action.contains("enters")) { type[0] = new EntersOrLeft(playersId[0], time);
       * type[1] = new EntersOrLeft(playersId[1], time); }
       */
    return type;
  }

  public static Player[] returnPlayers(Elements playersLink, Match match) {
    Player[] players = new Player[2];
    String ids[] = new String[2];
    if (playersLink == null || playersLink.isEmpty())
      return null;
    ids[0] = ConvertHelper.returnPlayerId(playersLink.get(0).attr("href"));
    ids[1] = playersLink.size() == 2 ? ConvertHelper.returnPlayerId(playersLink.get(1).attr("href"))
        : null;
    players[0] = Player.builder().playerId(ids[0])
        .team(match.getAwayPlayersID().contains(ids[0]) ? match.getAwayTeam() : match.getHomeTeam())
        .build();
    players[1] = ids[1] == null ? null
        : Player.builder().playerId(ids[1]).team(
            match.getAwayPlayersID().contains(ids[1]) ? match.getAwayTeam() : match.getHomeTeam())
            .build();
    return players;
  }
}
