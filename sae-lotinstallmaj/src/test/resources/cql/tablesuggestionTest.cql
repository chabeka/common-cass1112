USE dfce;

/* Ajout d'un nouvelle 
table 
dans la base dfce */


/* Creation de la nouvelle table "suggestion" */
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

/* FIN AJOUT */