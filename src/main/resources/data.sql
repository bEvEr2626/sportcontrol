INSERT INTO sports (name) VALUES
('Football'),
('Basketball');

INSERT INTO teams (name) VALUES
('Lions'),
('Tigers'),
('Bears'),
('Sharks');

INSERT INTO tournaments (name, sport_id) VALUES
('Spring Cup', 1),
('City League', 1),
('Hoops Open', 2);

INSERT INTO tournament_teams (tournament_id, team_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 4),
(3, 3),
(3, 4);

INSERT INTO players (name, team_id) VALUES
('Alex Reed', 1),
('Jordan Lee', 1),
('Casey Park', 2),
('Taylor Quinn', 2),
('Riley Fox', 3),
('Morgan Gray', 3),
('Avery Lane', 4),
('Parker Cole', 4);

INSERT INTO matches (name, location, match_date, tournament_id, home_team_id, away_team_id) VALUES
('Match 1', 'Arena A', '2026-05-05 18:00:00', 1, 1, 2),
('Match 2', 'Arena B', '2026-05-06 19:30:00', 1, 3, 1),
('Match 3', 'Central Stadium', '2026-05-07 17:00:00', 2, 2, 4),
('Match 4', 'East Court', '2026-05-08 20:00:00', 3, 3, 4);