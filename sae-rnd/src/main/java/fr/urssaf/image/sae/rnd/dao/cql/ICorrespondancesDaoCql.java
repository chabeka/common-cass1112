package fr.urssaf.image.sae.rnd.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericCompositeDAO;
import fr.urssaf.image.sae.rnd.modele.Correspondance;

/**
 * (AC75095351) Interface pour manipuler la Correspondance en mode Cql
 */
public interface ICorrespondancesDaoCql extends IGenericCompositeDAO<Correspondance, String, String> {

}
