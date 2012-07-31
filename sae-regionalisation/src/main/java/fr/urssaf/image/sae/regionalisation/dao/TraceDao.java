package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.util.List;

import fr.urssaf.image.sae.regionalisation.bean.Trace;

/**
 * Service contenant les opérations concernant les traces d'exécution.
 * 
 * 
 */
public interface TraceDao {

   /**
    * Ajout d'une trace de modification de métadonnée.
    * 
    * @param trace
    *           informations de trace
    */
   void addTraceMaj(Trace trace);

   /**
    * Ajout d'une trace permettant de connaître le nombre de documents rattachés
    * à un critère de recherche
    * 
    * @param idCriterion
    *           identifiant du critère de recherche
    * @param documentCount
    *           nombre de documents associés au critère de recherche
    * @param maj
    *           <code>true</code> si le mode est MISE_A_JOUR, <code>false</code>
    *           si le mode est TIR_A_BLANC
    */
   void addTraceRec(BigDecimal idCriterion, int documentCount, boolean maj);

   /**
    * Renvoie la liste des traces de mise àn jour pour un critère de recherche
    * 
    * @param idCriterion
    *           identifiant du critère de recherche
    * @return traces de mise à jour
    */
   List<Trace> findTraceMajByCriterion(BigDecimal idCriterion);

   /**
    * Renvoie le nombre de documents associes à la recherche
    * 
    * @param idCriterion
    *           identifiant du critère de recherche
    * @return nombre de documents associés à la recherche
    */
   int findNbreDocs(BigDecimal idCriterion);

}
