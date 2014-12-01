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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.storage.dfce.annotations.ServiceChecked;
import fr.urssaf.image.sae.storage.dfce.model.AbstractServices;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
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
         manageMetadata(storedDocument, metadata);
         modifMetas.add(metadata.getShortCode());
      }

      LOGGER.debug("{} - Suppression des critères", trcPrefix);
      for (StorageMetadata metadata : Utils.nullSafeIterable(deletedMetadatas)) {
         storedDocument.deleteCriterion(storedDocument
               .getSingleCriterion(metadata.getShortCode()));
         delMetas.add(metadata.getShortCode());
      }

      LOGGER.debug("{} - Mise à jour dans DFCE", trcPrefix);
      String rndCode = null;
      Date referenceDate = null;

      try {
         rndCode = rndCodeUpdate(modifiedMetadatas, storedDocument);
         referenceDate = referenceDateUpdate(modifiedMetadatas, storedDocument);
         getDfceService().getStoreService().updateDocument(storedDocument);

      } catch (TagControlException exception) {
         rollback(rndCode, referenceDate, storedDocument);
         throw new UpdateServiceEx(exception);

      } catch (FrozenDocumentException exception) {
         rollback(rndCode, referenceDate, storedDocument);
         throw new UpdateServiceEx(exception);

      }

      LOGGER.debug("{} - Ajout d'une trace", trcPrefix);
      tracesDfceSupport.traceModifDocumentDansDFCE(uuid, modifMetas, delMetas,
            new Date());

      LOGGER.debug("{} - fin", trcPrefix);

   }

   private void rollback(String rndCode, Date referenceDate,
         Document storedDocument) {
      String trcPrefix = "rollback";
      LOGGER.debug("{} - début", trcPrefix);

      if (StringUtils.isNotBlank(rndCode)) {
         try {
            updateRnd(storedDocument, rndCode);
         } catch (FrozenDocumentException exception) {
            LOGGER.error("{} - Impossible de réinitialiser à l'état "
                  + "d'origine le code RND pour le document {}. "
                  + "Code d'origine : {}", new Object[] { trcPrefix,
                  storedDocument.getUuid().toString(), rndCode });
         }
      }

      if (referenceDate != null) {
         try {
            updateReferenceDate(referenceDate, storedDocument);
         } catch (FrozenDocumentException exception) {
            LOGGER.error("{} - Impossible de réinitialiser à l'état "
                  + "d'origine la date de débit de conservation "
                  + "pour le document {}. Date d'origine : {}", new Object[] {
                  trcPrefix, storedDocument.getUuid().toString(),
                  DateFormatUtils.ISO_DATE_FORMAT.format(referenceDate) });
         }
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

   private void updateRnd(Document storedDocument, String rndCode)
         throws FrozenDocumentException {
      String trcPrefix = "updateRnd";
      LOGGER.debug("{} - début", trcPrefix);

      getDfceService().getStoreService().updateDocumentType(storedDocument,
            rndCode);

      LOGGER.debug("{} - fin", trcPrefix);
   }

   private Date referenceDateUpdate(List<StorageMetadata> modifiedMetadatas,
         Document storedDocument) throws FrozenDocumentException {
      String trcPrefix = "referenceDateUpdate()";
      LOGGER.debug("{} - début", trcPrefix);

      Date oldDate = null;

      StorageMetadata metadata = find(
            StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION.getShortCode(),
            modifiedMetadatas);
      if (metadata != null) {
         oldDate = storedDocument.getLifeCycleReferenceDate();
         updateReferenceDate((Date) metadata.getValue(), storedDocument);
      }

      LOGGER.debug("{} - fin", trcPrefix);
      return oldDate;
   }

   private void updateReferenceDate(Date referenceDate, Document storedDocument)
         throws FrozenDocumentException {
      String trcPrefix = "updateReferenceDate";
      LOGGER.debug("{} - début", trcPrefix);

      getDfceService().getStoreService().updateDocumentLifeCycleReferenceDate(
            storedDocument, referenceDate);

      LOGGER.debug("{} - fin", trcPrefix);
   }

   private String rndCodeUpdate(List<StorageMetadata> modifiedMetadatas,
         Document storedDocument) throws FrozenDocumentException {
      String trcPrefix = "rndCodeUpdate()";
      LOGGER.debug("{} - début", trcPrefix);

      String oldRnd = null;

      StorageMetadata metadata = find(StorageTechnicalMetadatas.TYPE
            .getShortCode(), modifiedMetadatas);
      if (metadata != null) {
         oldRnd = storedDocument.getType();
         updateRnd(storedDocument, (String) metadata.getValue());
      }

      LOGGER.debug("{} - fin", trcPrefix);
      return oldRnd;
   }

   private StorageMetadata find(String shortCode,
         List<StorageMetadata> modifiedMetadatas) {

      String trcPrefix = "find";
      LOGGER.debug("{} - début", trcPrefix);

      int index = 0;
      StorageMetadata metadata = null;
      while (metadata == null && index < modifiedMetadatas.size()) {
         if (shortCode.equals(modifiedMetadatas.get(index).getShortCode())) {
            metadata = modifiedMetadatas.get(index);
         }
         index++;
      }

      LOGGER.debug("{} - fin", trcPrefix);
      return metadata;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final <T> void setUpdateServiceParameter(final T parameter) {
      setDfceService((ServiceProvider) parameter);

   }

   private void manageMetadata(Document storedDocument, StorageMetadata metadata) {
      String trcPrefix = "manageMetadata";
      LOGGER.debug("{} - début", trcPrefix);

      if (StorageTechnicalMetadatas.TITRE.getShortCode().equals(
            metadata.getShortCode())) {
         storedDocument.setTitle((String) metadata.getValue());

      } else if (StorageTechnicalMetadatas.DATE_CREATION.getShortCode().equals(
            metadata.getShortCode())) {
         storedDocument.setCreationDate((Date) metadata.getValue());

      } else if (!StorageTechnicalMetadatas.DATE_DEBUT_CONSERVATION
            .getShortCode().equals(metadata.getShortCode())
            && !StorageTechnicalMetadatas.TYPE.getShortCode().equals(
                  metadata.getShortCode())) {
         if (storedDocument.getSingleCriterion(metadata.getShortCode()) == null) {
            storedDocument.addCriterion(metadata.getShortCode(),
                  (Serializable) metadata.getValue());
         } else {
            storedDocument.getSingleCriterion(metadata.getShortCode()).setWord(
                  (Serializable) metadata.getValue());
         }
      }

      LOGGER.debug("{} - fin", trcPrefix);

   }
}
