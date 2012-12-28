/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;

/**
 * Interface offrant les services concernant la famille de colonne
 * <b>DocInfo</b>
 * 
 */
public interface DocInfoDao {

   /**
    * Récupère l'ensemble des documents
    * 
    * @param metas
    *           liste des métadonnées à récupérer
    * 
    * @return la requete permettant de récupérer les documents
    */
   AllRowsQuery<DocInfoKey, String> getQuery(String... metas);

}