/*
 
 Pré-requis pour l'exécution du script :
 
 - création d'une base de données pour les traitements
 
 - création d'un répertoire /tmp/regio/
 
 - dépôt dans ce répertoire des fichiers issus de l'extraction du fonds documentaire du SAE :
 		* fonds_doc_*.csv
 		
*/


/*
##############################################
# Import des données extraites du SAE
#
# Cette table peut-être alimentée au fur et à
# mesure que l'on extrait le fonds documentaire
# de la production, puisque l'on est capable
# de faire une extraction par mois d'archivage
##############################################
*/


-- Création de la table
CREATE TABLE fonds_doc_prod_datas (
  id_doc character varying(36) NOT NULL,
  cog character varying(5),
  cop character varying(5),
  nce character varying(30),
  nci character varying(30),
  npe character varying(30),
  CONSTRAINT fonds_doc_prod_datas_pkey PRIMARY KEY (id_doc)
);


-- Import des fichiers CSV
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201201.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201202.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201203.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201204.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201205.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201206.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201207.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201208.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201209.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201210.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201211.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201212.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201301.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201302.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201303.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201304.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201305.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201306.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201307.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201308.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201309.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201310.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201311.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201312.csv' WITH DELIMITER ';';
COPY fonds_doc_prod_datas FROM '/tmp/regio/fonds_doc_201401.csv' WITH DELIMITER ';';

