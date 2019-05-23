package com.mozzartbet.gameservice.parser;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.util.JsoupHelper;

public class JSoupMatchParser {

  public Match returnMatch(String pageUrl) {
    Match match = null;
    Document doc = JsoupHelper.connectToPage(pageUrl);
    if (doc == null)
      return match;

    Elements rows = doc.select("table#pbp tr");
    for (int i = 0; i < rows.size(); i++) {
      Elements cols = rows.get(i).select("td");
      for (int j = 0; j < cols.size(); j++)
        System.out.print(cols.get(j).text() + " ");
      System.out.println("");
    }

    return match;

  }
}
