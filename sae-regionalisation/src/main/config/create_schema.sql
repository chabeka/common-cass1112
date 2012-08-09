-- DROP DATABASE regionalisation;

CREATE DATABASE "regionalisation" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';

ALTER DATABASE "sae-regionalisation" OWNER TO postgres;

SET statement_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;


CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;


CREATE TABLE criteres (
    id integer NOT NULL,
    lucene character varying(500) NOT NULL,
    traite boolean
);


ALTER TABLE public.criteres OWNER TO postgres;


COMMENT ON TABLE criteres IS 'table des critères de recherche';


COMMENT ON COLUMN criteres.id IS 'identifiant unique du critere de recherche';


COMMENT ON COLUMN criteres.lucene IS 'requête LUCENE';


COMMENT ON COLUMN criteres.traite IS 'indicateur permettant de déterminer si l''enregistrement a été traité ou non';


CREATE SEQUENCE "CRITERES_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."CRITERES_ID_seq" OWNER TO postgres;


ALTER SEQUENCE "CRITERES_ID_seq" OWNED BY criteres.id;


CREATE TABLE metadonnees (
    id integer NOT NULL,
    id_critere integer,
    nne character varying(15),
    nne_flag boolean,
    npe character varying(7),
    npe_flag boolean,
    den character varying(500),
    den_flag boolean,
    cv2 character varying(1),
    cv2_flag boolean,
    scv character varying(2),
    scv_flag boolean,
    nci character varying(7),
    nci_flag boolean,
    nce character varying(18),
    nce_flag boolean,
    srt character varying(14),
    srt_flag boolean,
    psi character varying(14),
    psi_flag boolean,
    nst character varying(10),
    nst_flag boolean,
    nre character varying(15),
    nre_flag boolean,
    nic character varying(6),
    nic_flag boolean,
    dre date,
    dre_flag boolean,
    apr character varying(15),
    apr_flag boolean,
    atr character varying(15),
    atr_flag boolean,
    cop character varying(5),
    cop_flag boolean,
    cog character varying(5),
    cog_flag boolean,
    sac character varying(5),
    sac_flag boolean,
    nbp integer,
    nbp_flag boolean
);


ALTER TABLE public.metadonnees OWNER TO postgres;


COMMENT ON TABLE metadonnees IS 'table des métadonnées';


COMMENT ON COLUMN metadonnees.id IS 'identifiant unique de la ligne des métadonnées';


COMMENT ON COLUMN metadonnees.id_critere IS 'identifiant du critère de recherche rattaché a ces informations (correspond à CRITERES.ID)';


COMMENT ON COLUMN metadonnees.nne IS 'NNI employeur';


COMMENT ON COLUMN metadonnees.nne_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.npe IS 'numéro de personne';


COMMENT ON COLUMN metadonnees.npe_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.den IS 'dénomination';


COMMENT ON COLUMN metadonnees.den_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.cv2 IS 'code catégorie V2';


COMMENT ON COLUMN metadonnees.cv2_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.scv IS 'code sous catégorie 2';


COMMENT ON COLUMN metadonnees.scv_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.nci IS 'numéro de compte interne';


COMMENT ON COLUMN metadonnees.nci_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.nce IS 'numéro de compte externe';


COMMENT ON COLUMN metadonnees.nce_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.srt IS 'siret';


COMMENT ON COLUMN metadonnees.srt_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.psi IS 'pseudo siret';


COMMENT ON COLUMN metadonnees.psi_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.nst IS 'numéro de structure';


COMMENT ON COLUMN metadonnees.nst_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.nre IS 'numéro de recours';


COMMENT ON COLUMN metadonnees.nre_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.nic IS 'numéro interne de contrôle';


COMMENT ON COLUMN metadonnees.nic_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.dre IS 'date de réception';


COMMENT ON COLUMN metadonnees.dre_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.apr IS 'application productrice';


COMMENT ON COLUMN metadonnees.apr_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.atr IS 'application traitement';


COMMENT ON COLUMN metadonnees.atr_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.cop IS 'code organisme proprietaire';


COMMENT ON COLUMN metadonnees.cop_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.cog IS 'code organisme gestionnaire';


COMMENT ON COLUMN metadonnees.cog_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.sac IS 'site acquisition';


COMMENT ON COLUMN metadonnees.sac_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


COMMENT ON COLUMN metadonnees.nbp IS 'nombre de pages';


COMMENT ON COLUMN metadonnees.nbp_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


CREATE SEQUENCE "METADONNEES_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."METADONNEES_ID_seq" OWNER TO postgres;


ALTER SEQUENCE "METADONNEES_ID_seq" OWNED BY metadonnees.id;


CREATE TABLE trace_maj (
    id integer NOT NULL,
    id_critere integer NOT NULL,
    id_document character varying(36) NOT NULL,
    nom_metadata character varying(30) NOT NULL,
    ancienne_valeur character varying(50),
    nouvelle_valeur character varying(50)
);


ALTER TABLE public.trace_maj OWNER TO postgres;


COMMENT ON TABLE trace_maj IS 'table de traces des mises à jour';


COMMENT ON COLUMN trace_maj.id IS 'identifiant unique';


COMMENT ON COLUMN trace_maj.id_critere IS 'identifiant unique du critère de recherche (correspond à CRITERES.ID)';


COMMENT ON COLUMN trace_maj.id_document IS 'identifiant du document';


COMMENT ON COLUMN trace_maj.nom_metadata IS 'nom de la métadonnée';


COMMENT ON COLUMN trace_maj.ancienne_valeur IS 'ancienne valeur de la métadonnée';


COMMENT ON COLUMN trace_maj.nouvelle_valeur IS 'nouvelle valeur de la métadonnée';


CREATE SEQUENCE "TRACE_MAJ_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."TRACE_MAJ_ID_seq" OWNER TO postgres;


ALTER SEQUENCE "TRACE_MAJ_ID_seq" OWNED BY trace_maj.id;


CREATE TABLE trace_rec (
    id integer NOT NULL,
    id_critere integer NOT NULL,
    nbre_doc integer DEFAULT 0 NOT NULL,
    maj boolean NOT NULL
);


ALTER TABLE public.trace_rec OWNER TO postgres;


COMMENT ON TABLE trace_rec IS 'table de Trace des recherches';


COMMENT ON COLUMN trace_rec.id IS 'identifiant unique';


COMMENT ON COLUMN trace_rec.id_critere IS 'identifiant unique du critère de recherche (correspond à CRITERE.ID)';


COMMENT ON COLUMN trace_rec.nbre_doc IS 'nombre de documents associés à la recherche';


CREATE SEQUENCE "TRACE_REC_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."TRACE_REC_ID_seq" OWNER TO postgres;


ALTER SEQUENCE "TRACE_REC_ID_seq" OWNED BY trace_rec.id;


ALTER TABLE criteres ALTER COLUMN id SET DEFAULT nextval('"CRITERES_ID_seq"'::regclass);


ALTER TABLE metadonnees ALTER COLUMN id SET DEFAULT nextval('"METADONNEES_ID_seq"'::regclass);


ALTER TABLE trace_maj ALTER COLUMN id SET DEFAULT nextval('"TRACE_MAJ_ID_seq"'::regclass);


ALTER TABLE trace_rec ALTER COLUMN id SET DEFAULT nextval('"TRACE_REC_ID_seq"'::regclass);


ALTER TABLE ONLY criteres
    ADD CONSTRAINT "CRITERES_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY metadonnees
    ADD CONSTRAINT "METADONNEES_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY trace_maj
    ADD CONSTRAINT "TRACE_MAJ_pkey" PRIMARY KEY (id);



ALTER TABLE ONLY trace_rec
    ADD CONSTRAINT "TRACE_REC_pkey" PRIMARY KEY (id);


ALTER TABLE ONLY metadonnees
    ADD CONSTRAINT metadonnees_id_critere_key UNIQUE (id_critere);


ALTER TABLE ONLY metadonnees
    ADD CONSTRAINT "METADONNEES_ID_CRITERE_fkey" FOREIGN KEY (id_critere) REFERENCES criteres(id);


ALTER TABLE ONLY trace_maj
    ADD CONSTRAINT "TRACE_MAJ_ID_CRITERE_fkey" FOREIGN KEY (id_critere) REFERENCES criteres(id);


ALTER TABLE ONLY trace_rec
    ADD CONSTRAINT "TRACE_REC_ID_CRITERE_fkey" FOREIGN KEY (id_critere) REFERENCES criteres(id);


REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;