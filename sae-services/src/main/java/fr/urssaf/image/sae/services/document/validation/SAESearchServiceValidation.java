package fr.urssaf.image.sae.services.document.validation;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe SAESearchServiceValidation
 * 
 * Classe de validation des arguments en entrée des implementations du service
 * SAESearchService
 * 
 */
@Aspect
public class SAESearchServiceValidation {
   
   private static final String SAESEARCHCLASS = "fr.urssaf.image.sae.services.document.SAESearchService.";
   private static final String PARAM_SEARCH = "execution(java.util.List<fr.urssaf.image.sae.bo.model.untyped.UntypedDocument> "+SAESEARCHCLASS+"search(*,*))" +
                                                  "&& args(requete,listMetaDesired)";
   
   /**
    * Vérifie les paramètres d'entrée de la méthode recherche de l'interface {@link fr.urssaf.image.sae.services.document.SAESearchService} sont bien corrects.
    * 
    * @param requete : Lucene requête.
    * @param listMetaDesired : Liste métadonnée souhaitée
    */
   @Before(PARAM_SEARCH)
   public final void search(String requete, List<String> listMetaDesired) {
      Validate.notEmpty(requete,ResourceMessagesUtils.loadMessage("argument.required", "'Requete lucene'"));
      Validate.notNull(listMetaDesired,ResourceMessagesUtils.loadMessage("argument.required", "'Liste desirée'"));
   }
}
