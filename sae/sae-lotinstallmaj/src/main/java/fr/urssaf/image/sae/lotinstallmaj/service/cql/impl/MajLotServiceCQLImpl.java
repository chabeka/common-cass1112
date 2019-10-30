package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.MajLotService;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.OperationCQL;

@Service
public class MajLotServiceCQLImpl implements MajLotService {

	private static final Logger LOG = LoggerFactory.getLogger(MajLotServiceCQLImpl.class);
	
	/*
	// SAE
	public static final String SAE_MODE_API = "SAE_CREATE_MODE_API";
	public static final String SAE_MIG_TRACES = "SAE_CREATE_TRACE";
	public static final String SAE_MIG_JOB_SPRING = "SAE_CREATE_JOB_SPRING";
	public static final String SAE_MIG_PILE_TRAVAUX = "SAE_CREATE_PILE_TRAVAUX";
	// SAE DELETE TABLE MIG
	public static final String SAE_DELETE_MODE_API = "SAE_DELETE_MODE_API";
	public static final String SAE_DELETE_MIG_TRACES = "SAE_DELETE_TRACES";
	public static final String SAE_DELETE_MIG_JOB_SPRING = "SAE_DELETE_JOB_SPRING";
	public static final String SAE_DELETE_MIG_PILE_TRAVAUX = "SAE_DELETE_PILE_TRAVAUX";
	// DFCE
	public static final String DFCE_192_TO_200_SCHEMA = "DFCE_192_TO_200_SCHEMA";
	public static final String DFCE_200_TO_210_SCHEMA = "DFCE_200_TO_210_SCHEMA";
	public static final String DFCE_210_TO_230_SCHEMA = "DFCE_210_TO_230_SCHEMA";
	public static final String DFCE_230_TO_192_SCHEMA = "DFCE_230_TO_192_SCHEMA";
	*/
	
	@Autowired
	private SAECassandraUpdaterCQL saeUpdater;
	
	@Autowired
	private DFCECassandraUpdaterCQL dfceUpdater;
	
	@Override
	public void demarre(String nomOperation, String[] argSpecifiques) {
				
		// DFCE
		
		if (OperationCQL.DFCE_192_TO_200_SCHEMA.getNomOp().equalsIgnoreCase(nomOperation)) {
			
			updateDFCE192TO200();
			
		} else if (OperationCQL.DFCE_200_TO_210_SCHEMA.getNomOp().equalsIgnoreCase(nomOperation)){
			
			updateDFCE200TO210();
			
		} else if (OperationCQL.DFCE_210_TO_230_SCHEMA.getNomOp().equalsIgnoreCase(nomOperation)){
			//
			updateDFCE210TO230();
			
		} else if (OperationCQL.DFCE_230_TO_192_SCHEMA.getNomOp().equalsIgnoreCase(nomOperation)){
			
			updateDFCE230TO192();
			
		}
		
		// SAE
		
		else if (OperationCQL.SAE_MIG_TRACES.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.createTablesTraces();
			
		}else if (OperationCQL.SAE_MIG_PILE_TRAVAUX.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.createTablesPileTravaux();
			
		}else if (OperationCQL.SAE_MIG_JOB_SPRING.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.createTablesJobSpring();
			
		}else if (OperationCQL.SAE_MODE_API.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.createTablesModeapi();
			
		}else if (OperationCQL.SAE_DELETE_MODE_API.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.deleteTablesModeapi();
			
		}else if (OperationCQL.SAE_DELETE_MIG_TRACES.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.deleteTablesTraces();
			
		}else if (OperationCQL.SAE_DELETE_MIG_JOB_SPRING.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.deleteTablesJobSpring();;
			
		}else if (OperationCQL.SAE_DELETE_MIG_PILE_TRAVAUX.getNomOp().equalsIgnoreCase(nomOperation)){
			
			saeUpdater.deleteTablesPilesTravaux();
			
		}else {
			
		  // Opération inconnue => log + exception runtime
		  String message = String.format("Erreur technique : L'opération %s est inconnue", nomOperation);
		  LOG.error(message);
		  throw new MajLotRuntimeException(message);
		}
		
	}
	
	@Override
	public void demarreCreateMetadatasIndexesDroitsSAE(String applicationConcernee) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void demarreCreateSAE() {
		LOG.debug("Démarrage des opérations de création de la base SAE");
	    this.createGedBase();
	    LOG.debug("Opérations de création terminées sur la base SAE");
		
	}	
	
	
	private void updateDFCE192TO200() {
		LOG.info("Début de l'opération : mise à jour du keyspace DFCE pour le lot 192TO200");
		// Récupération de la chaîne de connexion au cluster cassandra
		dfceUpdater.update192ToVersion200();
		LOG.info("Fin de l'opération : mise à jour du keyspace DFCE");
	}

	private void updateDFCE200TO210() {
		LOG.info("Début de l'opération : mise à jour du keyspace DFCE pour le lot 200TO210");
		// Récupération de la chaîne de connexion au cluster cassandra
		dfceUpdater.update200ToVersion210();
		LOG.info("Fin de l'opération : mise à jour du keyspace DFCE");
	}

	private void updateDFCE210TO230() {
		LOG.info("Début de l'opération : mise à jour du keyspace DFCE pour le lot 210TO230");
		// Récupération de la chaîne de connexion au cluster cassandra
		dfceUpdater.update210ToVersion230();
		LOG.info("Fin de l'opération : mise à jour du keyspace DFCE");
	}
	
	private void updateDFCE230TO192() {
		LOG.info("Début de l'opération : mise à jour du keyspace DFCE pour le lot 230TO192");
		// Récupération de la chaîne de connexion au cluster cassandra
		dfceUpdater.update230ToVersion192();
		LOG.info("Fin de l'opération : mise à jour du keyspace DFCE");
	}
	
	private void createGedBase() {
		LOG.info("Début de l'opération : creation des tables cql SAE ");
		
		saeUpdater.createTablesModeapi();
		saeUpdater.createTablesTraces();
		saeUpdater.createTablesPileTravaux();
		saeUpdater.createTablesJobSpring();
		
		LOG.info("Fin de l'opération : creation des tables cql SAE");
	}
}
