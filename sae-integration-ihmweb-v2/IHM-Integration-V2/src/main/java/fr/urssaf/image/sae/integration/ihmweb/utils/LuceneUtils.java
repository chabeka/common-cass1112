package fr.urssaf.image.sae.integration.ihmweb.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Utilitaires pour le type booleen
 */
public final class LuceneUtils {
   
   private LuceneUtils() {
      
   }
   
   
   /**
    * 
    * Methode permettant de trouver une requete LUCENE dans une liste de requete
    * LUCENE à partir d'un extrait de cette requete (Ex : nom metadonnées,
    * valeur de metadonnées, etc.).
    * 
    * @param listRequeteLucene
    *           Liste de requete LUCENE.
    * @param extraitRequete
    *           Extrait de la requete.
    * @return une requete LUCENE trouver dans une liste de requete.
    */
   public static String trouverRequeteLuceneDansListAvecExtrait(
         List<String> listRequeteLucene, String extraitRequete) {
      
      if (StringUtils.isNotEmpty(extraitRequete)
            && CollectionUtils.isNotEmpty(listRequeteLucene)) {
         for (String requeteLucene : listRequeteLucene) {
            if (StringUtils.isNotEmpty(requeteLucene)
                  && requeteLucene.contains(extraitRequete)) {
               return requeteLucene;
            }
         }
      }
         
      return null;
      
   }
   
   
}
