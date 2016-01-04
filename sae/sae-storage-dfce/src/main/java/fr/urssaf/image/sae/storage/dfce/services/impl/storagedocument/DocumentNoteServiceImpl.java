package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import fr.urssaf.image.sae.storage.exception.DocumentNoteServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocumentNote;
import fr.urssaf.image.sae.storage.services.storagedocument.DocumentNoteService;

/**
 * Implémente les services de l'interface {@link DocumentNoteService}.
 * 
 */
@Service
@Qualifier("documentNoteService")
public class DocumentNoteServiceImpl extends AbstractServices implements
      DocumentNoteService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocumentNoteServiceImpl.class);

   /**
    * Service utilitaire de mutualisation du code des implémentations des
    * services DFCE
    */
   @Autowired
   private StorageDocumentServiceSupport storageDocumentServiceSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   @ServiceChecked
   @Loggable(LogLevel.TRACE)
   public void addDocumentNote(UUID docUuid, String contenu, String login, Date dateCreation, UUID noteUuid)
         throws DocumentNoteServiceEx {
      storageDocumentServiceSupport.addDocumentNote(getDfceService(), docUuid,
            contenu, login, dateCreation, noteUuid, LOGGER);

   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   @ServiceChecked
   @Loggable(LogLevel.TRACE)
   public List<StorageDocumentNote> getDocumentNotes(UUID docUuid) {
      return storageDocumentServiceSupport.getDocumentNotes(getDfceService(),
            docUuid, LOGGER);
   }

   
   /**
    * {@inheritDoc}
    */
   public final <T> void setDocumentNoteServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);
   }


}
