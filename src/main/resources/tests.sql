
SELECT * FROM db_sprintboot.user;

INSERT INTO db_sprintboot.user (username, password, enabled) VALUES ('andres', '{bcrypt}$2a$10$y.GZC.pYAA/zcDv1RS8.tObhs.X3JKQZlmzaqVpfgsdr/MnDuNpBK',1);
INSERT INTO db_sprintboot.user (username, password, enabled) VALUES ('admin', '{bcrypt}$2a$10$UEnOBYHbG9ZRVyxw5hbhh.cvOQc2MaRNiHvgFA7q42MY5bXwWBTSa',1);


SELECT * FROM db_sprintboot.authorities;

insert into db_sprintboot.authorities (user_id, authority) values (3, 'ROLE_USER');
insert into db_sprintboot.authorities (user_id, authority) values (4, 'ROLE_USER');
insert into db_sprintboot.authorities (user_id, authority) values (4, 'ROLE_ADMIN');
