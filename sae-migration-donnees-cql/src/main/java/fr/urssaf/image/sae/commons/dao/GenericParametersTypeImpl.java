/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.commons.dao;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.commons.model.GenericParametersType;

/**
 * Service permettant l'extraction des données du model thrift en utilisant
 * des requete cql. Ce qui engendre du mapping manuel pour contruire les
 * bean associés aux données extraites
 */
@Repository
public class GenericParametersTypeImpl extends GenericDAOImpl<GenericParametersType, UUID> implements IGenericParametersTypeDao {

}
