
/*
##################### METADATA #######################
*/

CREATE TABLE dictionarycql (
	identifiant text,
	listEntries list<text>,
	PRIMARY KEY (identifiant)
);

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
);