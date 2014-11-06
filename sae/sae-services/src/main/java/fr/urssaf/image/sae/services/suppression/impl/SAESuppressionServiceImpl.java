/**
 * 
 */
package fr.urssaf.image.sae.services.suppression.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.urssaf.image.sae.services.document.impl.AbstractSAEServices;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.DeletionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.UUIDCriteria;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;

/**
 * Classe implémentant l'interface {@link SAESuppressionService}. Cette classe
 * est un singleton et peut être accessible par le système d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Service
public class SAESuppressionServiceImpl extends AbstractSAEServices implements
      SAESuppressionService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAESuppressionServiceImpl.class);

   @Autowired
   @Qualifier("storageDocumentService")
   private StorageDocumentService storageService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void suppression(UUID idArchive) throws SuppressionException, ArchiveInexistanteEx {
      String trcPrefix = "suppression";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de suppression du document {}", new Object[] {
            trcPrefix, idArchive.toString() });

      UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, null);

      try {
         this.getStorageServiceProvider().openConnexion();

         try {
            LOG.debug("{} - recherche du document", trcPrefix);
            StorageDocument document = storageService
                  .searchStorageDocumentByUUIDCriteria(uuidCriteria);
            if (document == null) {
               String message = StringUtils
                     .replace(
                           "Il n'existe aucun document pour l'identifiant d'archivage {0}",
                           "{0}", idArchive.toString());
               throw new ArchiveInexistanteEx(message);

            }

            LOG.debug("{} - suppression du document", trcPrefix);
            storageService.deleteStorageDocument(idArchive);

         } catch (SearchingServiceEx exception) {
            throw new SuppressionException(exception);

         } catch (DeletionServiceEx exception) {
            throw new SuppressionException(exception);
         }
      } catch (ConnectionServiceEx e) {
         throw new SuppressionException(e);
      }
      LOG.debug("{} - Suppression du document {} terminée", new Object[] {
            trcPrefix, idArchive.toString() });
      LOG.debug("{} - fin", trcPrefix);
   }

}
