CREATE TABLE tracedestinataire (
    codeevt text PRIMARY KEY,
    destinataires map<text, frozen<list<text>>>
); 




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

