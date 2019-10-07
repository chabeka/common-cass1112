/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.metadata.referential.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IMetadataDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * (AC75095351) Implémentation du dao cql Metadata
 */
@Repository
public class MetadataCqlDaoImpl extends GenericDAOImpl<MetadataReference, String> implements IMetadataDaoCql {

}
