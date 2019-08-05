package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileLoader;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileSet;

@Component
public class DFCECassandraUpdaterCQL {

   private static final Logger LOG = LoggerFactory.getLogger(DFCECassandraUpdaterCQL.class);
   
   private static final String DFCE_192_TO_200_SCHEMA = "/cql/dfce-1.9.2-TO-2.0.0-schema.cql";
   private static final String DFCE_200_TO_210_SCHEMA = "/cql/dfce-2.0.0-TO-2.1.0-schema.cql";
   private static final String DFCE_210_TO_230_SCHEMA = "/cql/dfce-2.1.0-TO-2.3.0-schema.cql";
   private static final String DFCE_230_TO_192_SCHEMA = "/cql/dfce-2.3.0-TO-1.9.2-schema.cql";
   
   @Autowired
   private DFCEKeyspaceConnecter dfcecf;
 
	
   /**
    * Mise à jour vers la version 200
    */
   public final void update192ToVersion200() {
	   
	   CQLDataFileSet cqlData;
	   //
	   cqlData = new CQLDataFileSet(DFCE_192_TO_200_SCHEMA);
	   CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
	   dataLoader.load(cqlData);	
   }
   /**
    * Mise à jour vers la version 210
    */
   public final void update200ToVersion210() {

	   	CQLDataFileSet cqlData;
		//
		cqlData = new CQLDataFileSet(DFCE_200_TO_210_SCHEMA);
		CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
		dataLoader.load(cqlData);
   }
   /**
    * Mise à jour vers la version 230
    */
   public final void update210ToVersion230() {

	   	CQLDataFileSet cqlData;
		//
		cqlData = new CQLDataFileSet(DFCE_210_TO_230_SCHEMA);
		CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
		dataLoader.load(cqlData);
   }
   
   /**
    * Retour arrière de la version 2.3.1 à la version 1.9.2
    */
   public final void update230ToVersion192() {

	   	CQLDataFileSet cqlData;
		cqlData = new CQLDataFileSet(DFCE_230_TO_192_SCHEMA);
		CQLDataFileLoader dataLoader = new CQLDataFileLoader(dfcecf.getSession());
		dataLoader.load(cqlData);
   }
   
}
