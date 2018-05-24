/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class TraceRegSecuriteIndexCqlDaoImpl extends GenericIndexCqlDaoImpl<TraceRegSecuriteIndexCql, Date> implements ITraceRegSecuriteIndexCqlDao {

}
