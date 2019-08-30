package fr.urssaf.image.sae.services.controles.validation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;


/**
 * Classe de validation des arguments en entrée des implémentations du service
 * SAEControlesCaptureService
 * 
 * 
 */
@Aspect
public class SAEControlesCaptureServiceValidation {
   
   
   private static final String CAPTURE_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.controles.SAEControlesCaptureService.checkUntypedBinaryDocument(*))"
      + "&& args(untypedDocument)";

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode checkUntypedBinaryDocument de l'interface SAEControlesCaptureService sont bien correct.
    * 
    * @param untypedDocument
    *           document à traiter
    * 
    */
   @Before(CAPTURE_CONTROLES_METHOD)
   public final void checkUntypedBinaryDocument(UntypedDocument untypedDocument) {
   
      if (untypedDocument == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "untypedDocument"));
      }
   }

}
