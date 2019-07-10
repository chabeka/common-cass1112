/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.jobspring.dao;

import java.nio.ByteBuffer;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.jobspring.model.GenericJobExecution;

/**
 * TODO (AC75095028) Description du type
 */
public interface IGenericJobExecutionDAO extends IGenericDAO<GenericJobExecution, ByteBuffer> {

}
