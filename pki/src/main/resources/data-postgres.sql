/*Sifre su 123*/

INSERT INTO authority (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO authority (id,name) VALUES (2,'ROLE_CLIENT');

INSERT INTO client (id, type_of_client, common_name) VALUES (1,1, 'FTN');

INSERT INTO users ( id, username, password,authority_id) VALUES ( 1,'admin@gmail.com','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',1);
INSERT INTO users ( id, username, password,authority_id,client_id) VALUES ( 2,'client1@gmail.com','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2,1);
