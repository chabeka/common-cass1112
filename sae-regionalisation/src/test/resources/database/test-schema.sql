CREATE TABLE criteres (
    id serial NOT NULL,
    lucene character varying(500) NOT NULL,
    traite boolean
);

CREATE TABLE metadonnees (
    id serial NOT NULL,
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
    nbp_flag boolean,
    CONSTRAINT metadonnees_id_critere_key UNIQUE (id_critere)
);

CREATE TABLE trace_maj (
    id serial NOT NULL,
    id_critere integer NOT NULL,
    id_document character varying(36) NOT NULL,
    nom_metadata character varying(30) NOT NULL,
    ancienne_valeur character varying(50),
    nouvelle_valeur character varying(50)
);

CREATE TABLE trace_rec (
    id serial NOT NULL,
    id_critere integer NOT NULL,
    nbre_doc integer DEFAULT 0 NOT NULL
);
