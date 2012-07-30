package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.util.List;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;

/**
 * DAO contenant les opérations concernant les critères de recherche
 * 
 * 
 */
public interface SearchCriterionDao {

   /**
    * Récupération d'une liste de requêtes;
    * 
    * @param firstRecord
    *           numéro du premier enregistrement à traiter
    * @param recordCount
    *           nombre d'enregistrement à récupérer
    * @return liste des critères de recherche
    */
   List<SearchCriterion> getSearchCriteria(int firstRecord, int recordCount);

   /**
    * Mise à jour du critère de recherche
    * 
    * @param identifiant
    *           identifiant du critère de recherche
    */
   void updateSearchCriterion(BigDecimal identifiant);

   /**
    * Renvoi un critère de recherche
    * 
    * @param identifiant
    *           identifiant du critère de recherche
    * @return critère de recherche
    */
   SearchCriterion find(BigDecimal identifiant);

   /**
    * Persistance d'un critère de recherche
    * 
    * @param searchCriterion
    *           critère de recherche à créer
    */
   void save(SearchCriterion searchCriterion);
}
