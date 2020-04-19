
/*
##################### FORMAT #######################
*/
CREATE TABLE IF NOT EXISTS referentielformatcql ( 
          idFormat  text,
          autoriseGED boolean, 
          convertisseur text, 
          description text, 
          extension text, 
          identificateur text, 
          typeMime text, 
          validator text, 
          visualisable boolean, 
          PRIMARY KEY (idFormat) 
) WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};
