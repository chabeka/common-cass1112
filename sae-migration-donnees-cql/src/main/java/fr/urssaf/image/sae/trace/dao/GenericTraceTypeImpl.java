/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.model.GenericTraceType;

/**
 * Cette classe est utilisée juste pour l'extraction des données dans les tables thrift avec une requete cql.
 * La classe est utilisée que pour la migration des données des tables <b>Trace</b>.<br>
 */
@Service
public class GenericTraceTypeImpl extends GenericDAOImpl<GenericTraceType, UUID> implements IGenericTraceTypeDao {

}
