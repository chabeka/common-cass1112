/**
 *   (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.droit.dao.cql.IPagmpDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;

/**
 * (AC75095351) Implémentation du dao cql Pagmp
 */
@Repository
public class PagmpCqlDaoImpl extends GenericDAOImpl<Pagmp, String> implements IPagmpDaoCql {

}
