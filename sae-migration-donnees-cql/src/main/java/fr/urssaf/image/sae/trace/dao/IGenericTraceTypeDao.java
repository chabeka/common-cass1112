/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
//import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.model.GenericTraceType;

/**
 * Cette classe est utilisée juste pour l'extraction des données dans les tables thrift avec une requete cql.
 * La classe est utilisée que pour la migration des données des tables <b>Trace</b>.<br>
 */
public interface IGenericTraceTypeDao extends IGenericDAO<GenericTraceType, UUID> {

}
