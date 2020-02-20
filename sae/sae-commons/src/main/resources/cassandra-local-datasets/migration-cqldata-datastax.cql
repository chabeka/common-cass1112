/*#TABLES DEJA MIGREES*/

CREATE TABLE IF NOT EXISTS modeapi (
	cfname text , 
	mode text,
	PRIMARY KEY(cfname)
);

/*#TRACE#*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevt','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtindex','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecurite','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechnique','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitation','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationindex','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniqueindex','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuriteindex','DATASTAX');

/*#JOB SPRING*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobstep' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsteps' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecution' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutions' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunning' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutiontojobstep' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstance' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesByname' , 'DATASTAX');

/*#PILE TRAVAUX*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistory' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobqueue' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequest' , 'DATASTAX');

/*#DROITS  */
INSERT INTO modeapi(cfname, mode) VALUES ('droitactionunitaire' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitformatcontrolprofil' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagm' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagma' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmp' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmf' , 'DATASTAX');
/*#PARAMETERS */
INSERT INTO modeapi(cfname, mode) VALUES ('parameters' , 'DATASTAX');
/*#METADATA  */
INSERT INTO modeapi(cfname, mode) VALUES ('metadata' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('dictionary' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('referentielformat' , 'DATASTAX');
/*#RND  */
INSERT INTO modeapi(cfname, mode) VALUES ('rnd' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('correspondancesrnd' , 'DATASTAX');


/*#UNIQUEMENT POUR LES TESTS JUNITS*/


/*#DESTINATAIRE*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracedestinataire','DATASTAX');