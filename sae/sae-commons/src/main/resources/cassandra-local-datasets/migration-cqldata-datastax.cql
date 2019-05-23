/*#TABLES DEJA MIGREES*/

/*#TRACE#*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracedestinatairecql','DATASTAX'); 
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtcql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtindexcql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuritecql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniquecql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationcql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationindexcql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniqueindexcql','DATASTAX');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuriteindexcql','DATASTAX');

/*#JOB SPRING*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobstepcql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstepscql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutioncql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionscql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunningcql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutiontojobstepcql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancecql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesBynamecql' , 'DATASTAX');

/*#PILE TRAVAUX*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistorycql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobqueuecql' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequestcql' , 'DATASTAX');


/*#UNIQUEMENT POUR LES TESTS JUNITS*/


/*#DESTINATAIRE*/
INSERT INTO modeapi(cfname, mode) VALUES ('tracedestinataire' , 'DATASTAX');