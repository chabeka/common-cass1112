/*
##################### DROITS #######################
*/

CREATE TABLE IF NOT EXISTS droitactionunitairecql (
	code text,
	description text,
	PRIMARY KEY (code)
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TYPE IF NOT EXISTS controlprofil (
	formatCode text,
	fileFormat text,
	formatIdentification  boolean,
    formatValidation boolean,
    formatValidationMode text
);

CREATE  TABLE IF NOT EXISTS droitformatcontrolprofilcql (
	formatCode text,
	description  text,
    controlProfil frozen<controlprofil>,
	PRIMARY KEY (formatCode)
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS droitpagmacql ( 
          code text, 
          actionUnitaires List<text>, 
          PRIMARY KEY (code)
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


CREATE TABLE IF NOT EXISTS droitpagmpcql (

	code  text,
	description text,
	prmd text,
	PRIMARY KEY (code)
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS droitprmdcql ( 
          code text, 
          bean text, 
          description text, 
          lucene text, 
          metadata map<text,frozen<list<text>>>, 
          PRIMARY KEY (code) 
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS droitpagmfcql ( 
          codePagmf text, 
          description text, 
          codeFormatControlProfil text, 
          PRIMARY KEY (codePagmf) 
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS droitpagmcql ( 
		  idClient text,
		  code text,
          description text, 
          pagma text, 
          pagmp text, 
          pagmf text, 
          parametres map<text,text>, 
          compressionPdfActive boolean, 
          seuilCompressionPdf int, 
          PRIMARY KEY (idClient,code) 
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS droitcontratservicecql (

	codeClient text,
	description text,
	libelle text,
	listPki list<text>,
	listCert list<text>,
	verifNommage boolean,
	viDuree bigint,
	PRIMARY KEY (codeClient)
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
