/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.dao.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * classe de validation des méthodes de l'interface
 * fr.urssaf.image.sae.regionalisation.dao.TermInfoRangeStringDao
 * 
 */
@Aspect
public class TermInfoRangeStringDaoValidation {

   private static final String CLASS = "fr.urssaf.image.sae.regionalisation.dao.TermInfoRangeStringDao.";

   private static final String FILE_METHOD = "execution(com.netflix.astyanax.query.RowQuery "
         + CLASS + "getQuery(*,*,*))" + "&& args(first, last, indexName)";

   /**
    * 
    * @param first
    *           premier enregistrement
    * @param last
    *           dernier enregistrement
    * @param indexName
    *           nom de l'index
    */
   @Before(FILE_METHOD)
   public final void launchWithFile(String first, String last, String indexName) {

      if (StringUtils.isEmpty(first)) {
         throw new IllegalArgumentException(
               "le paramètre first doit être renseigné");
      }

      if (StringUtils.isEmpty(last)) {
         throw new IllegalArgumentException(
               "le paramètre last doit être renseigné");
      }

      if (StringUtils.isEmpty(indexName)) {
         throw new IllegalArgumentException(
               "le paramètre indexName doit être renseigné");
      }

      if (first.compareTo(last) > 1) {
         throw new IllegalArgumentException(
               "le paramètre first doit être supérieur au paramètre last");
      }

   }

}
