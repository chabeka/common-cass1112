
CREATE TABLE jobstep (
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


CREATE TABLE jobsteps (
	jobstepid		bigint,
    jobname 		text,
    stepname 		text,
	PRIMARY KEY (jobstepid)
);


CREATE TABLE jobexecution (
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
	PRIMARY KEY (jobexecutionid,creationtime)
)WITH CLUSTERING ORDER BY (creationtime DESC);



CREATE TABLE jobexecutions (
	jobname				text,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobname, jobexecutionid)
) WITH CLUSTERING ORDER BY (jobexecutionid DESC);


CREATE TABLE jobexecutionsrunning (
	jobname				text,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobname, jobexecutionid)
);

CREATE TABLE jobinstancetojobexecution (
	jobinstanceid		bigint,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobinstanceid, jobexecutionid)
)WITH CLUSTERING ORDER BY (jobexecutionid DESC);


CREATE TABLE jobexecutiontojobstep (
	jobexecutionid		bigint,
	jobstepid		bigint,
    value 				text,
	PRIMARY KEY (jobexecutionid, jobstepid)
);


CREATE TABLE sequences (
	jobidname		text,
    value 			bigint,
	PRIMARY KEY (jobidname)
);

CREATE TABLE jobinstance (
	jobinstanceid			bigint,
	jobname					text,
    jobparameters 			blob,
	jobkey  				blob,
	version					int,
	reservedby				text,
	PRIMARY KEY (jobinstanceid)
);
CREATE INDEX jobRKey_idx ON jobinstance (jobkey);


CREATE TABLE jobinstancesByname (
	jobname text,
	jobinstanceid bigint,
	reservedBy text,
	PRIMARY KEY (jobname, jobinstanceid)
)WITH CLUSTERING ORDER BY (jobinstanceid DESC);


CREATE TABLE modeapi (
	cfname text PRIMARY KEY, 
	mode text
);
INSERT INTO modeapi(cfname, mode) VALUES ('jobexecutions' , 'DATASTAX');
INSERT INTO modeapi(cfname, mode) VALUES ('jobstep' , 'HECTOR');
