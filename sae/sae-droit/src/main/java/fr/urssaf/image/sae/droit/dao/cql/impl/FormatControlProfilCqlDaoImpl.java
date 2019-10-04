/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.droit.dao.cql.IFormatControlProfilDaoCql;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;

/**
 * (AC75095351) Implémentation du dao cql FormatControlProfil
 */
@Repository
public class FormatControlProfilCqlDaoImpl extends GenericDAOImpl<FormatControlProfil, String> implements IFormatControlProfilDaoCql {

}
