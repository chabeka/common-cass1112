/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;

/**
 * Interface DAO de {@link TraceDestinataire}
 * 
 * @param <TraceDestinataire>
 *          Type de d'objet contenue dans le registre
 * @param <String>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceDestinataireCqlDao extends IGenericDAO<TraceDestinataire, String> {

}
