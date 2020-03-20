
/*
##################### PILE DES TRAVAUX #######################
*/
CREATE TABLE IF NOT EXISTS jobhistorycql (
	idjob timeuuid,
	trace map<timeuuid, text>,
	PRIMARY KEY (idjob)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS jobqueuecql (
	key text,
	idjob timeuuid,	
	type text,
	jobparameters map<text, text>,
	PRIMARY KEY (key,idjob)
)WITH CLUSTERING ORDER BY (idjob DESC) AND  gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};;
CREATE INDEX IF NOT EXISTS job_jobqueuecql_idx ON jobqueuecql( idjob );


CREATE TABLE IF NOT EXISTS jobrequestcql (
	idjob timeuuid,
	type text,
	parameters text,
	state text,
	reservedby text,
	creationdate timestamp,
	reservationdate timestamp,
	startingdate timestamp,
	endingdate timestamp,
	message text,
	saehost text,
	clienthost text,
	doccount int,
	doccounttraite int,
	pid int,
	tocheckflag boolean,
	tocheckflagraison text,
	viextrait text,
	jobparameters map<text, text>,
	jobkey blob,
	
	PRIMARY KEY (idjob)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
CREATE INDEX IF NOT EXISTS jobRequestKey_jobrequestcql_idx ON jobrequestcql (jobkey);