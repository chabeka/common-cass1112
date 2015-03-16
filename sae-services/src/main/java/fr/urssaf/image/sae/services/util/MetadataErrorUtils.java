/**
 * 
 */
package fr.urssaf.image.sae.services.util;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;

/**
 * Classe utilitaire permettant de réaliser des opérations sur les
 * {@link MetadataError}
 * 
 */
public final class MetadataErrorUtils {

   private MetadataErrorUtils() {
   }

   /**
    * Construire une list de code long.
    * 
    * @param errorsList
    *           : Liste de de type {@link MetadataError}
    * @return Liste de code long à partir d'une liste de de type
    *         {@link MetadataError}
    */
   public static String buildLongCodeError(List<MetadataError> errorsList) {
      List<String> codeLongErrors = new ArrayList<String>();
      for (MetadataError metadataError : Utils.nullSafeIterable(errorsList)) {
         codeLongErrors.add(metadataError.getLongCode());
      }

      return FormatUtils.formattingDisplayList(codeLongErrors);
   }

}
