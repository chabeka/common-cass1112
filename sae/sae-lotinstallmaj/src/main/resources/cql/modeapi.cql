CREATE TABLE modeapi (
	cfname text PRIMARY KEY, 
	mode text
);

/*
########################  DONNEES ###########################

########################  PILES TRAVAUX ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutions' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecution' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunning' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistory' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstance' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancetojobexecution' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancebyname' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequest' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstep' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsteps' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsqueue' , 'DUAL_MODE_READ_THRIFT');
/*
########################  DROITS ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('droitactionunitaire' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitformatcontrolprofil' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagma' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagm' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmp' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitcontratservice' , 'DUAL_MODE_READ_THRIFT');
/*
########################  TRACES ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('tracedestinataire' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevt' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevtindex' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevtindexdoc' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregexploitation' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregexploitationindex' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregsecurite' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregsecuriteindex' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregtechnique' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregtechniqueindex' , 'DUAL_MODE_READ_THRIFT');

/*
########################  RND ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('rnd' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('correspondancesrnd' , 'DUAL_MODE_READ_THRIFT');
/*
########################  AUTRES ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('parameters' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('dictionary' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('metadata' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('referentielformat' , 'DUAL_MODE_READ_THRIFT');