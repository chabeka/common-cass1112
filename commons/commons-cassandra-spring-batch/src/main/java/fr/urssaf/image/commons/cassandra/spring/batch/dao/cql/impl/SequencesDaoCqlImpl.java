/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.ISequencesDaoCql;

/**
 * 
 */
@Repository
public class SequencesDaoCqlImpl extends GenericDAOImpl<SequencesCql, String> implements ISequencesDaoCql {

	private static final Logger LOGGER = LoggerFactory.getLogger(SequencesDaoCqlImpl.class);
	
	/**
    * @return the logger
    */
   @Override
   public Logger getLogger() {
      return LOGGER;
   }
}
