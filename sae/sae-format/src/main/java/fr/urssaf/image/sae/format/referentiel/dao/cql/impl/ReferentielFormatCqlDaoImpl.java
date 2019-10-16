/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.format.referentiel.dao.cql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.format.referentiel.dao.cql.IReferentielFormatDaoCql;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * (AC75095351) Implémentation du dao cql ReferentielFormat
 */
@Repository
public class ReferentielFormatCqlDaoImpl extends GenericDAOImpl<FormatFichier, String> implements IReferentielFormatDaoCql {

}
