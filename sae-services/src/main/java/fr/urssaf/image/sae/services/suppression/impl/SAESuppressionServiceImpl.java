/**
 * 
 */
package fr.urssaf.image.sae.services.suppression.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;
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
public class SAESuppressionServiceImpl implements SAESuppressionService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAESuppressionServiceImpl.class);

   @Autowired
   private StorageDocumentService storageService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void suppression(UUID idArchive) throws SuppressionException {
      String trcPrefix = "suppression";
      LOG.debug("{} - début", trcPrefix);
      LOG.debug("{} - Début de suppression du document {}", new Object[] {
            trcPrefix, idArchive.toString() });

      UUIDCriteria uuidCriteria = new UUIDCriteria(idArchive, null);

      try {
         LOG.debug("{} - recherche du document", trcPrefix);
         StorageDocument document = storageService
               .searchStorageDocumentByUUIDCriteria(uuidCriteria);
         if (document == null) {
            String message = StringUtils.replace(
                  "le document {0} n'existe pas. Suppression impossible",
                  "{0}", idArchive.toString());
            throw new SuppressionException(message);
         }

         LOG.debug("{} - suppression du document", trcPrefix);
         storageService.deleteStorageDocument(idArchive);

      } catch (SearchingServiceEx exception) {
         throw new SuppressionException(exception);

      } catch (DeletionServiceEx exception) {
         throw new SuppressionException(exception);
      }

      LOG.debug("{} - Suppression du document {} terminée", new Object[] {
            trcPrefix, idArchive.toString() });
      LOG.debug("{} - fin", trcPrefix);
   }

}
