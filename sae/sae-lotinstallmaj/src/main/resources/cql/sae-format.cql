
/*
##################### FORMAT #######################
*/
CREATE TABLE referentielformatcql ( 
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
);
