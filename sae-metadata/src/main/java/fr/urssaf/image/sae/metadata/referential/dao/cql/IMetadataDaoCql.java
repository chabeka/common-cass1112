package fr.urssaf.image.sae.metadata.referential.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;


/**
 * Interface DAO de la colonne famille <code>Metadata</code>
 */

public interface IMetadataDaoCql extends IGenericDAO<MetadataReference, String> {

}
