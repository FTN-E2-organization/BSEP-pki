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

/* user kome je istekao link za aktiviranje naloga: (lozinka: Helloword6#123*)*/
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'isa.user.test@gmail.com','$2y$12$XqFbIDMCL.eIfKfKYsfPROuFmZuEr7zhUtLM8Zs0OObA7QqGhry0K',2, false, '1234567k');
INSERT INTO confirmation_token (token_id, confirmation_token, creation_date, user_id) VALUES (10, 'f819e980-58c6-415f-8908-f602ddd0b773', '2021-04-10', 1);

INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'admin@mail.com','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',1, true, '1234567k');
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'ftn@uns.ac.rs','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',2, true, '1234567k');
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'marija.maric98@gmail.com','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',2, true, '1234567k');
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'pmf@uns.ac.rs','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',2, true, '1234567k');
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'pera_peric99@gmail.com','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',2, true, '1234567k');
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'etf.bg@gmail.com','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',2, true, '1234567k');
INSERT INTO users (id, username, password,authority_id, enabled, salt) VALUES (nextval('users_seq'),'nada_nadic@gmail.com','$2y$12$QJ/N2clZ3XTykH6bxOdW7e.XTzxKiH1KL2jaHyRZyCMSb0WUbQgWK',2, true, '1234567k');

