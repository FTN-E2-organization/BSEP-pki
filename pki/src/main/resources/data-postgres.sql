/*Sifre su 123*/

INSERT INTO authority (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO authority (id,name) VALUES (2,'ROLE_CLIENT');

INSERT INTO client (id, type_of_client, common_name) VALUES (1,1, 'FTN');

INSERT INTO users (id, username, password,authority_id) VALUES (1,'admin@gmail.com','$2a$10$iMzA8v0mJxErATSSs9vNbesXLDfv2r1koG1rdg6y7vvEDTz7gOYHm',1);
INSERT INTO users (id, username, password,authority_id,client_id) VALUES (2,'client1@gmail.com','$2a$10$iMzA8v0mJxErATSSs9vNbesXLDfv2r1koG1rdg6y7vvEDTz7gOYHm',2,1);
