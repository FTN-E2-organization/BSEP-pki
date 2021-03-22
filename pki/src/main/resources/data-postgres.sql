/*Sifre su 123*/

INSERT INTO authority (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO authority (id,name) VALUES (2,'ROLE_SUBJECT');

INSERT INTO users (id, username, password,authority_id) VALUES ( 1,'admin@gmail.com','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',1);
INSERT INTO users (id, username, password,authority_id) VALUES ( 2,'ftn@uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);
INSERT INTO users (id, username, password,authority_id) VALUES ( 3,'marija.maric@uns.ac.rs','$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra',2);

INSERT INTO subject (id, type_of_subject, common_name, given_name, surname, organization, organization_unit, country_code, state, locality, email) VALUES 
				   (2, 1, 'FTN', '', '', 'Fakultet tehničkih nauka', 'Katedra za informatiku', 'RS', 'Srbija', 'Novi Sad', 'ftn@uns.ac.rs');
INSERT INTO subject (id, type_of_subject, common_name, given_name, surname, organization, organization_unit, country_code, state, locality, email) VALUES 
				   (3, 0, 'RA80/2019', 'Marija', 'Marić', 'Fakultet tehničkih nauka', '', 'RS', 'Srbija', 'Novi Sad', 'marija.maric@uns.ac.rs');
