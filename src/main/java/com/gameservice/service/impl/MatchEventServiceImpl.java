package com.gameservice.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.gameservice.domain.MatchEvent;
import com.gameservice.message.EventMessage;
import com.gameservice.message.EventMessageConverter;
import com.gameservice.repository.MatchEventRepository;
import com.gameservice.service.MatchEventService;
import com.gameservice.service.dto.SendEventsRequest;

@Service
@Transactional(readOnly = true)
@Validated
public class MatchEventServiceImpl implements MatchEventService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private EventMessageConverter eventMessageConverter;
	@Autowired
	private MatchEventRepository eventRepo;

	@Override
	@Transactional
	@Async
	public Future<List<EventMessage>> sendEvents(SendEventsRequest request) {
		List<MatchEvent> events = eventRepo.findEvents(request.getFromDate(), request.getToDate(), 0);

		LocalDateTime prev = request.getRelativeTo();

		List<EventMessage> result = new ArrayList<>(events.size());

		for (MatchEvent e : events) {
			long timeToSleep = ChronoUnit.MILLIS.between(prev, e.getEventTimestamp());
			sleepMillis(request.calculateTimeToSleep(timeToSleep));

			// what does this button do
			if (eventRepo.updateSent(e.getId()) == 1) {
				EventMessage m = eventMessageConverter.convert(e);
				result.add(m);
				jmsTemplate.convertAndSend(request.getDestinationName(), m);
			}

			prev = e.getEventTimestamp();
		}

		return AsyncResult.forValue(result);
	}

	private void sleepMillis(long timeToSleep) {
		try {
			Thread.sleep(timeToSleep);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

}
