create table teams(
	id number primary key,  
	created_on timestamp, 
	modified_on timestamp, 
	name varchar2(40), 
	team_id varchar2(40)
);

create sequence sq_teams;

create table players(
	id number primary key,
	player_id varchar2(40),
	team_id varchar2(40), 
	created_on timestamp, 
	modified_on timestamp,
	number varchar2(40),
	name varchar2(40), 
	position varchar2(40),
	height varchar2(40),
	weight varchar2(40), 
	birthDate varchar2(40), 
	nationality varchar2(40),
	experience number, 
	college varchar2(80)
); 
create sequence sq_players;
alter table players add constraint fk_players_team foreign key (team_id) references teams(team_id);


create table events(
	id number primary key,
	created_on timestamp,
	modified_on timestamp,
	match_id varchar2(20),
	score_summary varchar2(80),
	event_time number(7,2),
	event_type varchar2(20),
	home_team_action varchar2(80),
	away_team_action varchar2(80),
	points_made number,
	neutral_action varchar2(80),
	quarter varchar2(20),
	first_player_id varchar2(40),
	second_player_id varchar2(40)
);
create sequence sq_events;
alter table events add constraint fk_events_player1 foreign key (first_player_id) references players(player_id);
alter table events add constraint fk_events_player2 foreign key (second_player_id) references players(player_id);
