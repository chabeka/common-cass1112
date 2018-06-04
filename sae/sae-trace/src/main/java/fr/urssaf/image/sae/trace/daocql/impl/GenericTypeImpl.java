/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.model.GenericType;
import fr.urssaf.image.sae.trace.daocql.IGenericType;

/**
 * TODO (AC75095028) Description du type
 * Service permettant l'extraction des données du model thrift en utilisant
 * des requete cql. Ce qui engendre du mapping manuel pour contruire les
 * bean associés aux données extraites
 */
@Service
public class GenericTypeImpl extends GenericDAOImpl<GenericType, UUID> implements IGenericType {

}
