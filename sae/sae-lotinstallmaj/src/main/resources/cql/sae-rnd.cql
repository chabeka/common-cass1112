
/*
##################### RND #######################
*/
CREATE TABLE IF NOT EXISTS rndcql (
	code text,
	codeFonction text,
	codeActivite text,
	dureeConservation int,
	cloture boolean,
	type text,
	libelleEnd text,
	PRIMARY KEY (code)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

CREATE TABLE IF NOT EXISTS correspondancesrndcql (
	codeTemporaire text,
	codeDefinitif text,
	versionCourante text,
	etat text,
	dateDebutMaj timestamp,
	dateFinMaj timestamp,
	PRIMARY KEY ((codeTemporaire,versionCourante))
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
