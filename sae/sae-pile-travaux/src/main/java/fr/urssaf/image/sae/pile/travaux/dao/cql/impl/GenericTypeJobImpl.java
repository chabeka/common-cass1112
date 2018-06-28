/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.pile.travaux.dao.cql.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IGenericTypeJobDao;
import fr.urssaf.image.sae.pile.travaux.model.GenericTypeJob;

/**
 * TODO (AC75095028) Description du type
 * Service permettant l'extraction des données du model thrift en utilisant
 * des requete cql. Ce qui engendre du mapping manuel pour contruire les
 * bean associés aux données extraites
 */
@Service
public class GenericTypeJobImpl extends GenericDAOImpl<GenericTypeJob, UUID> implements IGenericTypeJobDao {

}
