/*
 
 Pré-requis pour l'exécution du script :
 
 - création d'une base de données nommée "regio"
 
 - création d'un répertoire /tmp/regio/
 
 - dépôt dans ce répertoire des fichiers issus des moulinettes des fichiers V2 :
 		* regionalisation_coti.csv
 		* regionalisation_cpte.csv
 		* regionalisation_pers.csv

 - dépôt dans ce répertoire du fichier du fonds documentaire sae,
   pré-traité avec sed pour la mise à jour des codes organisme :
 		* fonds_doc.csv
 	 
*/


/*
# ############################################
# Options Postgresql 
# ############################################
*/

-- Activation de la prise de mesure du temps d'exécution de chaque requête
\timing



/*
# ############################################
# Sort le timestamp du début du traitement 
# ############################################
*/
select current_timestamp;



/*
##############################################
# Table de comptages pour les vérifications
##############################################
*/

DROP TABLE IF EXISTS comptages;

CREATE TABLE comptages (
   nom_comptage character varying(30) NOT NULL,
   nombre integer,
   CONSTRAINT comptages_pkey PRIMARY KEY (nom_comptage)
);



/*
##############################################
# Import des données extraites du SAE
##############################################
*/

DROP TABLE IF EXISTS fonds_doc_prod;

-- Création de la table et alimentation dans la même transaction
-- afin de pouvoir utiliser le COPY FROM FREEZE

-- Début de la transaction
BEGIN TRANSACTION;

-- Création de la table, optimisation avec UNLOGGER
CREATE UNLOGGED TABLE fonds_doc_prod (
  id_doc character varying(36) NOT NULL,
  cog character varying(5),
  cog_renum character varying(5),
  is_cog_renum boolean,
  cop character varying(5),
  cop_renum character varying(5),
  is_cop_renum boolean,
  nce character varying(30),
  nci character varying(30),
  npe character varying(30),
  nce_renum character varying(30),
  nci_renum character varying(30),
  npe_renum character varying(30),
  is_nce_renum boolean,
  is_nci_renum boolean,
  is_npe_renum boolean,
  CONSTRAINT fonds_doc_prod_pkey PRIMARY KEY (id_doc)
);

-- Import du fichier CSV, optimisation avec FREEZE
COPY fonds_doc_prod(id_doc,cog,cog_renum,is_cog_renum,cop,cop_renum,is_cop_renum,nce,nci,npe) FROM '/tmp/regio/fonds_doc.csv' WITH DELIMITER ';' FREEZE;

-- Fin de la transaction
END TRANSACTION;


-- Vérification du nombre d'enregistrements importés
-- Le comptage doit correspondre à la somme du nombre de lignes des fichiers d'extraction
INSERT INTO comptages
SELECT 'fonds_doc_prod.count', COUNT(*) FROM fonds_doc_prod;





/*
##############################################
# Import des fichiers V2 retravaillés - nce
############################################## 
*/


-- Suppression de la table précédente
DROP TABLE IF EXISTS regionalisation_coti;


-- Création de la table et alimentation dans la même transaction
-- afin de pouvoir utiliser le COPY FROM FREEZE

-- Début de la transaction
BEGIN TRANSACTION;

-- Création de la structure initiale
CREATE UNLOGGED TABLE regionalisation_coti
(
  code character varying(8),
  old_cog character varying(3),
  old_reference character varying(50),
  new_reference character varying(50),
  new_cog character varying(3)
);

-- Import du fichier CSV
COPY regionalisation_coti from '/tmp/regio/regionalisation_coti.csv' WITH DELIMITER '$' FREEZE;

-- Fin de la transaction
END TRANSACTION;


-- Suppression du 1er champ inutile
ALTER TABLE regionalisation_coti
DROP COLUMN code;


-- Ajout d'un champ concaténant le texte "UR" ainsi que le code organisme sur 3 positions
-- afin de matcher la structure des données SAE 
ALTER TABLE regionalisation_coti
ADD COLUMN old_cog_avec_ur character varying(5);
--
UPDATE regionalisation_coti
SET old_cog_avec_ur='UR' || old_cog;

-- Optimisation
VACUUM ANALYZE regionalisation_coti;

-- Comptage
INSERT INTO comptages
SELECT 'regionalisation_coti.count', COUNT(*) FROM regionalisation_coti;


-- Pré-analyse des cas particuliers
-- Cas des numéros de compte externes qui commencent par un trigramme différent du code organisme à régionaliser

DROP TABLE IF EXISTS nce_detection_anomalie1_etape1;

DROP TABLE IF EXISTS nce_detection_anomalie1_etape2;

SELECT *, SUBSTRING(old_reference,1,3) AS debut_old_reference
INTO nce_detection_anomalie1_etape1
FROM regionalisation_coti
WHERE SUBSTRING(old_reference,1,3)<>old_cog
ORDER BY old_cog, debut_old_reference, old_reference;

SELECT old_cog, debut_old_reference, count(*) as nombre
INTO nce_detection_anomalie1_etape2
FROM nce_detection_anomalie1_etape1
WHERE debut_old_reference<>old_cog
GROUP BY debut_old_reference,old_cog
ORDER BY old_cog, debut_old_reference, nombre;

COPY nce_detection_anomalie1_etape1
TO '/tmp/regio/nce_detection_anomalie1_etape1.csv'
WITH CSV HEADER; 

COPY nce_detection_anomalie1_etape2
TO '/tmp/regio/nce_detection_anomalie1_etape2.csv'
WITH CSV HEADER;



-- Pré-analyse des cas particuliers
-- Cas des numéros de compte externes que l'on trouve dans le SAE avec un code organisme
-- différent de celui défini dans les fichiers V2

DROP TABLE IF EXISTS nce_detection_anomalie2;

SELECT cog,old_cog,nce,cop
INTO nce_detection_anomalie2
FROM fonds_doc_prod,regionalisation_coti
WHERE (fonds_doc_prod.nce=regionalisation_coti.old_reference)
AND (fonds_doc_prod.cog<>regionalisation_coti.old_cog_avec_ur)
ORDER BY cog, old_cog, nce;

COPY nce_detection_anomalie2
TO '/tmp/regio/nce_detection_anomalie_2.csv'
WITH CSV HEADER; 




/*
##############################################
# Import des fichiers V2 retravaillés - nci
##############################################
*/


-- Suppression de la table précédente
DROP TABLE IF EXISTS regionalisation_cpte;


-- Création de la table et alimentation dans la même transaction
-- afin de pouvoir utiliser le COPY FROM FREEZE

-- Début de la transaction
BEGIN TRANSACTION;

-- Création de la structure initiale
CREATE UNLOGGED TABLE regionalisation_cpte
(
  code character varying(8),
  old_cog character varying(3),
  old_reference character varying(50),
  new_reference character varying(50),
  new_cog character varying(3)
);


-- Import du fichier CSV
COPY regionalisation_cpte from '/tmp/regio/regionalisation_cpte.csv' WITH DELIMITER '$' FREEZE;

-- Fin de la transaction
END TRANSACTION;


-- Suppression du 1er champ inutile
ALTER TABLE regionalisation_cpte
DROP COLUMN code;


-- Ajout d'un champ concaténant le texte "UR" ainsi que le code organisme sur 3 positions
-- afin de matcher la structure des données SAE 
ALTER TABLE regionalisation_cpte
ADD COLUMN old_cog_avec_ur character varying(5);
--
UPDATE regionalisation_cpte
SET old_cog_avec_ur='UR' || old_cog;


-- Optimisation
VACUUM ANALYZE regionalisation_cpte;


-- Comptage
INSERT INTO comptages
SELECT 'regionalisation_cpte.count', COUNT(*) FROM regionalisation_cpte;



/*
##############################################
# Import des fichiers V2 retravaillés - npe
##############################################
*/


-- Suppression de la table précédente
DROP TABLE IF EXISTS regionalisation_pers;

-- Création de la table et alimentation dans la même transaction
-- afin de pouvoir utiliser le COPY FROM FREEZE

-- Début de la transaction
BEGIN TRANSACTION;

-- Création de la structure initiale
CREATE UNLOGGED TABLE regionalisation_pers
(
  code character varying(8),
  old_cog character varying(3),
  old_reference character varying(50),
  new_reference character varying(50),
  new_cog character varying(3)
);

-- Import du fichier CSV
COPY regionalisation_pers from '/tmp/regio/regionalisation_pers.csv' WITH DELIMITER '$' FREEZE;

-- Fin de la transaction
END TRANSACTION;


-- Suppression du 1er champ inutile
ALTER TABLE regionalisation_pers
DROP COLUMN code;


-- Ajout d'un champ concaténant le texte "UR" ainsi que le code organisme sur 3 positions
-- afin de matcher la structure des données SAE 
ALTER TABLE regionalisation_pers
ADD COLUMN old_cog_avec_ur character varying(5);
--
UPDATE regionalisation_pers
SET old_cog_avec_ur='UR' || old_cog;


-- Suppression des zéros préfixant les numéros de personne
-- Dans le SAE, on a stocké les numéros de personne en numérique
UPDATE regionalisation_pers
SET old_reference = CAST(CAST(old_reference AS INTEGER) AS VARCHAR);
--  
UPDATE regionalisation_pers
SET new_reference = CAST(CAST(new_reference AS INTEGER) AS VARCHAR);


-- Optimisation
VACUUM ANALYZE regionalisation_pers;


-- Comptage
INSERT INTO comptages
SELECT 'regionalisation_pers.count', COUNT(*) FROM regionalisation_pers;



/*
# ############################################
# Renumérotation nce
# ############################################
*/


-- Renumérotation
UPDATE fonds_doc_prod
SET nce_renum = sousRequete.new_reference, is_nce_renum=true
FROM (SELECT new_reference, old_cog_avec_ur, old_reference
      FROM regionalisation_coti) AS sousRequete
WHERE (sousRequete.old_reference<>sousRequete.new_reference) AND 
      (fonds_doc_prod.nce=sousRequete.old_reference) AND 
      (fonds_doc_prod.cog=sousRequete.old_cog_avec_ur);


-- cas non standards
-- TODO: à étudier (exemple en vague 2 : les TGE de Paris)


-- Détection des anomalies
-- nce non renumérotés alors que le cog a changé

DROP TABLE IF EXISTS nce_detection_anomalie_nce_non_renum_cog_change;

SELECT *
INTO nce_detection_anomalie_nce_non_renum_cog_change
FROM fonds_doc_prod
WHERE ((is_nce_renum IS NULL) OR (is_nce_renum=false))
AND (is_cog_renum=true);

COPY nce_detection_anomalie_nce_non_renum_cog_change
TO '/tmp/regio/nce_detection_anomalie_nce_non_renum_cog_change.csv'
WITH CSV HEADER;


-- Détection des anomalies
-- nce renumérotés alors que le cog n'a pas changé

DROP TABLE IF EXISTS nce_detection_anomalie_nce_renum_cog_non_change;

SELECT *
INTO nce_detection_anomalie_nce_renum_cog_non_change
FROM fonds_doc_prod
WHERE (is_nce_renum=true)
AND (is_cog_renum=false);

COPY nce_detection_anomalie_nce_renum_cog_non_change
TO '/tmp/regio/nce_detection_anomalie_nce_renum_cog_non_change.csv'
WITH CSV HEADER;



/*
# ############################################
# Renumérotation nci
# ############################################
*/

-- Renumérotation
UPDATE fonds_doc_prod
SET nci_renum = sousRequete.new_reference, is_nci_renum=true
FROM (SELECT new_reference, old_cog_avec_ur, old_reference
      FROM regionalisation_cpte) AS sousRequete
WHERE (sousRequete.old_reference<>sousRequete.new_reference) AND
      (fonds_doc_prod.cog=sousRequete.old_cog_avec_ur) AND 
      (fonds_doc_prod.nci=sousRequete.old_reference); 



/*
# ############################################
# Renumérotation npe
# ############################################
*/

-- Renumérotation
UPDATE fonds_doc_prod
SET npe_renum = sousRequete.new_reference, is_npe_renum=true
FROM (SELECT new_reference, old_cog_avec_ur, old_reference
      FROM regionalisation_pers) AS sousRequete
WHERE (sousRequete.old_reference<>sousRequete.new_reference) AND
      (fonds_doc_prod.cog=sousRequete.old_cog_avec_ur) AND 
      (fonds_doc_prod.npe=sousRequete.old_reference);


/*
# ############################################
# Transformation des champs vides en champ
# null pour garantir un export sans quote
# pour les valeurs vides (problème survenu
# lors des tests de régio)
# ############################################
*/

UPDATE fonds_doc_prod
SET cog_renum=null
WHERE cog_renum='';

UPDATE fonds_doc_prod
SET cop_renum=null
WHERE cop_renum='';

UPDATE fonds_doc_prod
SET nce_renum=null
WHERE nce_renum='';

UPDATE fonds_doc_prod
SET nci_renum=null
WHERE nci_renum='';

UPDATE fonds_doc_prod
SET npe_renum=null
WHERE npe_renum='';


/*
# ############################################
# Export des données pour le programme de
# régionalisation SAE
# ############################################
*/

-- Export des documents dont il faut modifier au moins 1 métadonnée

DROP TABLE IF EXISTS docs_a_traiter;


SELECT id_doc,nce_renum,nci_renum,npe_renum,cog_renum,cop_renum 
INTO docs_a_traiter
FROM fonds_doc_prod
WHERE (is_nce_renum=true) OR (is_nci_renum=true) OR (is_npe_renum=true) OR (is_cog_renum=true) OR (is_cop_renum=true)
ORDER BY id_doc;

-- Comptages
INSERT INTO comptages
SELECT 'docs_a_traiter.count', COUNT(*) FROM docs_a_traiter;

-- Sortie de la table dans un fichier CSV
COPY
docs_a_traiter
TO '/tmp/regio/docs_a_traiter.csv'
WITH CSV DELIMITER ';';



/*
# ############################################
# Contrôles
# ############################################
*/


-- Sortie des comptages
SELECT * FROM comptages;


/*
# ############################################
# Sort le timestamp de la fin du traitement 
# ############################################
*/
select current_timestamp;




/*
# ############################################
# Vérifications visuelles 
# ############################################
*/

-- SELECT * FROM regionalisation_coti LIMIT 1000;

-- SELECT * FROM regionalisation_cpte LIMIT 1000;

-- SELECT * FROM regionalisation_pers LIMIT 1000;

-- SELECT * FROM nce_detection_anomalie1_etape1 LIMIT 1000;
-- SELECT * FROM nce_detection_anomalie1_etape2 LIMIT 1000;

-- SELECT * FROM detection_anomalie_nce_non_renum LIMIT XXX;

/*
SELECT * FROM fonds_doc_prod
WHERE
	(is_nce_renum=true)
	OR (is_nci_renum=true)
	OR (is_npe_renum=true) 
	OR (is_cog_renum=true) 
	OR (is_cop_renum=true)
LIMIT 1000;
*/

-- SELECT * FROM docs_a_traiter LIMIT 1000;







