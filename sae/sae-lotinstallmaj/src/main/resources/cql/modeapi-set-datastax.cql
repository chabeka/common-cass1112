CREATE TABLE IF NOT EXISTS modeapi (
	cfname text PRIMARY KEY, 
	mode text
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

/*
########################  DONNEES ###########################

########################  PILES TRAVAUX ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutions' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecution' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunning' , 'DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('jobexecutiontojobstep','DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistory' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstance' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancetojobexecution' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesbyname' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequest' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstep' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsteps' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobsqueue' , 'DATASTAX');
/*
########################  DROITS ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('droitactionunitaire' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitformatcontrolprofil' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagma' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmf' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagm' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmp' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmd' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('droitcontratservice' , 'DATASTAX');
/*
########################  TRACES ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('tracedestinataire' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevt' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevtindex' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('tracejournalevtindexdoc' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregexploitation' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregexploitationindex' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregsecurite' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregsecuriteindex' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregtechnique' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('traceregtechniqueindex' , 'DATASTAX');

/*
########################  RND ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('rnd' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('correspondancesrnd' , 'DATASTAX');
/*
########################  AUTRES ###########################
*/
INSERT INTO modeapi(cfname, mode) VALUES ('parameters' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('dictionary' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('metadata' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('referentielformat' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('sequences' , 'DATASTAX');