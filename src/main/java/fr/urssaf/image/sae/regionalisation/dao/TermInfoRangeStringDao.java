/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.dao;

import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;

/**
 * Interface des opérations relatives à la famille de colonne
 * TermInfoRangeString
 */
public interface TermInfoRangeStringDao {

   
   /**
    * Créé la requête de récupération des index
    * 
    * @param first
    *           premier enregistrement à traiter
    * @param last
    *           dernier enregistrement à traiter
    * @param indexName
    *           nom de l'index concerné
    * @return la requête associée
    */
   RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> getQuery(
         String first, String last, String indexName);

}
