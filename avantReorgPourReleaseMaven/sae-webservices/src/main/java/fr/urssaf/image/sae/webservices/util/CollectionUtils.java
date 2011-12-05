package fr.urssaf.image.sae.webservices.util;

import java.util.Collections;
import java.util.List;

/**
 * Classe utilitaire sur les collections
 * 
 * 
 */
public final class CollectionUtils {

   private CollectionUtils() {

   }

   /**
    * si <code>arg</code> renvoie une liste vide, sinon renvoie l'argument
    * 
    * @param <T>
    *           type de la liste
    * @param arg
    *           liste
    * @return liste non null
    */
   public static <T> List<T> loadListNotNull(List<T> arg) {

      List<T> value;

      if (arg == null) {

         value = Collections.emptyList();
      }

      else {
         value = arg;
      }

      return value;
   }

}
