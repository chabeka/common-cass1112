package fr.urssaf.image.sae.regionalisation.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.regionalisation.bean.Metadata;

/**
 * Service contenant les opérations concernant les métadonnées à modifier
 * 
 * 
 */
public interface MetadataDao {

   /**
    * liste des métadonnées modifiables
    */
   String[] METADATAS = new String[] { "nne", "npe", "den", "cv2", "scv",
         "nci", "nce", "srt", "psi", "nst", "nre", "nic", "dre", "apr", "atr",
         "cop", "cog", "sac", "nbp" };

   /**
    * Récupération d'une map de métadonnées. Seules les métadonnées marquées à
    * traiter sont récupérées.
    * 
    * @param identifiant
    *           identifiant du critère de recherche. Cet identifiant est
    *           référencé dans les enregistrements des métadonnées.
    * @return Map des métadonnées marquées comme à modifier
    */
   Map<String, Object> getMetadatas(BigDecimal identifiant);

   /**
    * Renvoi la liste des métadonnées d'un critère de recherche
    * 
    * @param identifiant
    *           identifiant du critère de recherche
    * @return liste des métadonnées
    */
   Map<String, Object> find(BigDecimal identifiant);

   /**
    * Persistance des métadonnées associées à un critère de recherche
    * 
    * @param idCritere
    *           critère de recherche
    * @param metadatas
    *           liste des métadonnées associées
    */
   void save(BigDecimal idCritere, List<Metadata> metadatas);

   /**
    * Renvoie la liste de toutes les métadonnées associées à un critère de
    * recherche
    * 
    * @param identifiant
    *           critere de recherche
    * @return liste de toutes les métadonnées associées au critère de recherche
    */
   List<Metadata> getAllMetadatas(BigDecimal identifiant);
}
