CREATE TABLE jobhistory (
	idjob timeuuid,
	trace map<timeuuid, text>,
	PRIMARY KEY (idjob)
);

CREATE TABLE jobqueue (
	idjob timeuuid,
	jobsituation text,	
	type text,
	jobparameters map<text, text>,
	PRIMARY KEY (idjob, jobsituation)
);
CREATE INDEX jobLocation_idx ON jobqueue( jobsituation );

CREATE TABLE jobrequest (
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