psql -h localhost -U postgres
> CREATE database db_user;
> CREATE role program WITH password 'test';
> GRANT ON PRIVILEGES ON database db_user TO program;
> ALTER role program WITH login;
> /q


psql
\connect db_user;
INSERT INTO roles VALUES (1, 'ROLE_USER');
INSERT INTO roles VALUES (2, 'ROLE_ADMIN');

