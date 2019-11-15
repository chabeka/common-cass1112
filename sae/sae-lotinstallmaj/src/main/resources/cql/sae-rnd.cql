
/*
##################### RND #######################
*/
CREATE TABLE rndcql (
	code text,
	codeFonction text,
	codeActivite text,
	dureeConservation int,
	cloture boolean,
	type text,
	libelleEnd text,
	PRIMARY KEY (code)
);

CREATE TABLE  correspondancesrndcql (
	codeTemporaire text,
	codeDefinitif text,
	versionCourante text,
	etat text,
	dateDebutMaj timestamp,
	dateFinMaj timestamp,
	PRIMARY KEY ((codeTemporaire,versionCourante))
);
