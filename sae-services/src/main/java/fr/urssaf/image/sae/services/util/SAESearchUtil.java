package fr.urssaf.image.sae.services.util;

import org.apache.commons.lang.StringUtils;

/**
 * Fonctions utilitaires pour le service de recherche
 */
public final class SAESearchUtil {

   private SAESearchUtil() {
      // constructeur privé
   }

   /**
    * Trim la requête Lucene client.<br>
    * Conserve l'espace de fin de la requête s'il est échappé (ex. de requête:
    * "meta:valeur\ ")
    * 
    * @param requeteClient
    *           la requête client
    * @return la requête trimmée
    */
   public static String trimRequeteClient(String requeteClient) {

      String requeteTrim = StringUtils.trim(requeteClient);

      // Cas particulier des requêtes se terminant par un espace échappé
      // exemple: "meta:valeur\ "
      // pour lesquelles il faut garder l'espace final
      if (StringUtils.isNotBlank(requeteTrim) && requeteTrim.endsWith("\\")
            && requeteClient.endsWith(" ")) {
         requeteTrim += " ";
      }

      return requeteTrim;

   }

}
