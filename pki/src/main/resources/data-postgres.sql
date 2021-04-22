/*Sifre su 123*/

INSERT INTO authority (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO authority (id,name) VALUES (2,'ROLE_SUBJECT');


INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'admin','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',1, true, '1234567k');

/* user kom je istekao link za aktiviranje naloga: */
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'jelenazeljko@live.com','$2y$10$cbn.yFXEQMvDBnZ3HrWoaeQgrRzknddNV9/.Vi1OWFihcvbCGqafi',2, false, '1234567k');
INSERT INTO confirmation_token (token_id, confirmation_token, creation_date, user_id) VALUES (10, 'f819e980-58c6-415f-8908-f602ddd0b773', '2021-04-10', 2);

/*INSERT INTO users (id, username, password,authority_id) VALUES ( 2,'ftn.uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 3,'marija.maric98','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 4,'pmf.uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 5,'pera_peric99','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 6,'etf.bg','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 7,'nada_nadic','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);*/

