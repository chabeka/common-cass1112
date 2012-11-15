/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao;

import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidColumn;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidKey;

/**
 * Interface fournissant les services concernant <b>TermInfoRangeUuid</b>
 * 
 */
public interface TermInfoRangeUuidDao {

   /**
    * Récupère l'ensemble des colonnes de la ligne concernant <b>SM_UUID</b>
    * 
    * @return la requête de recherche des Uuid
    */
   RowQuery<TermInfoRangeUuidKey, TermInfoRangeUuidColumn> getAllUuidColumns();

}
