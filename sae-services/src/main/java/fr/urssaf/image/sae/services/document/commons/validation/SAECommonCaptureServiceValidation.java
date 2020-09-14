package fr.urssaf.image.sae.services.document.commons.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe SAECommonCaptureServiceValidation
 * Classe de validation des arguments en entrée des implementations du service
 * SAECommonCaptureService
 */
@Aspect
@Component
public class SAECommonCaptureServiceValidation {

  private static final String SAE_COMMON_CAPTURE_CLASS = "fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService.";

  private static final String PARAM_SEARCH = "execution(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument "
      + SAE_COMMON_CAPTURE_CLASS
      + "buildBinaryStorageDocumentForCapture(*,*))"
      + "&& args(untypedDocument,captureResult)";

  /**
   * Vérifie les paramètres d'entrée de la méthode
   * buildBinaryStorageDocumentForCapture<br>
   * de l'interface SAECommonCaptureService sont bien corrects.
   * 
   * @param untypedDocument
   *          : representant un document à archiver.
   * @param captureResult
   *          : résultat de la capture
   */
  @Before(PARAM_SEARCH)
  public final void buildBinaryStorageDocumentForCapture(
                                                         final UntypedDocument untypedDocument, final CaptureResult captureResult) {
    Validate.notNull(untypedDocument,
                     ResourceMessagesUtils.loadMessage(
                                                       "argument.required",
                                                       "'untypedDocument'"));
  }

}
