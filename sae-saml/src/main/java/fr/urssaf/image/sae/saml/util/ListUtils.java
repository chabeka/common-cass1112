package fr.urssaf.image.sae.saml.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Classe utilitaires pour la manipulation des {@link java.util.List}<br>
 * <br>
 * la classe s'appuie sur <a
 * href="http://commons.apache.org/collections/">apache collections</a>
 * 
 */
public final class ListUtils {

   private ListUtils() {

   }

   /**
    * filtrage des listes de chaines de caractères des valeurs non renseignées<br>
    * <br>
    * ex: <br>
    * {" ","","bbb",null,"a"} : {"bbb","a"} <br>
    * null : null <br>
    * {}: {}
    * 
    * @param list
    *           liste à filtrer
    * @return instance d'une liste filtrée
    */
   @SuppressWarnings("unchecked")
   public static List<String> filter(List<String> list) {

      List<String> filterList = null;

      if (list != null) {

         filterList = (List<String>) CollectionUtils.select(list,
               new Predicate() {

                  @Override
                  public boolean evaluate(Object object) {

                     return StringUtils.isNotBlank((String) object);
                  }

               });

      }

      return filterList;
   }
}
