package fr.urssaf.image.sae.metadata.referential.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;


/**
 * Interface DAO de la colonne famille <code>Dictionary</code>
 */

public interface IDictionaryDaoCql extends IGenericDAO<Dictionary, String> {

}
