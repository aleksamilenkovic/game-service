package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;

public class Quarter {
  private String quarterName;
  private LinkedList<MatchEvent> matches;

  public Quarter(String qname, LinkedList<MatchEvent> listOfMatches) {
    quarterName = qname;
    matches = listOfMatches;
  }

  public String getQuarterName() {
    return quarterName;
  }

  public void setQuarterName(String quarterName) {
    this.quarterName = quarterName;
  }

  public LinkedList<MatchEvent> getMatches() {
    return matches;
  }

  public void setMatches(LinkedList<MatchEvent> matches) {
    this.matches = matches;
  }
}
