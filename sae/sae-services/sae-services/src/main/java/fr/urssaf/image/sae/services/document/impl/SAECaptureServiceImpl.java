package fr.urssaf.image.sae.services.document.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import fr.urssaf.image.sae.exception.SAECaptureServiceEx;
import fr.urssaf.image.sae.model.UntypedDocument;
import fr.urssaf.image.sae.services.document.SAECaptureService;
import fr.urssaf.image.sae.storage.dfce.contants.Constants;
import fr.urssaf.image.sae.storage.dfce.messages.MessageHandler;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;

/**
 * Fournit l'implémentation des services :<br>
 * <lu> <li>Capture unitaire.</li> <br>
 * <li>Capture en masse.</li> </lu>
 * 
 * @author akenore,rhofir.
 */
@Service
@Qualifier("saeCaptureService")
public class SAECaptureServiceImpl extends AbstractSAEServices implements
      SAECaptureService {

   /**
    * {@inheritDoc}
    */
   public final void bulkCapture(String urlEcde) throws SAECaptureServiceEx {
      try {
         getStorageServiceProvider().setStorageServiceProviderParameter(
               getStorageConnectionParameter());
         getStorageServiceProvider().getStorageConnectionService()
               .openConnection();
         getStorageServiceProvider().getStorageDocumentService()
               .bulkInsertStorageDocument(new StorageDocuments(), true);
      } catch (ConnectionServiceEx except) {
         throw new SAECaptureServiceEx(MessageHandler
               .getMessage(Constants.CNT_CODE_ERROR), except.getMessage(),
               except);
      } catch (InsertionServiceEx except) {
         throw new SAECaptureServiceEx(MessageHandler
               .getMessage(Constants.CNT_CODE_ERROR), except.getMessage(),
               except);
      } finally {
         getStorageServiceProvider().getStorageConnectionService()
               .closeConnexion();
      }
   }

   /**
    * {@inheritDoc}
    */
   public final String capture(UntypedDocument unTypedDoc)
         throws SAECaptureServiceEx {
      // TODO Auto-generated method stub
      return null;
   }
}
