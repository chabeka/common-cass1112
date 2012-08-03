package fr.urssaf.image.sae.regionalisation.service.validation;

import java.io.File;
import java.text.MessageFormat;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * fr.urssaf.image.sae.regionalisation.service.SearchCriterionService.<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class SearchCriterionServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.regionalisation.service.SearchCriterionService.";

   private static final String METHOD = "execution(void " + CLASS
         + "enregistrerSearchCriterion(*))" + "&& args(searchCriterionCvs)";

   private static final String REQUIRED_EXCEPTION = "Le paramètre ''{0}'' doit être renseigné.";

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.regionalisation.service.SearchCriterionService#enregistrerSearchCriterion(File)}
    * <br>
    * <ul>
    * <li><code>searchCriterionCvs/code> doit être renseigné</li>
    * </ul>
    * 
    * @param searchCriterionCvs
    *           fichier cvs
    */
   @Before(METHOD)
   public final void enregistrerSearchCriterion(File searchCriterionCvs) {

      if (searchCriterionCvs == null) {
         throw new IllegalArgumentException(MessageFormat.format(
               REQUIRED_EXCEPTION, "searchCriterionCvs"));
      }

   }

}
