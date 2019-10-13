package com.gameservice.service;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.gameservice.domain.Player;
import com.gameservice.mapper.setups.PlayerSetup;
import com.gameservice.service.PlayerService.LockType;
import com.gameservice.service.dto.PlayerSearchRequest;
import com.gameservice.service.dto.PlayerSearchResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PlayerServiceLatchLockTest extends BaseSpringTest {

	@Autowired
	private PlayerSetup playerSetup;

	@Autowired
	private PlayerService playerService;

	@Test
	@Commit
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = {
			"delete from basketball_seasons where season_year=1910" })
	public void testTwoThreads() throws InterruptedException {
		playerSetup.toplicanin1910();

		ExecutorService pool = Executors.newFixedThreadPool(2);
		PlayerSaveTask1 pt1 = new PlayerSaveTask1();
		PlayerSaveTask2 pt2 = new PlayerSaveTask2();
		pool.invokeAll(asList(pt2, pt1));

		assertThat(pt1.p.getName(), equalTo("Jovan Djokovic 1"));
		assertThat(pt2.caught, is(instanceOf(OptimisticLockingFailureException.class)));

		Player pp = playerService.getPlayer(pt1.p.getId());
		assertThat(pp.getName(), equalTo("Jovan Djokovic 1"));
	}

	final CountDownLatch latch = new CountDownLatch(1);

	class PlayerSaveTask1 implements Callable<Player> {
		Player p;
		PlayerSearchResponse psr;

		@Override
		public Player call() throws Exception {
			psr = playerService
					.searchPlayers(PlayerSearchRequest.builder().playerName("Jovan").teamId("TOP/1910").build());
			p = psr.getPlayers().get(0);
			p.setName(p.getName() + " 1");

			p = playerService.save(p, LockType.OPTIMISTIC);
			latch.countDown();

			return p;
		}
	}

	class PlayerSaveTask2 implements Callable<Player> {
		Player p;
		Exception caught;
		PlayerSearchResponse psr;

		@Override
		public Player call() throws Exception {
			psr = playerService
					.searchPlayers(PlayerSearchRequest.builder().playerName("Jovan").teamId("TOP/1910").build());
			p = psr.getPlayers().get(0);
			p.setName(p.getName() + " 2");

			try {
				latch.await();
				p = playerService.save(p, LockType.OPTIMISTIC);
			} catch (OptimisticLockingFailureException e) {
				caught = e;
			}
			return p;
		}
	}
}
