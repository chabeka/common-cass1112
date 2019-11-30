/*#TABLES DEJA MIGREES*/

/*#TRACE#*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtcql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtindexcql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuritecql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniquecql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationcql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationindexcql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniqueindexcql','DUAL_MODE');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuriteindexcql','DUAL_MODE');

/*#JOB SPRING*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobstepcql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstepscql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutioncql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionscql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunningcql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutiontojobstepcql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancecql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesBynamecql' , 'DUAL_MODE');

/*#PILE TRAVAUX*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistorycql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobqueuecql' , 'DUAL_MODE');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequestcql' , 'DUAL_MODE');


/*#UNIQUEMENT POUR LES TESTS JUNITS*/


/*#DESTINATAIRE*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracedestinatairecql','DUAL_MODE');