/*
##################### TRACES #######################
*/

CREATE TABLE tracedestinatairecql (
    codeevt text PRIMARY KEY,
    destinataires map<text, frozen<list<text>>>
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


CREATE TABLE tracejournalevtcql (
    identifiant timeuuid,
    timestamp timestamp,
    codeevt text,
    contexte text,
    contratservice text,
    infos map<text, text>,
    login text,
    pagms list<text>,
    PRIMARY KEY (identifiant, timestamp)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


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
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE tracejournalevtindexdoccql (
	identifiant timeuuid,
    timestamp timestamp,
    login text,
    codeevt text,
    pagms list<text>,

	identifiantindex uuid,
    contexte text,
    contratservice text,
	infos map<text,text>,

    PRIMARY KEY (identifiantindex,timestamp, identifiant)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE traceregsecuritecql (
  identifiant timeuuid,
  timestamp timestamp,
  codeEvt text,
  login text,
  contratService text,
  pagms list<text>,
  infos map<text, text>,
  contexte text,
  PRIMARY KEY (identifiant)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


CREATE TABLE traceregtechniquecql (
    identifiant timeuuid PRIMARY KEY,
    codeevt text,
    contexte text,
    contratservice text,
    infos map<text, text>,
    login text,
    pagms list<text>,
    timestamp timestamp,
    stacktrace text
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE traceregexploitationcql (
  identifiant timeuuid PRIMARY KEY,
  timestamp timestamp,
  codeevt text,
  login text,
  contratService text,
  pagms list<text>,
  
  action text,
  infos map<text, text>,
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
  
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
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

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
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

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
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

