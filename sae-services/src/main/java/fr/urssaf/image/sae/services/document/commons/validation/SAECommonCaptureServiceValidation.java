package fr.urssaf.image.sae.services.document.commons.validation;

import org.apache.commons.lang.Validate;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;


/**
 * Classe SAECommonCaptureServiceValidation
 * 
 * Classe de validation des arguments en entrée des implementations du service
 * SAECommonCaptureService
 * 
 */
@Aspect
public class SAECommonCaptureServiceValidation {

   private static final String SAE_COMMON_CAPTURE_CLASS = "fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService.";
   private static final String PARAM_SEARCH = "execution(fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument "+SAE_COMMON_CAPTURE_CLASS+"buildBinaryStorageDocumentForCapture(*))" +
                                                  "&& args(untypedDocument)";
   
   /**
    * Vérifie les paramètres d'entrée de la méthode buildBinaryStorageDocumentForCapture<br>
    * de l'interface {@link SAECommonCaptureService} sont bien corrects.
    * 
    * @param untypedDocument : representant un document à archiver.
    */
   @Before(PARAM_SEARCH)
   public final void buildBinaryStorageDocumentForCapture(UntypedDocument untypedDocument) {
      Validate.notNull(untypedDocument,ResourceMessagesUtils.loadMessage("argument.required", "'untypedDocument'"));
   }
   
}
