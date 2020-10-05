USE dfce;

/* Ajout de nouvelle 
colonnes 
dans la table action_by_day */

// ajout de la colonne errors
alter table action_by_day add errors LIST<TEXT> ;

-- ajout de la colonne retry_count
alter table action_by_day add retry_count INT;

-- ajout de la colonne last_retry_date
alter table action_by_day add last_retry_date TIMESTAMP;

// ajout de la colonne creation_date
alter table action_by_day add creation_date TIMESTAMP;

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

/* 
Creation 
de la nouvelle 
table 
"suggestor" 
*/
CREATE TABLE suggestor (
  id TEXT,
  type TEXT,
  default_active BOOLEAN,
  options MAP<TEXT,TEXT>,
  PRIMARY KEY (id)
)
WITH COMPACTION = { 'class': 'LeveledCompactionStrategy', 'sstable_size_in_mb' : 256 };