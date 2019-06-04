package com.mozzartbet.gameservice.util;

import java.io.File;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.mozzartbet.gameservice.exception.UrlException;

public class JsoupHelper {
  public static Document connectToLivePage(String pageUrl) throws UrlException {
    URL url;
    Document doc = null;
    try {
      url = new URL(pageUrl);
      doc = Jsoup.parse(url, 3000);
    } catch (Exception e) {
      throw new UrlException(pageUrl);
    }
    return doc;
  }

  public static Document connectToLocalPage(String fileName) throws UrlException {
    Document doc = null;
    try {

      String filePath = "src/test/resources/" + fileName + ".html";
      doc = Jsoup.parse(new File(filePath), "utf-8");
      if (doc == null)
        throw new UrlException(fileName);
    } catch (Exception e) {
      throw new UrlException(fileName);
    }
    return doc;
  }


}
