/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.ISequencesDaoCql;
import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class SequencesDaoCqlImpl extends GenericDAOImpl<SequencesCql, String> implements ISequencesDaoCql {

}
