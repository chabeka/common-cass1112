package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.model.JobExecutionCqlForMig;

/**
 * Classe utilisée juste pour l'enregistrement des données dans la table cql de JobExecutionCql.
 */
public interface IJobExecutionCqlForMigDao extends IGenericDAO<JobExecutionCqlForMig, UUID>{


}
