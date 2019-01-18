CREATE database db_user;
CREATE role program WITH password 'test';
GRANT ALL PRIVILEGES ON database db_user TO program;
ALTER role program WITH login;
--
CREATE database db_flight;
CREATE role program WITH password 'test';
GRANT ALL PRIVILEGES ON database db_flight TO program;
ALTER role program WITH login;
--
CREATE database db_ticket;
CREATE role program WITH password 'test';
GRANT ALL PRIVILEGES ON database db_ticket TO program;
ALTER role program WITH login;
--
CREATE database db_route;
CREATE role program WITH password 'test';
GRANT ALL PRIVILEGES ON database db_route TO program;
ALTER role program WITH login;


\connect db_user;
INSERT INTO roles VALUES (1, 'ROLE_USER');
INSERT INTO roles VALUES (2, 'ROLE_ADMIN');

insert into services values (1, 'our_frontend', 'XsL2FuvmwFC4CTC', 'ede4bfb8-2acb-441e-9b00-4b786309fcd2');

--create admin
insert into user_roles values (1,2);

------------------------------
--подключение винда
psql -h localhost -U postgres
--пароль 1234
------------------------------
--подключение линух
sudo -i
su postgres
psql
------------------------------
--посмотреть таблицы базы
\dt
