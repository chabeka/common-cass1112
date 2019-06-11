/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.GenericType;
import fr.urssaf.image.sae.trace.daocql.IGenericTypeDao;

/**
 * Cette classe est utilisée juste pour l'extraction des données dans les tables thrift avec une requete cql.
 * La classe est utilisée que pour la migration des données des tables <b>Trace</b>.<br>
 */
@Service
public class GenericTypeImpl extends GenericDAOImpl<GenericType, UUID> implements IGenericTypeDao {

}
