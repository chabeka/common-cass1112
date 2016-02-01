package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.UUID;

import javax.activation.DataHandler;

import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.storage.dfce.annotations.Loggable;
import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.messages.LogLevel;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.StorageDocumentServiceSupport;
import fr.urssaf.image.sae.storage.exception.StorageDocAttachmentServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentAttachment;
import fr.urssaf.image.sae.storage.services.storagedocument.DocumentAttachmentService;

/**
 * Implémente les services de l'interface {@link DocumentAttachmentService}.
 * 
 */
@Service
@Qualifier("documentAttachmentService")
public class DocumentAttachmentServiceImpl extends AbstractServices implements
      DocumentAttachmentService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocumentAttachmentServiceImpl.class);

   /**
    * Service utilitaire de mutualisation du code des implémentations des
    * services DFCE
    */
   @Autowired
   private StorageDocumentServiceSupport storageDocumentServiceSupport;

   private static final String TRC_DOC_ATTACH_INSERT = "addDocumentAttachment()";
   private static final String TRC_DOC_ATTACH_GET = "addDocumentAttachment()";

   /**
    * {@inheritDoc}
    */
   @Override
   @ServiceChecked
   @Loggable(LogLevel.TRACE)
   public final void addDocumentAttachment(UUID docUuid, String docName,
         String extension, DataHandler contenu)
         throws StorageDocAttachmentServiceEx {

      LOGGER.debug("{} - Début ajout du document attaché",
            TRC_DOC_ATTACH_INSERT);
      storageDocumentServiceSupport.addDocumentAttachment(getDfceService(),
            getCnxParameters(), docUuid, docName, extension, contenu, LOGGER);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   @ServiceChecked
   @Loggable(LogLevel.TRACE)
   public final StorageDocumentAttachment getDocumentAttachments(UUID docUuid)
         throws StorageDocAttachmentServiceEx {
      LOGGER.debug("{} - Début récupération du document attaché",
            TRC_DOC_ATTACH_GET);

      return storageDocumentServiceSupport.getDocumentAttachment(
            getDfceService(), getCnxParameters(), docUuid, LOGGER);

   }
   
   /**
    * {@inheritDoc}
    */
   public final <T> void setDocumentAttachmentServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);
   }

}
