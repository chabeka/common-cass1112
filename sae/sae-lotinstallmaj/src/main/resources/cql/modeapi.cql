CREATE TABLE modeapi (
	cfname text PRIMARY KEY, 
	mode text
);

/*
########################  DONNEES ###########################

########################  PILES TRAVAUX ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutions' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecution' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunning' , 'HECTOR');
INSERT INTO modeapi (cfname,mode) VALUES ('jobexecutiontojobstep','HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistory' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstance' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancetojobexecution' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesbyname' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequest' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstep' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsteps' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsqueue' , 'HECTOR');
/*
########################  DROITS ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('droitactionunitaire' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitformatcontrolprofil' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagma' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmf' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagm' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmp' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('droitcontratservice' , 'HECTOR');
/*
########################  TRACES ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('tracedestinataire' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevt' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevtindex' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevtindexdoc' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregexploitation' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregexploitationindex' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregsecurite' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregsecuriteindex' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregtechnique' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregtechniqueindex' , 'HECTOR');

/*
########################  RND ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('rnd' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('correspondancesrnd' , 'HECTOR');
/*
########################  AUTRES ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('parameters' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('dictionary' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('metadata' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('referentielformat' , 'HECTOR');
INSERT INTO modeapi(cfname, mode) VALUES ('sequences' , 'HECTOR');