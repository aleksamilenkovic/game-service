package com.gameservice.util;

import org.jsoup.select.Elements;

import com.gameservice.domain.Match;
import com.gameservice.domain.MatchEventType;
import com.gameservice.domain.Player;
import com.gameservice.domain.Team;

public class MatchEventHelper {

	// Ovoj metodi ce biti prosledjena akcija kao string i proverice koji igraci su
	// ucestvovali u
	// akciji i sta su uradili.
	public static MatchEventType returnActionType(String action, boolean pointsMade, float time) {
		MatchEventType type = MatchEventType.NEUTRAL;
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
			 * else if (action.contains("enters")) { type[0] = new
			 * EntersOrLeft(playersId[0], time); type[1] = new EntersOrLeft(playersId[1],
			 * time); }
			 */
		return type;
	}

	public static Player[] returnPlayers(Elements playersLink, Match match) {
		Player[] players = new Player[2];
		String ids[] = new String[2];
		if (playersLink == null || playersLink.isEmpty())
			return null;
		ids[0] = ConvertHelper.returnPlayerId(playersLink.get(0).attr("href"));
		ids[1] = playersLink.size() == 2 ? ConvertHelper.returnPlayerId(playersLink.get(1).attr("href")) : null;
		Team firstPlayerTeam = match.getAwayPlayersID().contains(ids[0]) ? match.getAwayTeam()
				: match.getHomePlayersID().contains(ids[0]) ? match.getHomeTeam() : null,
				secondPlayerTeam = match.getAwayPlayersID().contains(ids[1]) ? match.getAwayTeam()
						: match.getHomePlayersID().contains(ids[1]) ? match.getHomeTeam() : null;
		players[0] = firstPlayerTeam != null ? Player.builder().playerId(ids[0]).team(firstPlayerTeam).build() : null;
		players[1] = ids[1] == null ? null
				: secondPlayerTeam != null ? Player.builder().playerId(ids[1]).team(secondPlayerTeam).build() : null;
		return players;
	}
}
