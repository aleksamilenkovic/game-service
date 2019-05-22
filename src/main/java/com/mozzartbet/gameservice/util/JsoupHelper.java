package com.mozzartbet.gameservice.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupHelper {
  public static Document connectToPage(String pageUrl) {
    URL url;
    Document doc = null;
    try {
      url = new URL(pageUrl);
      doc = Jsoup.parse(url, 3000);
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
