package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileLoader;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileSet;

@Component
public class SAECassandraUpdaterCQL {

	private static final Logger LOG = LoggerFactory.getLogger(SAECassandraUpdaterCQL.class);
	
	private static final String SAE_CREATE_TRACES_SCRIPT = "/cql/sae-traces.cql";
	private static final String SAE_CREATE_JOB_SPRING_SCRIPT = "/cql/sae-job-spring.cql";
	private static final String SAE_CREATE_PILE_TRAVAUX_SCRIPT = "/cql/sae-pile-travaux.cql";
	private static final String SAE_CREATE_MODE_API_SCRIPT = "/cql/modeapi.cql";
	
	// Script de delete
	private static final String SAE_DELETE_MODE_API_SCRIPT = "DROP TABLE modeapi;";
	private static final String SAE_DELETE_TABLES_TRACES_SCRIPT = "/cql/delete-tables-traces.cql";
	private static final String SAE_DELETE_TABLES_JOBSPRING_SCRIPT = "/cql/delete-tables-jobspring.cql";
	private static final String SAE_DELETE_TABLES_PILESTRAVAUX_SCRIPT = "/cql/delete-tables-piletravaux.cql";
	
	@Autowired
	private SAEKeyspaceConnecter saecf;
	
	
	/**
	* Creation des tables de traces
	*/
    public final void createTablesTraces() {
    	LOG.info("Début de l'opération : creation des tables traces cql");
    		
    	CQLDataFileSet cqlData;
    	//
		cqlData = new CQLDataFileSet(SAE_CREATE_TRACES_SCRIPT);
		CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
		dataLoader.load(cqlData);
	   
    	LOG.info("Fin de l'opération : creation des tables traces cql");
    }
    
	/**
	* Creation des tables pile des travaux
	*/
    public final void createTablesPileTravaux() {
    	LOG.info("Début de l'opération : creation des tables pile travaux cql");
    	
    	CQLDataFileSet cqlData;
    	//
		cqlData = new CQLDataFileSet(SAE_CREATE_PILE_TRAVAUX_SCRIPT);
		CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
		dataLoader.load(cqlData);	
	   
    	LOG.info("Fin de l'opération : creation des tables pile travaux cql");
    }
    
	/**
	* Creation des tables job spring
	*/
    public final void createTablesJobSpring() {
       LOG.info("Début de l'opération : creation des tables job spring cql");
    	
	   CQLDataFileSet cqlData;
	   //
	   cqlData = new CQLDataFileSet(SAE_CREATE_JOB_SPRING_SCRIPT);
	   CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
	   dataLoader.load(cqlData);
	   
	   LOG.info("Fin de l'opération : creation des tables job spring cql");
    }
    
    /**
	* Creation des tables job spring
	*/
    public final void createTablesModeapi() {
       LOG.info("Début de l'opération : creation de la  table modeapi cql");
    	
	   
	   CQLDataFileSet cqlData;
	   //
	   cqlData = new CQLDataFileSet(SAE_CREATE_MODE_API_SCRIPT);
	   CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
	   dataLoader.load(cqlData);
	   
	   LOG.info("Fin de l'opération : creation de la table modeapi cql");
    }
    
    /**
	* Suppression de la table modeapi 
	*/
    public final void deleteTablesModeapi() {
       LOG.info("Début de l'opération : suppression de la  table modeapi cql");
    	
	   String query = SAE_DELETE_MODE_API_SCRIPT;
	   saecf.getSession().execute(query);
	   
	   LOG.info("Fin de l'opération : suppression de la table modeapi cql");
    }
    
    /**
	* Suppression des tables traces 
	*/
    public final void deleteTablesTraces() {
       LOG.info("Début de l'opération : suppression des tables traces");
    	
	   
	   CQLDataFileSet cqlData;
	   //
	   cqlData = new CQLDataFileSet(SAE_DELETE_TABLES_TRACES_SCRIPT);
	   CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
	   dataLoader.load(cqlData);
	   
	   LOG.info("Fin de l'opération : suppression des tables traces");
    }
    
    /**
	* Suppression des tables job spring 
	*/
    public final void deleteTablesJobSpring() {
       LOG.info("Début de l'opération : suppression des tables job spring");
    	
	   
	   CQLDataFileSet cqlData;
	   //
	   cqlData = new CQLDataFileSet(SAE_DELETE_TABLES_JOBSPRING_SCRIPT);
	   CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
	   dataLoader.load(cqlData);
	   
	   LOG.info("Fin de l'opération : suppression des tables job spring");
    }
    
    /**
	* Suppression des tables job spring 
	*/
    public final void deleteTablesPilesTravaux() {
       LOG.info("Début de l'opération : suppression des tables pile travaux");
    	
	   
	   CQLDataFileSet cqlData;
	   //
	   cqlData = new CQLDataFileSet(SAE_DELETE_TABLES_PILESTRAVAUX_SCRIPT);
	   CQLDataFileLoader dataLoader = new CQLDataFileLoader(saecf.getSession());
	   dataLoader.load(cqlData);
	   
	   LOG.info("Fin de l'opération : suppression des tables pile travaux");
    }
}
