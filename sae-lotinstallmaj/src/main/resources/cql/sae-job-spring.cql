/*
##################### JOBS SPRING #######################
*/


CREATE TABLE IF NOT EXISTS jobstepcql (
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
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE INDEX IF NOT EXISTS jobExecutionId_jobstepcql_idx ON jobstepcql (jobexecutionid);

CREATE TABLE IF NOT EXISTS jobstepscql (
	jobstepid		bigint,
    jobname 		text,
    stepname 		text,
	PRIMARY KEY (jobstepid)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


CREATE TABLE IF NOT EXISTS jobexecutioncql (
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
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};



CREATE TABLE IF NOT EXISTS jobexecutionscql (
	jobname				text,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobname, jobexecutionid)
)WITH CLUSTERING ORDER BY (jobexecutionid DESC) AND gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
CREATE INDEX IF NOT EXISTS jobExIdKey_jobexecutionscql_idx ON jobexecutionscql (jobexecutionid);


CREATE TABLE IF NOT EXISTS jobexecutionsrunningcql (
	jobname				text,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobname, jobexecutionid)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS jobinstancetojobexecutioncql (
	jobinstanceid		bigint,
	jobexecutionid		bigint,
    value 				text,
	PRIMARY KEY (jobinstanceid, jobexecutionid)
)WITH CLUSTERING ORDER BY (jobexecutionid DESC) AND gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


CREATE TABLE IF NOT EXISTS jobexecutiontojobstepcql (
	jobexecutionid		bigint,
	jobstepid		bigint,
    value 				text,
	PRIMARY KEY (jobexecutionid, jobstepid)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};


CREATE TABLE IF NOT EXISTS jobinstancecql (
	jobinstanceid			bigint,
	jobname					text,
    jobparameters 			blob,
	jobkey  				blob,
	version					int,
	reservedby				text,
	PRIMARY KEY (jobinstanceid)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
CREATE INDEX IF NOT EXISTS jobRKey_jobinstancecql_idx ON jobinstancecql (jobkey);
CREATE INDEX IF NOT EXISTS jobReserveBy_jobinstancecql_idx ON jobinstancecql (reservedby);
CREATE INDEX IF NOT EXISTS jobname_jobinstancecql_idx ON jobinstancecql (jobname);


CREATE TABLE IF NOT EXISTS jobinstancesBynamecql (
	jobname text,
	jobinstanceid bigint,
	PRIMARY KEY (jobname, jobinstanceid)
)WITH CLUSTERING ORDER BY (jobinstanceid DESC) AND gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS sequencescql (
	jobidname		text,
    value 			bigint,
	PRIMARY KEY (jobidname)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
