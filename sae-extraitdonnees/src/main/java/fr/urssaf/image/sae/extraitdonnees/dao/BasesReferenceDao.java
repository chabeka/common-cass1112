package fr.urssaf.image.sae.extraitdonnees.dao;

import com.netflix.astyanax.query.AllRowsQuery;

/**
 * Interface offrant les services concernant la famille de colonne
 * <b>BaseReference</b>
 * 
 */
public interface BasesReferenceDao {

   /**
    * Récupère l'ensemble des lignes
    * 
    * @return la requete permettant de récupérer les lignes
    */
   AllRowsQuery<String, String> getQuery();

}