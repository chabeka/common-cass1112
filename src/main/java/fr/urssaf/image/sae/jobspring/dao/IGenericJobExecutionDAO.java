package fr.urssaf.image.sae.jobspring.dao;

import java.nio.ByteBuffer;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.jobspring.model.GenericJobExecution;

/**
 * (AC75095028) Interface pour la manipulation de JobExecution
 */
public interface IGenericJobExecutionDAO extends IGenericDAO<GenericJobExecution, ByteBuffer> {

}
