/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.droit.dao.cql.IPagmaDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagma;

/**
 * (AC75095351) Implémentation du dao cql Pagma
 */
@Repository
public class PagmaCqlDaoImpl extends GenericDAOImpl<Pagma, String> implements IPagmaDaoCql {

}
