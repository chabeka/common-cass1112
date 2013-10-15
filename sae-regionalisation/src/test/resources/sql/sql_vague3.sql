/*
 
 Pré-requis pour l'exécution du script :
 
 - création d'une base de données pour les traitements
 
 - création d'un répertoire /tmp/regio/
 
 - avoir importé les données du SAE à l'aide de l'autre script
 
 - dépôt dans ce répertoire des fichiers issus des moulinettes des fichiers V2 :
 		* regionalisation_coti.csv
 		* regionalisation_cpte.csv
 		* regionalisation_pers.csv
 	 
*/


/*
# ############################################
# Options Postgresql 
# ############################################
*/

-- Activation de la prise de mesure du temps d'exécution de chaque reqûete
\timing



/*
# ############################################
# Sort le timestamp du début du traitement 
# ############################################
*/
select current_timestamp;


/*
# ############################################
# Procédure stockée pour la transposition des 
# codes organismes
# ############################################
*/

CREATE OR REPLACE FUNCTION vague3_transpo_codeOrga (codeOrga varchar(5)) RETURNS varchar(5) AS
$$
DECLARE
  result varchar(5);
BEGIN

IF codeOrga='UR890' THEN 
  result := 'UR267';
ELSIF codeOrga='UR210' THEN 
  result := 'UR267';
ELSIF codeOrga='UR710' THEN 
  result := 'UR267';
ELSIF codeOrga='UR580' THEN 
  result := 'UR267';
ELSIF codeOrga='UR410' THEN 
  result := 'UR247';
ELSIF codeOrga='UR180' THEN 
  result := 'UR247';
ELSIF codeOrga='UR280' THEN 
  result := 'UR247';
ELSIF codeOrga='UR360' THEN 
  result := 'UR247';
ELSIF codeOrga='UR450' THEN 
  result := 'UR247';
ELSIF codeOrga='UR370' THEN 
  result := 'UR247';
ELSIF codeOrga='UR251' THEN 
  result := 'UR437';
ELSIF codeOrga='UR390' THEN 
  result := 'UR437';
ELSIF codeOrga='UR700' THEN 
  result := 'UR437';
ELSIF codeOrga='UR909' THEN 
  result := 'UR437';
ELSIF codeOrga='UR270' THEN 
  result := 'UR237';
ELSIF codeOrga='UR760' THEN 
  result := 'UR237';
ELSIF codeOrga='UR840' THEN 
  result := 'UR937';
ELSIF codeOrga='UR040' THEN 
  result := 'UR937';
ELSIF codeOrga='UR050' THEN 
  result := 'UR937';
ELSIF codeOrga='UR130' THEN 
  result := 'UR937';
ELSIF codeOrga='UR061' THEN 
  result := 'UR937';
ELSIF codeOrga='UR830' THEN 
  result := 'UR937';
ELSIF codeOrga='UR740' THEN 
  result := 'UR827';
ELSIF codeOrga='UR010' THEN 
  result := 'UR827';
ELSIF codeOrga='UR730' THEN 
  result := 'UR827';
ELSIF codeOrga='UR071' THEN 
  result := 'UR827';
ELSIF codeOrga='UR260' THEN 
  result := 'UR827';
ELSIF codeOrga='UR388' THEN 
  result := 'UR827';
ELSIF codeOrga='UR420' THEN 
  result := 'UR827';
ELSIF codeOrga='UR690' THEN 
  result := 'UR827';
ELSE
  result = NULL;
END IF;
RETURN result;
END;
$$
LANGUAGE 'plpgsql' STRICT;



/*
# ############################################
# Procédure stockée pour la mise à jour des  
# codes organismes
# ############################################
*/

CREATE OR REPLACE FUNCTION vague3_maj_codeOrga () RETURNS void AS
$$
DECLARE
  ligne RECORD;
  cogRenum VARCHAR(5);
  copRenum VARCHAR(5);
  isCogRenum BOOLEAN;
  isCopRenum BOOLEAN;
BEGIN

  FOR ligne IN

    SELECT id_doc, cog,cop
    FROM fonds_doc_prod

    LOOP

    cogRenum := vague3_transpo_codeorga(ligne.cog);
    copRenum := vague3_transpo_codeorga(ligne.cop);

    IF (cogRenum IS NULL) THEN
      isCogRenum := FALSE;
    ELSE
      isCogRenum := TRUE;
    END IF;

    IF (copRenum IS NULL) THEN
      isCopRenum := FALSE;
    ELSE
      isCopRenum := TRUE;
    END IF;
    
    -- RAISE NOTICE 'cog: %, cogRenum: %, isCogRenum: %, cop: %, copRenum: %, isCopRenum: %', ligne.cog, cogRenum, isCogRenum, ligne.cop, copRenum, isCopRenum;

    UPDATE fonds_doc_prod
    SET cog_renum=cogRenum, cop_renum=copRenum, is_cog_renum=isCogRenum, is_cop_renum=isCopRenum
    WHERE id_doc=ligne.id_doc;


  END LOOP;

END;
$$
LANGUAGE 'plpgsql' STRICT;


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
# Préparation de la table des traitements de
# renumération
##############################################
*/

-- Suppression de la table de traitement précédente, si elle existe
DROP TABLE IF EXISTS fonds_doc_prod;

-- Création de la table de traitement
CREATE TABLE fonds_doc_prod (
  id_doc character varying(36) NOT NULL,
  cog character varying(5),
  cop character varying(5),
  nce character varying(30),
  nci character varying(30),
  npe character varying(30),
  cog_renum character varying(5),
  cop_renum character varying(5),
  nce_renum character varying(30),
  nci_renum character varying(30),
  npe_renum character varying(30),
  is_nce_renum boolean,
  is_nci_renum boolean,
  is_npe_renum boolean,
  is_cop_renum boolean,
  is_cog_renum boolean,
  CONSTRAINT fonds_doc_prod_pkey PRIMARY KEY (id_doc)
);

-- Copie de la table fonds_doc_prod_datas dans une nouvelle table fonds_doc_prod
INSERT INTO fonds_doc_prod
SELECT * FROM fonds_doc_prod_datas;
-- FAB1, sur cer69imageint10.cer69.recouv: 5 045 296 ms (1h24)

-- Vérification visuelle
-- SELECT * FROM fonds_doc_prod LIMIT 1000;

-- Vérification du nombre d'enregistrements importés
-- Le comptage doit correspondre à la somme du nombre de lignes des fichiers d'extraction
INSERT INTO comptages
SELECT 'fonds_doc_prod.count', COUNT(*) FROM fonds_doc_prod_datas;

-- Optimisation
VACUUM ANALYZE fonds_doc_prod;


/*
##############################################
# Import des fichiers V2 retravaillés - nce
############################################## 
*/


-- Suppression de la table précédente
DROP TABLE IF EXISTS regionalisation_coti;


-- Création de la structure initiale
CREATE TABLE regionalisation_coti
(
  code character varying(8),
  old_cog character varying(3),
  old_reference character varying(50),
  new_reference character varying(50),
  new_cog character varying(3)
);


-- Import du fichier CSV
COPY regionalisation_coti from '/tmp/regio/regionalisation_coti.csv' WITH DELIMITER '$';


-- Suppression du 1er champ inutile
ALTER TABLE regionalisation_coti
DROP COLUMN code;


-- Vérification visuelle
-- SELECT * FROM regionalisation_coti LIMIT 1000;


-- Ajout d'un champ concaténant le texte "UR" ainsi que le code organisme sur 3 positions
-- afin de matcher la structure des données SAE 
ALTER TABLE regionalisation_coti
ADD COLUMN old_cog_avec_ur character varying(5);
--
UPDATE regionalisation_coti
SET old_cog_avec_ur='UR' || old_cog;


-- Vérification visuelle
-- SELECT * FROM regionalisation_coti LIMIT 1000;


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
ORDER BY old_cog, debut_old_reference;

-- SELECT * FROM nce_detection_anomalie1_etape1 LIMIT 1000;

SELECT old_cog, debut_old_reference, count(*) as nombre
INTO nce_detection_anomalie1_etape2
FROM nce_detection_anomalie1_etape1
WHERE debut_old_reference<>old_cog
GROUP BY debut_old_reference,old_cog
ORDER BY old_cog, debut_old_reference, nombre;

-- SELECT * FROM nce_detection_anomalie1_etape1 LIMIT 1000;

-- SELECT * FROM nce_detection_anomalie1_etape2;

-- SELECT SUM(nombre) FROM nce_detection_anomalie1_etape2;

COPY nce_detection_anomalie1_etape1
TO '/tmp/regio/nce_detection_anomalie2_etape2.csv'
WITH CSV HEADER; 

COPY nce_detection_anomalie1_etape2
TO '/tmp/regio/nce_detection_anomalie2_etape1.csv'
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

-- SELECT COUNT(*) FROM detection_anomalie2;

-- SELECT * FROM detection_anomalie2 LIMIT 1000;

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


-- Création de la structure initiale
CREATE TABLE regionalisation_cpte
(
  code character varying(8),
  old_cog character varying(3),
  old_reference character varying(50),
  new_reference character varying(50),
  new_cog character varying(3)
);


-- Import du fichier CSV
COPY regionalisation_cpte from '/tmp/regio/regionalisation_cpte.csv' WITH DELIMITER '$';


-- Suppression du 1er champ inutile
ALTER TABLE regionalisation_cpte
DROP COLUMN code;


-- Vérification visuelle
-- SELECT * FROM regionalisation_cpte LIMIT 1000;


-- Ajout d'un champ concaténant le texte "UR" ainsi que le code organisme sur 3 positions
-- afin de matcher la structure des données SAE 
ALTER TABLE regionalisation_cpte
ADD COLUMN old_cog_avec_ur character varying(5);
--
UPDATE regionalisation_cpte
SET old_cog_avec_ur='UR' || old_cog;


-- Vérification visuelle
-- SELECT * FROM regionalisation_cpte LIMIT 1000;


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


-- Création de la structure initiale
CREATE TABLE regionalisation_pers
(
  code character varying(8),
  old_cog character varying(3),
  old_reference character varying(50),
  new_reference character varying(50),
  new_cog character varying(3)
);


-- Import du fichier CSV
COPY regionalisation_pers from '/tmp/regio/regionalisation_pers.csv' WITH DELIMITER '$';


-- Suppression du 1er champ inutile
ALTER TABLE regionalisation_pers
DROP COLUMN code;


-- Vérification visuelle
-- SELECT * FROM regionalisation_pers LIMIT 1000;


-- Ajout d'un champ concaténant le texte "UR" ainsi que le code organisme sur 3 positions
-- afin de matcher la structure des données SAE 
ALTER TABLE regionalisation_pers
ADD COLUMN old_cog_avec_ur character varying(5);
--
UPDATE regionalisation_pers
SET old_cog_avec_ur='UR' || old_cog;


-- Vérification visuelle
-- SELECT * FROM regionalisation_pers LIMIT 1000;


-- Suppression des zéros préfixant les numéros de personne
-- Dans le SAE, on a stocké les numéros de personne en numérique
UPDATE regionalisation_pers
SET old_reference = CAST(CAST(old_reference AS INTEGER) AS VARCHAR);
--  
UPDATE regionalisation_pers
SET new_reference = CAST(CAST(new_reference AS INTEGER) AS VARCHAR);


-- Vérification visuelle
--SELECT * FROM regionalisation_pers LIMIT 1000;


-- Optimisation
VACUUM ANALYZE regionalisation_pers;


-- Comptage
INSERT INTO comptages
SELECT 'regionalisation_pers.count', COUNT(*) FROM regionalisation_pers;




/*
##############################################
# Changement des codes organismes
##############################################
*/

-- Exécution de la procédure stockée
SELECT vague3_maj_codeOrga();
-- Temps de temps de traitement en FAB1 sur cer69imageint10: 10 481 909 ms (3h)


-- Vérification visuelle
-- SELECT * FROM fonds_doc_prod LIMIT 1000;



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
-- FAB1 sur cer69imageint10: Temps de traitement: 6 765 760 ms (2h), nb lignes modifiées: 1 507 670 


-- cas non standards
-- TODO: à étudier (exemple en vague 2 : les TGE de Paris)


-- Vérification visuelle
-- SELECT * FROM fonds_doc_prod WHERE is_nce_renum=true LIMIT 1000;


-- Détection des anomalies
-- nce non renumérotés alors que le cog a changé
DROP TABLE IF EXISTS nce_detection_anomalie_nce_non_renum_cog_change;
--
SELECT *
INTO nce_detection_anomalie_nce_non_renum_cog_change
FROM fonds_doc_prod
WHERE ((is_nce_renum IS NULL) OR (is_nce_renum=false))
AND (is_cog_renum=true);
--
COPY nce_detection_anomalie_nce_non_renum_cog_change
TO '/tmp/regio/nce_detection_anomalie_nce_non_renum_cog_change.csv'
WITH CSV HEADER;


-- Détection des anomalies
-- nce renumérotés alors que le cog n'a pas changé
-- 
DROP TABLE IF EXISTS nce_detection_anomalie_nce_non_renum_cog_non_change;
-- 
SELECT *
INTO nce_detection_anomalie_nce_non_renum_cog_non_change
FROM fonds_doc_prod
WHERE ((is_nce_renum IS NULL) OR (is_nce_renum=false))
AND (is_cog_renum=true);
-- 
-- SELECT COUNT(*) FROM detection_anomalie_nce_non_renum;
-- 
-- SELECT * FROM detection_anomalie_nce_non_renum LIMIT XXX;
--
COPY nce_detection_anomalie_nce_non_renum_cog_non_change
TO '/tmp/regio/nce_detection_anomalie_nce_non_renum_cog_non_change.csv'
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
-- FAB1 sur cer69imageint10: Temps de traitement: 850 298 ms (15 mn), nb lignes modifiées: 174 733 


-- Vérification visuelle
-- SELECT * FROM fonds_doc_prod WHERE is_nci_renum=true LIMIT 1000;



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
-- FAB1 sur cer69imageint10: Temps de traitement: 2 198 614 ms (35 mn), nb lignes modifiées: 616 970


-- Vérification visuelle
-- SELECT * FROM fonds_doc_prod WHERE is_npe_renum=true LIMIT 1000;



/*
# ############################################
# Vérification visuelle globale
# ############################################
*/

-- SELECT * FROM fonds_doc_prod WHERE (is_nce_renum=true) OR (is_nci_renum=true) OR (is_npe_renum=true) OR (is_cog_renum=true) OR (is_cop_renum=true)  LIMIT 1000;



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
-- # FAB1 : 1 517 305

-- SELECT * FROM docs_a_traiter LIMIT 1000;

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



