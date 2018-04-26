CREATE TABLE logger (
  idseq INTEGER PRIMARY KEY,
  horodatage TIMESTAMP NOT NULL,
  occurences SMALLINT NOT NULL,
  probleme VARCHAR(100) NOT NULL,
  action CHAR(50) NOT NULL,
  infos VARCHAR(200) NOT NULL
);


