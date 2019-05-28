package com.mozzartbet.gameservice.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

  public static Document connectToLocalPage(String fileName) {
    Document doc = null;
    try {

      String filePath = "src/test/resources/" + fileName + ".html";
      doc = Jsoup.parse(new File(filePath), "utf-8");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return doc;
    } catch (IOException e) {
      e.printStackTrace();
      return doc;
    }
    return doc;
  }


}
