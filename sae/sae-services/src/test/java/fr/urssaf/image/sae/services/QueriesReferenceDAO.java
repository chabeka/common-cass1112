package fr.urssaf.image.sae.services;

import java.util.Map;

import fr.urssaf.image.sae.model.SAEQueryData;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;

/**
 * Fournit des services de manipulation des requêtes pour les tests unitaires.
 */
public interface QueriesReferenceDAO {

   /**
    * @return La liste des métadonnées du référentiel des métadonnées.
    * @throws SAESearchServiceEx
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   Map<String, SAEQueryData> getAllQueries() throws SAESearchServiceEx;

   /**
    * @param queryType
    *           : Type de requêtes.
    * @return Retourne un objet de type {@link SAEQueryData}une requête à partir
    *         du du type de la requête.
    * @throws SAESearchServiceEx
    *            Exception levée lorsqu'un dysfonctionnement survient.
    */
   SAEQueryData getQueryByType(final String queryType)
         throws SAESearchServiceEx;

}
