
/*
##################### PARAMETERS #######################
*/

CREATE TABLE IF NOT EXISTS parameterscql (
	typeParameters text,
	name text,
    value blob,
	PRIMARY KEY (typeParameters,name)
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
