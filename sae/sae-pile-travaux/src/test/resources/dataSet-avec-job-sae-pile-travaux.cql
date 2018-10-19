CREATE TABLE jobhistorycql (
	idjob timeuuid,
	trace map<timeuuid, text>,
	PRIMARY KEY (idjob)
);

CREATE TABLE jobqueuecql (
	key text,
	idjob timeuuid,	
	type text,
	jobparameters map<text, text>,
	PRIMARY KEY (key,idjob)
)WITH CLUSTERING ORDER BY (idjob DESC);
CREATE INDEX job_idx ON jobqueue( idjob );


CREATE TABLE serverhosts (
	hostname		text,
    idjob 			timeuuid,
	PRIMARY KEY (hostname)
);

CREATE TABLE jobrequestcql (
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
);
CREATE INDEX jobRKey_idx ON jobrequest (jobkey);