package fr.urssaf.image.sae.services.capture.validation;

import java.net.URI;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * SAECaptureService
 * 
 * 
 */
@Aspect
public class SAECaptureServiceValidation {

   private static final String CAPTURE_METHOD = "execution(java.util.UUID fr.urssaf.image.sae.services.capture.SAECaptureService.capture(*,*))"
         + "&& args(metadatas,ecdeURL)";

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode capture de l'interface SAECaptureService sont bien correct.
    * 
    * @param metadatas
    *           liste des métadonnées à archiver doit non vide
    * @param ecdeURL
    *           url ECDE du fichier numérique à archiver doit non null
    * 
    */
   @Before(CAPTURE_METHOD)
   public final void capture(List<UntypedMetadata> metadatas, URI ecdeURL) {

      if (ecdeURL == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeURL"));
      }

      if (CollectionUtils.isEmpty(metadatas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "metadatas"));
      }

   }
}
