/*#TABLES DEJA MIGREES*/

/*#TRACE#*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtcql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('tracejournalevtindexcql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuritecql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniquecql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationcql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregexploitationindexcql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregtechniqueindexcql','DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuriteindexcql','DUAL_MODE_READ_THRIFT');

/*#JOB SPRING*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobstepcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstepscql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutioncql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionscql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutionsrunningcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutiontojobstepcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancecql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobinstancesBynamecql' , 'DUAL_MODE_READ_THRIFT');

/*#PILE TRAVAUX*/
INSERT INTO modeapi(cfname, mode) VALUES ('jobhistorycql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobqueuecql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('jobrequestcql' , 'DUAL_MODE_READ_THRIFT');

/*#DROITS  (EC)*/
INSERT INTO modeapi(cfname, mode) VALUES ('droitactionunitairecql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitformatcontrolprofilcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmacql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmpcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmdcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmdcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitprmdcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('droitpagmfcql' , 'DUAL_MODE_READ_THRIFT');
/*#PARAMETERS  (EC)*/
INSERT INTO modeapi(cfname, mode) VALUES ('parameterscql' , 'DUAL_MODE_READ_THRIFT');
/*#METADATA  (EC)*/
INSERT INTO modeapi(cfname, mode) VALUES ('metadatacql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('dictionarycql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('referentielformatcql' , 'DUAL_MODE_READ_THRIFT');
/*#RND  (EC)*/
INSERT INTO modeapi(cfname, mode) VALUES ('rndcql' , 'DUAL_MODE_READ_THRIFT');
INSERT INTO modeapi(cfname, mode) VALUES ('correspondancesrndcql' , 'DUAL_MODE_READ_THRIFT');





/*#UNIQUEMENT POUR LES TESTS JUNITS*/


/*#DESTINATAIRE*/
INSERT INTO modeapi (cfname,mode) VALUES ('tracedestinatairecql','DUAL_MODE_READ_THRIFT');