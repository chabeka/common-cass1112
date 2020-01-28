
/*
##################### PARAMETERS #######################
*/

CREATE TABLE parameterscql (
	typeParameters text,
	name text,
    value blob,
	PRIMARY KEY (typeParameters,name)
);