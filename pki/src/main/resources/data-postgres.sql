/*Sifre su 123*/

INSERT INTO authority (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO authority (id,name) VALUES (2,'ROLE_SUBJECT');

INSERT INTO users (id, username, password,authority_id) VALUES ( 1,'admin@gmail.com','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',1);
INSERT INTO users (id, username, password,authority_id) VALUES ( 2,'ftn@uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 3,'marija.maric@uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
