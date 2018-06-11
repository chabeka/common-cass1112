CREATE TABLE tracedestinataire (
    codeevt text PRIMARY KEY,
    destinataires map<text, frozen<list<text>>>
); 

INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('CAPTURE_MASSE|KO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('DFCE_DEPOT_DOC|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('DFCE_MODIF_DOC|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('DFCE_SUPPRESSION_DOC|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('DFCE_TRANSFERT_DOC|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('DFCE_CORBEILLE_DOC|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('DFCE_RESTORE_DOC|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('MAJ_VERSION_RND|OK', {'JOURN_EVT' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('IGC_LOAD_CRLS|KO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('WS_LOAD_CRLS|KO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('WS_LOAD_CRLS|OK', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('WS_TRANSFERT|KO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('ERREUR_IDENT_FORMAT_FICHIER|INFO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('ERREUR_VALID_FORMAT_FICHIER|INFO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('META_VAL_ESPACE|INFO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('SUPPRESSION_MASSE|KO', {'REG_TECHNIQUE' : ['all_infos']});
INSERT INTO tracedestinataire(codeevt, destinataires) VALUES ('RESTORE_MASSE|KO', {'REG_TECHNIQUE' : ['all_infos']});



CREATE TABLE tracejournalevt (
    identifiant timeuuid PRIMARY KEY,
    codeevt text,
    contexte text,
    contratservice text,
    infos map<text, text>,
    login text,
    pagms list<text>,
    timestamp timestamp
);

CREATE TABLE tracejournalevtindexcql (
    identifiantindex text,
    timestamp timestamp,
    identifiant timeuuid,
    codeevt text,
    contexte text,
    contratservice text,
    login text,
    pagms list<text>,
    PRIMARY KEY (identifiantindex, timestamp, identifiant)
);


CREATE TABLE traceregsecurite (
  identifiant timeuuid,
  timestamp timestamp,
  codeEvt text,
  login text,
  contratService text,
  pagms list<text>,
  infos map<text, text>,
  contexte text,
  PRIMARY KEY (identifiant)
);


CREATE TABLE traceregtechnique (
    identifiant timeuuid PRIMARY KEY,
    codeevt text,
    contexte text,
    contratservice text,
    infos map<text, text>,
    login text,
    pagms list<text>,
    timestamp timestamp,
    stacktrace text
);

CREATE TABLE traceregexploitation (
  identifiant timeuuid PRIMARY KEY,
  timestamp timestamp,
  codeevt text,
  login text,
  contratService text,
  pagms list<text>,
  
  action text,
  infos map<text, text>,
);
  
CREATE TABLE traceregexploitationindexcql (
    identifiant timeuuid,
    timestamp timestamp,
    login text,
    codeevt text,
    pagms list<text>,
	
	identifiantindex text,
    action text,
    contrat text,
    PRIMARY KEY (identifiantindex, timestamp, identifiant)
);

CREATE TABLE traceregtechniqueindexcql (
    identifiant timeuuid,
    timestamp timestamp,
    login text,
    codeevt text,
    pagms list<text>,
	
	
    identifiantindex text,
    contexte text,
    contrat text,
    PRIMARY KEY (identifiantindex, timestamp, identifiant)
);

CREATE TABLE traceregsecuriteindexcql (
    identifiant timeuuid,
    timestamp timestamp,
    login text,
    codeevt text,
    pagms list<text>,
	
    identifiantindex text,
    contexte text,
    contrat text,
    PRIMARY KEY (identifiantindex, timestamp, identifiant)
);

