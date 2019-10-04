/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;

/**
 * (AC75095351) Implémentation du dao cql ContratService
 */
@Repository
public class ContratServiceCqlDaoImpl extends GenericDAOImpl<ServiceContract, String> implements IContratServiceDaoCql {

}
