INSERT INTO auteur (id,nom) VALUES (0,'auteur 0');
INSERT INTO auteur (id,nom) VALUES (1,'auteur 1');
INSERT INTO auteur (id,nom) VALUES (2,'auteur 2');
INSERT INTO auteur (id,nom) VALUES (3,'auteur 3');
INSERT INTO auteur (id,nom) VALUES (4,'auteur 4');

INSERT INTO document (id,titre,date,id_auteur) VALUES (1,'titre 1','2002-04-08 00:00:00',3);
INSERT INTO document (id,titre,date,id_auteur) VALUES (2,'titre 2','1999-07-03 00:00:00',4);
INSERT INTO document (id,titre,date,id_auteur) VALUES (3,'titre 3','2023-08-26 00:00:00',1);
INSERT INTO document (id,titre,date,id_auteur) VALUES (4,'titre 4','2025-03-30 00:00:00',2);
INSERT INTO document (id,titre,date,id_auteur) VALUES (5,'titre 5','2024-02-18 00:00:00',0);
 
INSERT INTO etat (id,id_document,etat,date) VALUES (0,2,'init','1999-12-31 23:59:59');
INSERT INTO etat (id,id_document,etat,date) VALUES (1,2,'open','2000-12-31 23:59:59');
INSERT INTO etat (id,id_document,etat,date) VALUES (2,2,'close','2001-12-31 23:59:59');
INSERT INTO etat (id,id_document,etat,date) VALUES (3,3,'init','1999-12-31 23:59:59');
INSERT INTO etat (id,id_document,etat,date) VALUES (4,3,'open','2001-12-31 23:59:59');
INSERT INTO etat (id,id_document,etat,date) VALUES (5,1,'open','1998-12-31 23:59:59');