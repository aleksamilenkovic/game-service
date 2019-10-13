package com.gameservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import org.jsoup.nodes.Element;

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
		return link.length() > 32 ? link.substring(45, link.length() - 5) : link.substring(9, link.length() - 5);
	}

	public static String returnTeamId(String link, boolean local) {
		return local ? (link.substring(5, 8) + "/" + link.substring(8, link.length()))
				: link.substring(link.length() - 13, link.length() - 5);
	}

	public static String returnTeamAttrRefId(Element el) {
		String href = el.select("a").attr("href");
		return href.substring(7, href.length() - 5);
	}

	public static double roundDecimal(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static LocalDateTime convertStringToLocalDate(String date) {
		return LocalDateTime.parse(date,
				new DateTimeFormatterBuilder().appendPattern("[uuuuMMddHHmmss][uuuuMMdd]")
						.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter());
	}

	public static LocalDateTime convertStringWithFullMonthToLocalDate(String dt) {
		return LocalDateTime.parse(dt,
				new DateTimeFormatterBuilder().appendPattern("h:mm a, MMMM d, uuuu").toFormatter());
	}
}
