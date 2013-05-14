/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.services.impl.storagedocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.UpdateServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.services.storagedocument.UpdateService;

/**
 * Classe d'implémentation de l'interface {@link UpdateService}. Cette classe
 * est un singleton et peut être accéssible via le mécanisme d'injection IOC et
 * l'annotation @Autowired
 * 
 */
@Service
public class UpdateServiceImpl extends AbstractServices implements
      UpdateService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(UpdateServiceImpl.class);

   @Autowired
   private TracesDfceSupport tracesDfceSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   @ServiceChecked
   public void updateStorageDocument(UUID uuid,
         List<StorageMetadata> modifiedMetadatas,
         List<StorageMetadata> deletedMetadatas) throws UpdateServiceEx {

      String trcPrefix = "updateStorageDocument()";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - Récupération des informations du document", trcPrefix);
      List<String> modifMetas = new ArrayList<String>();
      List<String> delMetas = new ArrayList<String>();
      Document storedDocument = getDfceService().getSearchService()
            .getDocumentByUUID(getBaseDFCE(), uuid);

      LOGGER.debug("{} - Modification des critères", trcPrefix);
      for (StorageMetadata metadata : Utils.nullSafeIterable(modifiedMetadatas)) {
         storedDocument.getSingleCriterion(metadata.getShortCode()).setWord(
               (Serializable) metadata.getValue());
         modifMetas.add(metadata.getShortCode());
      }

      LOGGER.debug("{} - Suppression des critères", trcPrefix);
      for (StorageMetadata metadata : Utils.nullSafeIterable(deletedMetadatas)) {
         storedDocument.deleteCriterion(storedDocument
               .getSingleCriterion(metadata.getShortCode()));
         delMetas.add(metadata.getShortCode());
      }

      LOGGER.debug("{} - Mise à jour dans DFCE", trcPrefix);
      try {
         getDfceService().getStoreService().updateDocument(storedDocument);

      } catch (TagControlException exception) {
         throw new UpdateServiceEx(exception);

      } catch (FrozenDocumentException exception) {
         throw new UpdateServiceEx(exception);
      }

      LOGGER.debug("{} - Ajout d'une trace", trcPrefix);
      tracesDfceSupport.traceModifDocumentDansDFCE(uuid, modifMetas, delMetas,
            new Date());

      LOGGER.debug("{} - fin", trcPrefix);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final <T> void setUpdateServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);

   }
}
