USE dfce;

alter table action_by_day add errors LIST<TEXT> ;
alter table action_by_day add retry_count INT;
alter table action_by_day add last_retry_date TIMESTAMP;
alter table action_by_day add creation_date TIMESTAMP;

CREATE TABLE suggestion (
  base TEXT,
  metadata TEXT,
  normalized_value_prefix TEXT,
  normalized_value TEXT,
  metadata_value TEXT,
  metadata_suggestions MAP<TEXT, TEXT>,
  PRIMARY KEY ((base, metadata, normalized_value_prefix), normalized_value, metadata_value)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };

CREATE TABLE suggestor (
  id TEXT,
  type TEXT,
  default_active BOOLEAN,
  options MAP<TEXT,TEXT>,
  PRIMARY KEY (id)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };
