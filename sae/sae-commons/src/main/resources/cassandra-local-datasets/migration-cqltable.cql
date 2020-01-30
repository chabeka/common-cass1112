CREATE TABLE tracedestinatairecql (
    codeevt text PRIMARY KEY,
    destinataires map<text, frozen<list<text>>>
); 


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
);


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
);

CREATE TABLE traceregexploitationcql (
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
);




/*
 #################JOB SPRING ######################
*/

CREATE TABLE jobstepcql (
	jobstepexecutionid	bigint,
    commitcount			int,
    endtime				timestamp,
    executioncontext	blob,
    exitcode			text,
    exitmessage			text,
    filtercount			int,
    jobexecutionid		bigint,
    lastupdated			timestamp,
    name				text,
    processskipcount	int,
    readcount			int,
    readskipcount		int,
    rollbackcount		int,
    starttime			timestamp,
    status				text,
    version				int,
    writecount			int,
    writeskipcount		int,
	PRIMARY KEY (jobstepexecutionid)
);


CREATE TABLE jobstepscql (
	jobstepid		bigint,
    jobname 		text,
    stepname 		text,
	PRIMARY KEY (jobstepid)
);


CREATE TABLE jobexecutioncql (
	jobexecutionid		bigint,
	jobinstanceid		bigint,
	jobName				text,
    creationtime		timestamp,
    executioncontext	blob,
	version 			int,
	starttime			timestamp,
	endtime				timestamp,
	status				text,
    exitcode			text,
    exitmessage			text,
    lastupdated			timestamp,
	PRIMARY KEY (jobexecutionid)
);



CREATE TABLE jobexecutionscql (
	jobname				text,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobname, jobexecutionid)
) WITH CLUSTERING ORDER BY (jobexecutionid DESC);
CREATE INDEX jobExIdKey_idx ON jobexecutionscql (jobexecutionid);


CREATE TABLE jobexecutionsrunningcql (
	jobname				text,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobname, jobexecutionid)
);

CREATE TABLE jobinstancetojobexecutioncql (
	jobinstanceid		bigint,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobinstanceid, jobexecutionid)
)WITH CLUSTERING ORDER BY (jobexecutionid DESC);


CREATE TABLE jobexecutiontojobstepcql (
	jobexecutionid		bigint,
	jobstepid		bigint,
    value 				text,
	PRIMARY KEY (jobexecutionid, jobstepid)
);


CREATE TABLE jobinstancecql (
	jobinstanceid			bigint,
	jobname					text,
    jobparameters 			blob,
	jobkey  				blob,
	version					int,
	reservedby				text,
	PRIMARY KEY (jobinstanceid)
);
CREATE INDEX jobInstKey_idx ON jobinstancecql (jobkey);
CREATE INDEX jobReserveBy_idx ON jobinstancecql (reservedby);


CREATE TABLE jobinstancesBynamecql (
	jobname text,
	jobinstanceid bigint,
	PRIMARY KEY (jobname, jobinstanceid)
)WITH CLUSTERING ORDER BY (jobinstanceid DESC);


/*
##################### PILE DES TRAVAUX #######################
*/
CREATE TABLE jobhistorycql (
	idjob timeuuid,
	trace map<timeuuid, text>,
	PRIMARY KEY (idjob)
);

CREATE TABLE jobqueuecql (
	key text,
	idjob timeuuid,	
	type text,
	jobparameters map<text, text>,
	PRIMARY KEY (key,idjob)
)WITH CLUSTERING ORDER BY (idjob DESC);
CREATE INDEX job_idx ON jobqueuecql( idjob );


CREATE TABLE jobrequestcql (
	idjob timeuuid,
	type text,
	parameters text,
	state text,
	reservedby text,
	creationdate timestamp,
	reservationdate timestamp,
	startingdate timestamp,
	endingdate timestamp,
	message text,
	saehost text,
	clienthost text,
	doccount int,
	doccounttraite int,
	pid int,
	tocheckflag boolean,
	tocheckflagraison text,
	viextrait text,
	jobparameters map<text, text>,
	jobkey blob,
	
	PRIMARY KEY (idjob)
);
CREATE INDEX jobRequestKey_idx ON jobrequestcql (jobkey);

/*
########################  AUTRES TABLES ###########################
*/

CREATE TABLE sequencescql (
	jobidname		text,
    value 			bigint,
	PRIMARY KEY (jobidname)
);

CREATE TABLE modeapi (
	cfname text , 
	mode text,
	PRIMARY KEY(cfname)
);



CREATE TABLE droitactionunitairecql (
	code text,
	description text,
	PRIMARY KEY (code)
);


CREATE  TYPE  controlprofil (
	formatCode text,
	fileFormat text,
	formatIdentification  boolean,
    formatValidation boolean,
    formatValidationMode text
); 

CREATE  TABLE  droitformatcontrolprofilcql (
	formatCode text,
	description  text,
    controlProfil frozen<controlprofil>,
	PRIMARY KEY (formatCode)
); 

CREATE TABLE droitpagmacql ( 
          code text, 
          actionUnitaires List<text>, 
          PRIMARY KEY (code)
);

CREATE TABLE droitpagmpcql (

	code  text,
	description text,
	prmd text,
	PRIMARY KEY (code)
);

CREATE TABLE droitprmdcql ( 
          code text, 
          bean text, 
          description text, 
          lucene text, 
          metadata map<text,frozen<list<text>>>, 
          PRIMARY KEY (code) 
);

CREATE TABLE droitpagmfcql ( 
          codePagmf text, 
          description text, 
          codeFormatControlProfil text, 
          PRIMARY KEY (codePagmf) 
);

CREATE TABLE droitpagmcql ( 
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
);

CREATE TABLE droitcontratservicecql (

	codeClient text,
	description text,
	libelle text,
	listPki list<text>,
	listCert list<text>,
	verifNommage boolean,
	viDuree bigint,
	PRIMARY KEY (codeClient)
);

CREATE TABLE parameterscql (
	typeParameters text,
	name text,
    value blob,
	PRIMARY KEY (typeParameters,name)
);

CREATE TABLE dictionarycql (
	identifiant text,
	listEntries list<text>,
	PRIMARY KEY (identifiant)
);

 CREATE TABLE metadatacql ( 
          longCode text,
          sCode text,
          type text,  
          reqArch boolean, 
          reqStor boolean, 
          length int, 
          pattern text, 
          cons boolean, 
          defCons boolean, 
          search boolean, 
          int boolean, 
          arch  boolean, 
          label text, 
          descr text, 
          hasDict boolean, 
          dictName text, 
          indexed boolean,      
          modif boolean, 
          dispo boolean, 
          leftTrim boolean, 
          rightTrim boolean, 
          transf boolean, 

          PRIMARY KEY (longCode)
);

CREATE TABLE referentielformatcql ( 
          idFormat  text,
          autoriseGED boolean, 
          convertisseur text, 
          description text, 
          extension text, 
          identificateur text, 
          typeMime text, 
          validator text, 
          visualisable boolean, 
          PRIMARY KEY (idFormat) 
);

CREATE TABLE rndcql (
	code text,
	codeFonction text,
	codeActivite text,
	dureeConservation int,
	cloture boolean,
	type text,
	libelleEnd text,
	PRIMARY KEY (code)
);

CREATE TABLE  correspondancesrndcql (
	codeTemporaire text,
	codeDefinitif text,
	versionCourante text,
	etat text,
	dateDebutMaj timestamp,
	dateFinMaj timestamp,
	PRIMARY KEY ((codeTemporaire,versionCourante))
);
