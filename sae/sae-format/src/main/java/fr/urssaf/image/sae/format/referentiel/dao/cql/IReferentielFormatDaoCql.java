package fr.urssaf.image.sae.format.referentiel.dao.cql;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;


/**
 * Interface DAO de la colonne famille <code>RefrentielFormat</code>
 */

public interface IReferentielFormatDaoCql extends IGenericDAO<FormatFichier, String> {

}
