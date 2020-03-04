
/*
##################### METADATA #######################
*/

CREATE TABLE dictionarycql (
	identifiant text,
	listEntries list<text>,
	PRIMARY KEY (identifiant)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};

 CREATE TABLE metadatacql ( 
          longCode text,
          sCode text,
          type text,  
          reqArch boolean, 
          reqStor boolean, 
          length int, 
          pattern text, 
          cons boolean, 
          defCons boolean, 
          search boolean, 
          int boolean, 
          arch  boolean, 
          label text, 
          descr text, 
          hasDict boolean, 
          dictName text, 
          indexed boolean,      
          modif boolean, 
          dispo boolean, 
          leftTrim boolean, 
          rightTrim boolean, 
          transf boolean, 

          PRIMARY KEY (longCode)
)WITH gc_grace_seconds = 1728000 AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'};