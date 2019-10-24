/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.commons.dao;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.commons.model.GenericParametersType;

/**
 * Cette classe est utilisée juste pour l'extraction des données dans les tables thrift avec une requete cql.
 * La classe est utilisée que pour la migration des données des tables <b>Parameters</b>.<br>
 */
public interface IGenericParametersTypeDao extends IGenericDAO<GenericParametersType, UUID> {

}
