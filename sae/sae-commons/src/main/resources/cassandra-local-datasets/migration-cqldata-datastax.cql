/*#TABLES DEJA MIGREES*/

CREATE TABLE IF NOT EXISTS modeapi (
	cfname text , 
	mode text,
	PRIMARY KEY(cfname)
);

/*#TRACE#*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevt','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtindex','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecurite','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechnique','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitation','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationindex','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniqueindex','HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuriteindex','HECTOR');

/*#JOB SPRING*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobstep' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsteps' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecution' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutions' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunning' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutiontojobstep' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstance' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesByname' , 'HECTOR');

/*#PILE TRAVAUX*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistory' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobqueue' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequest' , 'HECTOR');

/*#DROITS  */
INSERT INTO modeapi(cfname, mode) VALUES ('droitactionunitaire' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitformatcontrolprofil' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagm' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagma' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmp' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmf' , 'HECTOR');
/*#PARAMETERS */
INSERT INTO modeapi(cfname, mode) VALUES ('parameters' , 'HECTOR');
/*#METADATA  */
INSERT INTO modeapi(cfname, mode) VALUES ('metadata' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('dictionary' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('referentielformat' , 'HECTOR');
/*#RND  */
INSERT INTO modeapi(cfname, mode) VALUES ('rnd' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('correspondancesrnd' , 'HECTOR');





/*#UNIQUEMENT POUR LES TESTS JUNITS*/


/*#DESTINATAIRE*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracedestinataire','HECTOR');