/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;

/**
 * 
 * 
 */
public interface DocInfoDao {

   /**
    * Récupère l'ensemble des documents
    * 
    * @return la requete permettant de récupérer les documents
    */
   AllRowsQuery<DocInfoKey, String> getQuery();

}