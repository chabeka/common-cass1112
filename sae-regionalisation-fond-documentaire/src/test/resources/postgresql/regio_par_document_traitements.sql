-- Creation de la base de donnnées
CREATE DATABASE regio
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;


-- Table d'import de regionalisation_coti 
CREATE TABLE regionalisation_coti (
    code character varying(8),
    old_cog character varying(3),
    old_reference character varying(50),
    new_reference character varying(50),
    new_cog character varying(3),
    old_cog_avec_ur character varying(5)
);
ALTER TABLE regionalisation_coti OWNER TO postgres;


-- Table d'import de regionalisation_cpte
CREATE TABLE regionalisation_cpte (
    code character varying(8),
    old_cog character varying(3),
    old_reference character varying(50),
    new_reference character varying(50),
    new_cog character varying(3),
    old_cog_avec_ur character varying(5)
);
ALTER TABLE regionalisation_cpte OWNER TO postgres;


-- Table d'import de regionalisation_pers
CREATE TABLE regionalisation_pers (
    code character varying(8),
    old_cog character varying(3),
    old_reference character varying(50),
    new_reference character varying(50),
    new_cog character varying(3),
    old_cog_avec_ur character varying(5),
    old_reference_sans_zeros_devant character varying(50),
    new_reference_sans_zeros_devant character varying(50)
);
ALTER TABLE regionalisation_pers OWNER TO postgres;


-- Table d'import du fichier fonds_doc.csv
CREATE TABLE fonds_doc (
  id_doc character varying(36) NOT NULL,
  cog character varying(5),
  cop character varying(5),
  nce character varying(30),
  nci character varying(30),
  npe character varying(30),
  nce_renum character varying(30),
  nci_renum character varying(30),
  npe_renum character varying(30),
  cog_final character varying(5),
  cop_final character varying(5),
  nce_final character varying(30),
  nci_final character varying(30),
  npe_final character varying(30),
  is_nce integer,
  is_nci integer,
  is_npe integer,
  is_cog integer,
  is_cop integer,
  is_regio integer,
  CONSTRAINT fonds_doc_pkey PRIMARY KEY (id_doc)
);
ALTER TABLE fonds_doc OWNER TO postgres;

-- Import du fichier regionalisation_coti.csv dans la table regionalisation_coti 
copy "regionalisation_coti" from '/tmp/regionalisation_coti.csv' WITH DELIMITER ',' CSV HEADER;


-- Import du fichier regionalisation_cpte.csv dans la table regionalisation_cpte
copy "regionalisation_cpte" from '/tmp/regionalisation_cpte.csv' WITH DELIMITER ',' CSV HEADER;


-- Import du fichier regionalisation_pers.csv dans la table regionalisation_pers   
copy "regionalisation_pers" from '/tmp/regionalisation_pers.csv' WITH DELIMITER ',' CSV HEADER;


-- Import du fichier fonds_doc.csv dans la table fonds_doc   
copy "fonds_doc"(id_doc,cog,cop,nce,nci,npe) from '/tmp/fonds_doc.csv' WITH DELIMITER ';'; 



-- Création de la procédure stockée pour le traitement des codes organismes
CREATE OR REPLACE FUNCTION transpo_codeOrga (codeOrga varchar(5)) RETURNS varchar(5) AS
$$
DECLARE
  result varchar(5);
BEGIN

IF codeOrga='UR680' THEN 
  result := 'UR427';
ELSIF codeOrga='UR670' THEN 
  result := 'UR427';
ELSIF codeOrga='UR470' THEN 
  result := 'UR727';
ELSIF codeOrga='UR330' THEN 
  result := 'UR727';
ELSIF codeOrga='UR400' THEN 
  result := 'UR727';
ELSIF codeOrga='UR240' THEN 
  result := 'UR727';
ELSIF codeOrga='UR640' THEN 
  result := 'UR727';
ELSIF codeOrga='UR610' THEN 
  result := 'UR257';
ELSIF codeOrga='UR140' THEN 
  result := 'UR257';
ELSIF codeOrga='UR500' THEN 
  result := 'UR257';
ELSIF codeOrga='UR290' THEN 
  result := 'UR537';
ELSIF codeOrga='UR350' THEN 
  result := 'UR537';
ELSIF codeOrga='UR220' THEN 
  result := 'UR537';
ELSIF codeOrga='UR560' THEN 
  result := 'UR537';
ELSIF codeOrga='UR080' THEN 
  result := 'UR217';
ELSIF codeOrga='UR520' THEN 
  result := 'UR217';
ELSIF codeOrga='UR510' THEN 
  result := 'UR217';
ELSIF codeOrga='UR100' THEN 
  result := 'UR217';
ELSIF codeOrga='UR110' THEN 
  result := 'UR917';
ELSIF codeOrga='UR300' THEN 
  result := 'UR917';
ELSIF codeOrga='UR660' THEN 
  result := 'UR917';
ELSIF codeOrga='UR340' THEN 
  result := 'UR917';
ELSIF codeOrga='UR230' THEN 
  result := 'UR747';
ELSIF codeOrga='UR870' THEN 
  result := 'UR747';
ELSIF codeOrga='UR190' THEN 
  result := 'UR747';
ELSIF codeOrga='UR550' THEN 
  result := 'UR417';
ELSIF codeOrga='UR880' THEN 
  result := 'UR417';
ELSIF codeOrga='UR570' THEN 
  result := 'UR417';
ELSIF codeOrga='UR540' THEN 
  result := 'UR417';
ELSIF codeOrga='UR590' THEN 
  result := 'UR317';
ELSIF codeOrga='UR629' THEN 
  result := 'UR317';
ELSIF codeOrga='UR800' THEN 
  result := 'UR227';
ELSIF codeOrga='UR600' THEN 
  result := 'UR227';
ELSIF codeOrga='UR020' THEN 
  result := 'UR227';
ELSIF codeOrga='UR160' THEN 
  result := 'UR547';
ELSIF codeOrga='UR170' THEN 
  result := 'UR547';
ELSIF codeOrga='UR790' THEN 
  result := 'UR547';
ELSIF codeOrga='UR860' THEN 
  result := 'UR547';
ELSE
  result = codeOrga;
END IF;
RETURN result;
END;
$$
LANGUAGE 'plpgsql' STRICT;


--
-- Traitement de nce
--

-- Etape 1 : cas général
UPDATE fonds_doc
SET nce_renum = sousRequete.new_reference
FROM (SELECT new_reference, old_cog_avec_ur, old_reference
      FROM regionalisation_coti) AS sousRequete
WHERE (fonds_doc.cog=sousRequete.old_cog_avec_ur) AND (fonds_doc.nce=sousRequete.old_reference); 

-- Etape 2 : Spécifique pour les TGE de Paris
UPDATE fonds_doc
SET nce_renum = sousRequete.new_reference
FROM (SELECT new_reference, old_cog_avec_ur, old_reference
      FROM regionalisation_coti) AS sousRequete
WHERE (fonds_doc.cog='UR750') AND (sousRequete.old_cog_avec_ur IN ('UR968','UR921')) AND (fonds_doc.nce=sousRequete.old_reference); 



--
-- Traitement de nci
--
UPDATE fonds_doc
SET nci_renum = sousRequete.new_reference
FROM (SELECT new_reference, old_cog_avec_ur, old_reference
      FROM regionalisation_cpte) AS sousRequete
WHERE (fonds_doc.cog=sousRequete.old_cog_avec_ur) AND (fonds_doc.nci=sousRequete.old_reference);




--
-- Traitement de npe
--
UPDATE fonds_doc
SET npe_renum = sousRequete.new_reference_sans_zeros_devant
FROM (SELECT new_reference_sans_zeros_devant, old_cog_avec_ur, old_reference_sans_zeros_devant
      FROM regionalisation_pers) AS sousRequete
WHERE (fonds_doc.cog=sousRequete.old_cog_avec_ur) AND (fonds_doc.npe=sousRequete.old_reference_sans_zeros_devant);



--
-- Traitement de cog
--
UPDATE fonds_doc
SET cog_final=transpo_codeOrga(cog);



--
-- Traitement de cop
--
UPDATE fonds_doc
SET cop_final=transpo_codeOrga(cop);



--
-- Remplissage des champs final
--

UPDATE fonds_doc
SET nce_final=nce_renum
WHERE nce_renum IS NOT NULL;

UPDATE fonds_doc
SET nce_final=nce
WHERE nce_final IS NULL;

UPDATE fonds_doc
SET nci_final=nci_renum
WHERE nci_renum IS NOT NULL;

UPDATE fonds_doc
SET nci_final=nci
WHERE nci_final IS NULL;

UPDATE fonds_doc
SET npe_final=npe_renum
WHERE npe_renum IS NOT NULL;

UPDATE fonds_doc
SET npe_final=npe
WHERE npe_final IS NULL;




--
-- Remplissage des champs is_*
--
UPDATE fonds_doc
SET is_cog=1
WHERE cog_final<>cog;

UPDATE fonds_doc
SET is_cop=1
WHERE cop_final<>cop;

UPDATE fonds_doc
SET is_nce=1
WHERE (nce IS NOT NULL) AND (nce_final IS NOT NULL) AND (nce_final<>nce);

UPDATE fonds_doc
SET is_nci=1
WHERE (nci IS NOT NULL) AND (nci_final IS NOT NULL) AND (nci_final<>nci);

UPDATE fonds_doc
SET is_npe=1
WHERE (npe IS NOT NULL) AND (npe_final IS NOT NULL) AND (npe_final<>npe);

UPDATE fonds_doc
SET is_regio=1
WHERE (is_cog=1) OR (is_cop=1) OR (is_nce=1) OR (is_nci=1) OR (is_npe=1);


--
-- Extraction du fichier CSV des documents à traiter
--
COPY (SELECT id_doc,cog,cop,nce,nci,npe,cog_final,cop_final,nce_final,nci_final,npe_final,is_nce,is_nci,is_npe,is_cog,is_cop FROM fonds_doc WHERE is_regio=1) TO '/tmp/docs_a_regio.csv' WITH DELIMITER ';' CSV HEADER;








