/*Sifre su 123*/

INSERT INTO permission (id, name) VALUES (1,'USER_getSubjects');
INSERT INTO permission (id, name) VALUES (2,'USER_addSubject');
INSERT INTO permission (id, name) VALUES (3,'CERTIFICATE_revoke');
INSERT INTO permission (id, name) VALUES (4,'CERTIFICATE_add');
INSERT INTO permission (id, name) VALUES (5,'CERTIFICATE_getAllCAs');
INSERT INTO permission (id, name) VALUES (6,'CERTIFICATE_getAll');
INSERT INTO permission (id, name) VALUES (7,'CERTIFICATE_getById');
INSERT INTO permission (id, name) VALUES (8,'CERTIFICATE_getBySubjectId');
INSERT INTO permission (id, name) VALUES (9,'CERTIFICATE_getValidById');
INSERT INTO permission (id, name) VALUES (10,'CERTIFICATE_download');

INSERT INTO authority (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO authority (id,name) VALUES (2,'ROLE_SUBJECT');

INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,1);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,3);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,4);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,5);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,6);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,7);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (1,9);

INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (2,7);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (2,8);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (2,9);
INSERT INTO AUTHORITIES_PERMISSIONS (authority_id, permission_id) VALUES (2,10);

INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'admin','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',1, true);
INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'ftn.uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2, true);
INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'marija.maric98','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2, true);
INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'pmf.uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2, true);
INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'pera_peric99','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2, true);
INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'etf.bg','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2, true);
INSERT INTO users (id, username, password,authority_id, enabled) VALUES (nextval('users_seq'),'nada_nadic','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2, true);

