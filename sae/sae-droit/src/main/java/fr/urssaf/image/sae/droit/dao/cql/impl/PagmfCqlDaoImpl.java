/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.droit.dao.cql.IPagmfDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;

/**
 * (AC75095351) Implémentation du dao cql Pagmf
 */
@Repository
public class PagmfCqlDaoImpl extends GenericDAOImpl<Pagmf, String> implements IPagmfDaoCql {

}
