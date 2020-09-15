/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.rnd.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * (AC75095351) Interface pour manipuler le TypeDocument (CF RndCql) en mode Cql
 */
public interface IRndDaoCql extends IGenericDAO<TypeDocument, String> {

}
