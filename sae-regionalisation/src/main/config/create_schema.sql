--
-- PostgreSQL database dump
--

-- Dumped from database version 9.0.4
-- Dumped by pg_dump version 9.0.4
-- Started on 2012-07-19 16:27:39

SET statement_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 1816 (class 1262 OID 36503)
-- Name: sae-regionalisation; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "sae-regionalisation" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';


ALTER DATABASE "sae-regionalisation" OWNER TO postgres;

\connect "sae-regionalisation"

SET statement_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 319 (class 2612 OID 11574)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: postgres
--

CREATE OR REPLACE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO postgres;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1512 (class 1259 OID 36518)
-- Dependencies: 5
-- Name: criteres; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE criteres (
    id integer NOT NULL,
    lucene character varying(500) NOT NULL,
    traite boolean
);


ALTER TABLE public.criteres OWNER TO postgres;

--
-- TOC entry 1819 (class 0 OID 0)
-- Dependencies: 1512
-- Name: TABLE criteres; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE criteres IS 'table des critères de recherche';


--
-- TOC entry 1820 (class 0 OID 0)
-- Dependencies: 1512
-- Name: COLUMN criteres.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN criteres.id IS 'identifiant unique du critere de recherche';


--
-- TOC entry 1821 (class 0 OID 0)
-- Dependencies: 1512
-- Name: COLUMN criteres.lucene; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN criteres.lucene IS 'requête LUCENE';


--
-- TOC entry 1822 (class 0 OID 0)
-- Dependencies: 1512
-- Name: COLUMN criteres.traite; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN criteres.traite IS 'indicateur permettant de déterminer si l''enregistrement a été traité ou non';


--
-- TOC entry 1511 (class 1259 OID 36516)
-- Dependencies: 5 1512
-- Name: CRITERES_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "CRITERES_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."CRITERES_ID_seq" OWNER TO postgres;

--
-- TOC entry 1823 (class 0 OID 0)
-- Dependencies: 1511
-- Name: CRITERES_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "CRITERES_ID_seq" OWNED BY criteres.id;


--
-- TOC entry 1514 (class 1259 OID 36530)
-- Dependencies: 5
-- Name: metadonnees; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

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

--
-- TOC entry 1824 (class 0 OID 0)
-- Dependencies: 1514
-- Name: TABLE metadonnees; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE metadonnees IS 'table des métadonnées';


--
-- TOC entry 1825 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.id IS 'identifiant unique de la ligne des métadonnées';


--
-- TOC entry 1826 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.id_critere; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.id_critere IS 'identifiant du critère de recherche rattaché a ces informations (correspond à CRITERES.ID)';


--
-- TOC entry 1827 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nne; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nne IS 'NNI employeur';


--
-- TOC entry 1828 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nne_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nne_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1829 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.npe; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.npe IS 'numéro de personne';


--
-- TOC entry 1830 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.npe_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.npe_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1831 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.den; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.den IS 'dénomination';


--
-- TOC entry 1832 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.den_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.den_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1833 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.cv2; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.cv2 IS 'code catégorie V2';


--
-- TOC entry 1834 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.cv2_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.cv2_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1835 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.scv; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.scv IS 'code sous catégorie 2';


--
-- TOC entry 1836 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.scv_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.scv_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1837 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nci; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nci IS 'numéro de compte interne';


--
-- TOC entry 1838 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nci_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nci_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1839 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nce; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nce IS 'numéro de compte externe';


--
-- TOC entry 1840 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nce_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nce_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1841 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.srt; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.srt IS 'siret';


--
-- TOC entry 1842 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.srt_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.srt_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1843 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.psi; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.psi IS 'pseudo siret';


--
-- TOC entry 1844 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.psi_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.psi_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1845 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nst; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nst IS 'numéro de structure';


--
-- TOC entry 1846 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nst_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nst_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1847 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nre IS 'numéro de recours';


--
-- TOC entry 1848 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nre_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nre_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1849 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nic; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nic IS 'numéro interne de contrôle';


--
-- TOC entry 1850 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nic_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nic_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1851 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.dre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.dre IS 'date de réception';


--
-- TOC entry 1852 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.dre_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.dre_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1853 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.apr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.apr IS 'application productrice';


--
-- TOC entry 1854 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.apr_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.apr_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1855 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.atr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.atr IS 'application traitement';


--
-- TOC entry 1856 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.atr_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.atr_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1857 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.cop; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.cop IS 'code organisme proprietaire';


--
-- TOC entry 1858 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.cop_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.cop_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1859 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.cog; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.cog IS 'code organisme gestionnaire';


--
-- TOC entry 1860 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.cog_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.cog_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1861 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.sac; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.sac IS 'site acquisition';


--
-- TOC entry 1862 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.sac_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.sac_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1863 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nbp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nbp IS 'nombre de pages';


--
-- TOC entry 1864 (class 0 OID 0)
-- Dependencies: 1514
-- Name: COLUMN metadonnees.nbp_flag; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN metadonnees.nbp_flag IS 'indicateur permettant de déterminer si le champ doit être traité ou non';


--
-- TOC entry 1513 (class 1259 OID 36528)
-- Dependencies: 1514 5
-- Name: METADONNEES_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "METADONNEES_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."METADONNEES_ID_seq" OWNER TO postgres;

--
-- TOC entry 1865 (class 0 OID 0)
-- Dependencies: 1513
-- Name: METADONNEES_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "METADONNEES_ID_seq" OWNED BY metadonnees.id;


--
-- TOC entry 1516 (class 1259 OID 36556)
-- Dependencies: 5
-- Name: trace_maj; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE trace_maj (
    id integer NOT NULL,
    id_critere integer NOT NULL,
    id_document character varying(36) NOT NULL,
    nom_metadata character varying(30) NOT NULL,
    ancienne_valeur character varying(50),
    nouvelle_valeur character varying(50)
);


ALTER TABLE public.trace_maj OWNER TO postgres;

--
-- TOC entry 1866 (class 0 OID 0)
-- Dependencies: 1516
-- Name: TABLE trace_maj; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE trace_maj IS 'table de traces des mises à jour';


--
-- TOC entry 1867 (class 0 OID 0)
-- Dependencies: 1516
-- Name: COLUMN trace_maj.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_maj.id IS 'identifiant unique';


--
-- TOC entry 1868 (class 0 OID 0)
-- Dependencies: 1516
-- Name: COLUMN trace_maj.id_critere; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_maj.id_critere IS 'identifiant unique du critère de recherche (correspond à CRITERES.ID)';


--
-- TOC entry 1869 (class 0 OID 0)
-- Dependencies: 1516
-- Name: COLUMN trace_maj.id_document; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_maj.id_document IS 'identifiant du document';


--
-- TOC entry 1870 (class 0 OID 0)
-- Dependencies: 1516
-- Name: COLUMN trace_maj.nom_metadata; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_maj.nom_metadata IS 'nom de la métadonnée';


--
-- TOC entry 1871 (class 0 OID 0)
-- Dependencies: 1516
-- Name: COLUMN trace_maj.ancienne_valeur; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_maj.ancienne_valeur IS 'ancienne valeur de la métadonnée';


--
-- TOC entry 1872 (class 0 OID 0)
-- Dependencies: 1516
-- Name: COLUMN trace_maj.nouvelle_valeur; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_maj.nouvelle_valeur IS 'nouvelle valeur de la métadonnée';


--
-- TOC entry 1515 (class 1259 OID 36554)
-- Dependencies: 5 1516
-- Name: TRACE_MAJ_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "TRACE_MAJ_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."TRACE_MAJ_ID_seq" OWNER TO postgres;

--
-- TOC entry 1873 (class 0 OID 0)
-- Dependencies: 1515
-- Name: TRACE_MAJ_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "TRACE_MAJ_ID_seq" OWNED BY trace_maj.id;


--
-- TOC entry 1518 (class 1259 OID 36569)
-- Dependencies: 1800 5
-- Name: trace_rec; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE trace_rec (
    id integer NOT NULL,
    id_critere integer NOT NULL,
    nbre_doc integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.trace_rec OWNER TO postgres;

--
-- TOC entry 1874 (class 0 OID 0)
-- Dependencies: 1518
-- Name: TABLE trace_rec; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE trace_rec IS 'table de Trace des recherches';


--
-- TOC entry 1875 (class 0 OID 0)
-- Dependencies: 1518
-- Name: COLUMN trace_rec.id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_rec.id IS 'identifiant unique';


--
-- TOC entry 1876 (class 0 OID 0)
-- Dependencies: 1518
-- Name: COLUMN trace_rec.id_critere; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_rec.id_critere IS 'identifiant unique du critère de recherche (correspond à CRITERE.ID)';


--
-- TOC entry 1877 (class 0 OID 0)
-- Dependencies: 1518
-- Name: COLUMN trace_rec.nbre_doc; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN trace_rec.nbre_doc IS 'nombre de documents associés à la recherche';


--
-- TOC entry 1517 (class 1259 OID 36567)
-- Dependencies: 5 1518
-- Name: TRACE_REC_ID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "TRACE_REC_ID_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."TRACE_REC_ID_seq" OWNER TO postgres;

--
-- TOC entry 1878 (class 0 OID 0)
-- Dependencies: 1517
-- Name: TRACE_REC_ID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "TRACE_REC_ID_seq" OWNED BY trace_rec.id;


--
-- TOC entry 1796 (class 2604 OID 36521)
-- Dependencies: 1512 1511 1512
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE criteres ALTER COLUMN id SET DEFAULT nextval('"CRITERES_ID_seq"'::regclass);


--
-- TOC entry 1797 (class 2604 OID 36533)
-- Dependencies: 1514 1513 1514
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE metadonnees ALTER COLUMN id SET DEFAULT nextval('"METADONNEES_ID_seq"'::regclass);


--
-- TOC entry 1798 (class 2604 OID 36559)
-- Dependencies: 1516 1515 1516
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE trace_maj ALTER COLUMN id SET DEFAULT nextval('"TRACE_MAJ_ID_seq"'::regclass);


--
-- TOC entry 1799 (class 2604 OID 36572)
-- Dependencies: 1518 1517 1518
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE trace_rec ALTER COLUMN id SET DEFAULT nextval('"TRACE_REC_ID_seq"'::regclass);


--
-- TOC entry 1802 (class 2606 OID 36527)
-- Dependencies: 1512 1512
-- Name: CRITERES_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY criteres
    ADD CONSTRAINT "CRITERES_pkey" PRIMARY KEY (id);


--
-- TOC entry 1804 (class 2606 OID 36540)
-- Dependencies: 1514 1514
-- Name: METADONNEES_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metadonnees
    ADD CONSTRAINT "METADONNEES_pkey" PRIMARY KEY (id);


--
-- TOC entry 1808 (class 2606 OID 36561)
-- Dependencies: 1516 1516
-- Name: TRACE_MAJ_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trace_maj
    ADD CONSTRAINT "TRACE_MAJ_pkey" PRIMARY KEY (id);


--
-- TOC entry 1810 (class 2606 OID 36575)
-- Dependencies: 1518 1518
-- Name: TRACE_REC_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trace_rec
    ADD CONSTRAINT "TRACE_REC_pkey" PRIMARY KEY (id);


--
-- TOC entry 1806 (class 2606 OID 36585)
-- Dependencies: 1514 1514
-- Name: metadonnees_id_critere_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metadonnees
    ADD CONSTRAINT metadonnees_id_critere_key UNIQUE (id_critere);


--
-- TOC entry 1811 (class 2606 OID 36541)
-- Dependencies: 1512 1514 1801
-- Name: METADONNEES_ID_CRITERE_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metadonnees
    ADD CONSTRAINT "METADONNEES_ID_CRITERE_fkey" FOREIGN KEY (id_critere) REFERENCES criteres(id);


--
-- TOC entry 1812 (class 2606 OID 36562)
-- Dependencies: 1512 1516 1801
-- Name: TRACE_MAJ_ID_CRITERE_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trace_maj
    ADD CONSTRAINT "TRACE_MAJ_ID_CRITERE_fkey" FOREIGN KEY (id_critere) REFERENCES criteres(id);


--
-- TOC entry 1813 (class 2606 OID 36576)
-- Dependencies: 1801 1512 1518
-- Name: TRACE_REC_ID_CRITERE_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trace_rec
    ADD CONSTRAINT "TRACE_REC_ID_CRITERE_fkey" FOREIGN KEY (id_critere) REFERENCES criteres(id);


--
-- TOC entry 1818 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2012-07-19 16:27:42

--
-- PostgreSQL database dump complete
--

