package fr.urssaf.image.sae.vi.util;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Classe utilitaire pour filtrer les collections
 * 
 * 
 */
public final class FilterUtils {

   private FilterUtils() {

   }

   /**
    * Filtre les collections en supprimant les elements null
    * 
    * @param <T>
    *           type des elements de la collection
    * @param list
    *           collection à filtrer
    * @return collection filtrée
    */
   @SuppressWarnings("unchecked")
   public static <T> Collection<T> filter(Collection<T> list) {

      Collection<T> filterList = null;

      if (list != null) {

         filterList = CollectionUtils.select(list, new Predicate() {

            @Override
            public boolean evaluate(Object object) {

               return object != null;
            }

         });

      }

      return filterList;
   }

}
