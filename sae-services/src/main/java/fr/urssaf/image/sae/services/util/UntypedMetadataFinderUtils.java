package fr.urssaf.image.sae.services.util;

import java.util.List;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;

/**
 * Classe utilitaire pour la recherche des métadonnées à partir d'une liste de
 * UntypedMetadata.
 */
public final class UntypedMetadataFinderUtils {

   /**
    * Permet de récupérer la valeur de la métadonnée dans la liste des
    * métadonnées
    * 
    * @param metadatas
    *           Liste des métadonnées
    * @param longCode
    *           : Le code cherché
    * @return Valeur de la métadonnée cherché.
    */
   public static String valueMetadataFinder(
         final List<UntypedMetadata> metadatas, final String longCode) {
      for (UntypedMetadata metadata : metadatas) {
         if (metadata.getLongCode().equalsIgnoreCase(longCode)) {
            return metadata.getValue();
         }
      }
      return null;
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private UntypedMetadataFinderUtils() {
      assert false;
   }
}
