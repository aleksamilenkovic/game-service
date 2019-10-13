package com.gameservice.parser.threads;

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

import com.gameservice.domain.Match;
import com.gameservice.parser.MatchParserBasketballRef;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonthMatchParserThreads {
	class CallableThread implements Callable<List<Match>> {
		private final int start, end;
		private final Elements rows;

		public CallableThread(Elements rows, int start, int end) {
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
				matches.add(matchParser.returnMatch(matchId, null));
				log.info(matchId);
			}
			return matches;
		}
	}

	public List<Match> returnAllMonthMatches(Document doc) {
		Elements rows = doc.select("table#schedule tbody").first().select("tr");

		List<Match> allMatches = new ArrayList<Match>();
		// Get ExecutorService from Executors utility class, thread pool size is 2
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		// create a list to hold the Future object associated with Callable
		// List<Future<List<Match>>> list = new ArrayList<Future<List<Match>>>();
		// Create MyCallable instance
		List<Callable<List<Match>>> taskList = new ArrayList<Callable<List<Match>>>();
		int start = 0, end = rows.size() / 3;
		Callable<List<Match>> callable1 = new CallableThread(rows, start, end);
		start = end;
		end *= 2;
		Callable<List<Match>> callable2 = new CallableThread(rows, start, end);
		start = end;
		end = rows.size();
		Callable<List<Match>> callable3 = new CallableThread(rows, start, end);

		taskList.add(callable1);
		taskList.add(callable2);
		taskList.add(callable3);
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
