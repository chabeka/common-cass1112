CREATE TABLE auteur (
   id INTEGER PRIMARY KEY,
   nom VARCHAR(45) NOT NULL
);

CREATE TABLE document (
   id INTEGER IDENTITY PRIMARY KEY,
   titre VARCHAR(45) NOT NULL,
   date TIMESTAMP NOT NULL,
   id_auteur INTEGER NOT NULL,
   CONSTRAINT fk_document_auteur FOREIGN KEY (id_auteur) REFERENCES auteur(id)
);

CREATE TABLE etat (
   id INTEGER PRIMARY KEY,
   id_document INTEGER NOT NULL,
   etat VARCHAR(45) NOT NULL,
   date TIMESTAMP NOT NULL,
   CONSTRAINT fk_id_document FOREIGN KEY (id_document) REFERENCES document(id)
);
