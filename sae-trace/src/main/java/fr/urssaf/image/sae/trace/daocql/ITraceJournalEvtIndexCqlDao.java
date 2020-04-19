/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql;

import java.util.Iterator;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;

/**
 * Interface DAO de {@link TraceJournalEvtIndexCql}
 * 
 * @param <TraceJournalEvtIndexCql>
 *          Type de d'objet contenue dans le registre
 * @param <String>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceJournalEvtIndexCqlDao extends IGenericDAO<TraceJournalEvtIndexCql, String> {
  public Iterator<TraceJournalEvtIndexCql> IterableFindById(final String journee, final boolean ordreInverse);
}
