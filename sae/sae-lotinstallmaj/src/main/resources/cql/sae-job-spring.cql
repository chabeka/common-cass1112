/*
##################### JOBS SPRING #######################
*/


CREATE TABLE jobstepcql (
    jobstepexecutionid	bigint,
    commitcount			int,
    endtime			timestamp,
    executioncontext	        blob,
    exitcode			text,
    exitmessage			text,
    filtercount			int,
    jobexecutionid		bigint,
    lastupdated			timestamp,
    name			text,
    processskipcount	        int,
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
CREATE INDEX jobRKey_idx ON jobinstancecql (jobkey);
CREATE INDEX jobReserveBy_idx ON jobinstancecql (reservedby);


CREATE TABLE jobinstancesBynamecql (
	jobname text,
	jobinstanceid bigint,
	PRIMARY KEY (jobname, jobinstanceid)
)WITH CLUSTERING ORDER BY (jobinstanceid DESC);

CREATE TABLE sequencescql (
	jobidname		text,
    value 			bigint,
	PRIMARY KEY (jobidname)
);
