package fr.urssaf.image.sae.services.batch.suppression.support.lucene.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Validation des paramètres passés en arguments des implémentations de
 * {@link fr.urssaf.image.sae.services.batch.suppression.support.lucene.RequeteLuceneValidationSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class RequeteLuceneValidationSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String VALID_REQUETE_LUCENE_METHOD = "execution(String fr.urssaf.image.sae.services.batch.suppression.support.lucene.RequeteLuceneValidationSupport.validationRequeteLucene(*))"
         + " && args(requeteLucene)";
   
   private static final String VERIF_DROIT_METHOD = "execution(String fr.urssaf.image.sae.services.batch.suppression.support.lucene.RequeteLuceneValidationSupport.verificationDroitRequeteLucene(*))"
         + " && args(requeteLucene)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * validationRequeteLucene possède tous les arguments renseignés
    * 
    * @param requeteLucene
    *           Requete lucene de suppression
    */
   @Before(VALID_REQUETE_LUCENE_METHOD)
   public final void checkValidationRequeteLucene(final String requeteLucene) {

      if (StringUtils.isEmpty(requeteLucene)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "requeteLucene"));
      }
   }
   
   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * verificationDroitRequeteLucene possède tous les arguments renseignés
    * 
    * @param requeteLucene
    *           Requete lucene de suppression
    */
   @Before(VERIF_DROIT_METHOD)
   public final void checkVerificationDroitRequeteLucene(final String requeteLucene) {

      if (StringUtils.isEmpty(requeteLucene)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "requeteLucene"));
      }

   }
}
