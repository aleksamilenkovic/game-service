package com.gameservice.parser.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.gameservice.domain.Match;
import com.gameservice.parser.MatchParserBasketballRef;

public class SeasonMatchParserThreads {
	class CallableThread implements Callable<List<Match>> {
		private final int year;
		private final String month;

		public CallableThread(int year, String month) {
			this.year = year;
			this.month = month;
		}

		@Override
		public List<Match> call() throws Exception {
			MatchParserBasketballRef matchParser = new MatchParserBasketballRef();
			return matchParser.returnMatchesFromMonth(year, month);
		}
	}

	public List<Match> returnAllMatchesFromMonth(int year, String[] monthsArray) {
		List<Match> allMatches = new ArrayList<Match>();
		// Get ExecutorService from Executors utility class, thread pool size is 9
		ExecutorService executorService = Executors.newFixedThreadPool(7);
		// create a list to hold the Future object associated with Callable
		// List<Future<List<Match>>> list = new ArrayList<Future<List<Match>>>();
		// Create MyCallable instance
		List<Callable<List<Match>>> taskList = new ArrayList<Callable<List<Match>>>();
		for (int i = 0; i < monthsArray.length; i++) {
			Callable<List<Match>> callable = new CallableThread(year, monthsArray[i]);
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
