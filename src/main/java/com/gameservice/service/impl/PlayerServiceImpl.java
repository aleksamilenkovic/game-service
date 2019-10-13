package com.gameservice.service.impl;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import javax.validation.constraints.NotNull;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.gameservice.annotation.Loggable;
import com.gameservice.domain.Player;
import com.gameservice.repository.PlayerRepository;
import com.gameservice.service.PlayerService;
import com.gameservice.service.PlayerService.LockType;
import com.gameservice.service.dto.PlayerSearchRequest;
import com.gameservice.service.dto.PlayerSearchResponse;
import com.gameservice.service.exceptions.PlayerException;
import com.gameservice.service.exceptions.PlayerException.PlayerExceptionCode;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Striped;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Loggable(detail = true)
public class PlayerServiceImpl implements PlayerService {

	final PlayerRepository playerRepo;
	final JmsTemplate jmsTemplate;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	@Override
	public PlayerSearchResponse searchPlayers(PlayerSearchRequest request) {
		List<Player> players = playerRepo.searchPlayers(request);
		return new PlayerSearchResponse(players);
	}

	@Override
	@CacheEvict(cacheNames = "players", key = "#p0")
	@Loggable(detail = true)
	public int save(Player player) {
		try {
			int i = playerRepo.insert(player);
			jmsTemplate.convertAndSend(PlayerService.PlayerDestination.CREATED.getName(), player);
			updateStats(player);
			return i;
		} catch (DuplicateKeyException e) {
			throw new PlayerException(PlayerExceptionCode.DUPLICATED_NAME, "Name: %s is duplicated!", player.getName());
		}
	}

	@Transactional
	public void updateStats(Player player) {

	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, Player> getTeamPlayers(Long teamId) {
		List<Player> players = playerRepo.getPlayersForTeam(teamId);
		return players.stream().collect(toMap(Player::getId, Function.identity()));
	}

	@Transactional(readOnly = true)
	@Override
	public Player getPlayer(Long playerId) {
		return playerCache.getUnchecked(playerId);
	}

	final LoadingCache<Long, Player> playerCache = CacheBuilder.newBuilder().initialCapacity(100).maximumSize(1000)
			.expireAfterWrite(3, TimeUnit.SECONDS)
			// testing
			.recordStats().build(new CacheLoader<Long, Player>() {
				@Override
				public Player load(Long id) throws Exception {
					return playerRepo.getById(id);
				}

			});

	@VisibleForTesting
	public CacheStats playerCacheStats() {
		return playerCache.stats();
	}

	@Override
	@Cacheable(cacheNames = "players")
	public Player getPlayerCached(Long playerId) {
		return playerRepo.getById(playerId);
	}

	// Locks in memory

	final ReentrantLock playerLock = new ReentrantLock();

	public Player saveWithMemoryLock(Player player) {
		try {
			if (playerLock.tryLock(5, TimeUnit.SECONDS)) {
				try {
					playerRepo.insert(player);
					return playerRepo.getById(player.getId());
				} finally {
					playerLock.unlock();
				}
			} else {
				throw new IllegalStateException();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	final Striped<Lock> stripedLock = Striped.lock(4);

	public Player saveWithStripedLock(Player player) {
		Lock playerLock = stripedLock.get(player.getNumber());
		try {
			if (playerLock.tryLock(5, TimeUnit.SECONDS)) {
				try {
					playerRepo.insert(player);
					return playerRepo.getById(player.getId());
				} finally {
					playerLock.unlock();
				}
			} else {
				throw new IllegalStateException();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public Player saveWithOptimisticLock(Player player) {
		if (player.getId() == null) {
			playerRepo.insert(player);
			return player;
		}
		Player existing = (player.getModifiedOn() != null) ? player : playerRepo.getById(player.getId());
		if (playerRepo.updateOptimistic(player, existing.getModifiedOn()) == 0) {
			throw new OptimisticLockingFailureException("Player id=" + player.getId() + " was changed in meantime!");

		}
		return playerRepo.getById(player.getId());
	}

	@Override
	public Player save(Player player, LockType lockType) {
		switch (lockType) {
		case MEMORY:
			return saveWithMemoryLock(player);
		case STRIPED:
			return saveWithStripedLock(player);
		case OPTIMISTIC:
			return saveWithOptimisticLock(player);
		default:
			throw new IllegalArgumentException("Unsupported lock type: " + lockType);
		}
	}

	@Override
	public List<Player> findPlayersByName(@NotNull String playerName, String teamId) {
		return playerRepo.findPlayersByName(playerName, teamId);
	}
}
