package com.mozzartbet.gameservice.exception;

public class UrlException extends Exception {
  String url;

  public UrlException(String url) {
    this.url = url;
  }

  public String toString() {
    return "[UrlException (customMade)] *** wrong url : " + this.url;
  }
}
