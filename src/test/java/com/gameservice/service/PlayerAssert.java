package com.gameservice.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.gameservice.domain.Player;

public final class PlayerAssert {
	private PlayerAssert() {
	}

	public static void assertPlayer(Player p, String name, String number, String position, String teamId) {
		assertThat(p, notNullValue());
		assertThat(p.getName(), equalTo(name));
		assertThat(p.getNumber(), equalTo(number));
		assertThat(p.getPosition(), equalTo(position));
		assertThat(p.getTeam(), notNullValue());
		assertThat(p.getTeam().getTeamId(), equalTo(teamId));
	}
}
