DROP TABLE IF EXISTS publication;
DROP TABLE IF EXISTS person;

CREATE TABLE person (
  id INTEGER PRIMARY KEY,
  lastname VARCHAR(45) NOT NULL,
  firstname VARCHAR(45) NOT NULL
);


CREATE TABLE publication (
  id INTEGER IDENTITY PRIMARY KEY,
  title VARCHAR(45) NOT NULL,
  id_author INTEGER NOT NULL,
  CONSTRAINT fk_id_author FOREIGN KEY (id_author) REFERENCES person(id)

); 




