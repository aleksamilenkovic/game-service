package com.mozzartbet.gameservice.parser.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.parser.MatchParserBasketballRef;

public class MonthMatchParserThreads {
  class CallableThread implements Callable<List<Match>> {
    private final int year, start, end;
    private final Elements rows;

    public CallableThread(int year, Elements rows, int start, int end) {
      this.year = year;
      this.rows = rows;
      this.start = start;
      this.end = end;
    }

    @Override
    public List<Match> call() throws Exception {
      MatchParserBasketballRef matchParser = new MatchParserBasketballRef();
      List<Match> matches = new ArrayList<Match>();
      for (int i = start; i < end; i++) {
        Element row = rows.get(i);
        Elements cols = row.select("td");
        if (cols.isEmpty() || cols.get(cols.size() - 4).text().isEmpty())
          continue;

        String matchId = cols.get(cols.size() - 4).select("a").attr("href");
        matchId = matchId.substring(11, matchId.length() - 5);
        matches.add(matchParser.returnMatch(matchId, null, year));
      }
      return matches;
    }
  }

  public List<Match> returnAllMonthMatches(int year, Document doc) {
    Elements rows = doc.select("table#schedule tbody").first().select("tr");

    List<Match> allMatches = new ArrayList<Match>();
    // Get ExecutorService from Executors utility class, thread pool size is 9
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    // create a list to hold the Future object associated with Callable
    // List<Future<List<Match>>> list = new ArrayList<Future<List<Match>>>();
    // Create MyCallable instance
    List<Callable<List<Match>>> taskList = new ArrayList<Callable<List<Match>>>();
    int start = 0, end = rows.size() / 3;
    for (int i = 1; i <= 3; i++) {
      Callable<List<Match>> callable = new CallableThread(year, rows, start, end);
      int a = start;
      start = end;
      end = end * 2 - a;
      if (i == 2)
        if (rows.size() % 2 != 0)
          end += 2;

      // submit Callable tasks to be executed by thread pool
      // Future<List<Match>> future = executor.submit(callable);

      // add Future to the list, we can get return value using Future
      taskList.add(callable);
    }
    List<Future<List<Match>>> futures = null;
    try {
      futures = executorService.invokeAll(taskList);
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    for (Future<List<Match>> monthMatches : futures) {
      try {
        // print the return value of Future, notice the output delay in console
        // because Future.get() waits for task to get completed
        List<Match> listOfMonthMatches = monthMatches.get();
        allMatches.addAll(listOfMonthMatches);
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    // shut down the executor service now
    executorService.shutdown();
    return allMatches;
  }
}
