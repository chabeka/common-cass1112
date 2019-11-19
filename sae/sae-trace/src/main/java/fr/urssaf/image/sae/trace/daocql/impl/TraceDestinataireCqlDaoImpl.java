/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceDestinataireCqlDao} de la famille de colonnes {@link TraceDestinataire}
 * 
 * @param <TraceDestinataire>
 *          Type de d'objet contenue dans le registre
 * @param <String>
 *          Le type d'Identifiant de l'objet
 */
@Repository
public class TraceDestinataireCqlDaoImpl extends GenericDAOImpl<TraceDestinataire, String> implements ITraceDestinataireCqlDao {

}
