/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionRunnable;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.dfce.mapping.BeanMapper;
import fr.urssaf.image.sae.storage.dfce.messages.StorageMessageHandler;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.InsertionServiceEx;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.storage.util.StorageMetadataUtils;

/**
 * Item writer de l'écriture des documents dans DFCE
 * 
 */
@Component
public class StorageDocumentWriter implements ItemWriter<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentWriter.class);

   private InsertionPoolThreadExecutor poolExecutor;

   @Autowired
   private DFCEServicesManager dfceManager;

   @Autowired
   private StorageConnectionParameter cnxParameters;

   @Autowired
   private InterruptionTraitementMasseSupport support;

   @Autowired
   @Qualifier("interruption_capture_masse")
   private InterruptionTraitementConfig config;

   private StepExecution stepExecution;

   private static final String TRC_INSERT = "StorageDocumentWriter()";

   /**
    * initialisation du context
    * 
    * @param stepExecution
    *           context de l'étape
    * @throws ConnectionServiceEx
    *            erreur de connection
    */
   @BeforeStep
   public final void init(StepExecution stepExecution)
         throws ConnectionServiceEx {

      poolExecutor = new InsertionPoolThreadExecutor(support,
            config);

      stepExecution.getJobExecution().getExecutionContext().put(
            Constantes.THREAD_POOL, poolExecutor);

      this.stepExecution = stepExecution;

      dfceManager.getConnection();

      LOGGER.debug("{} - ouverture de la connexion DFCE", TRC_INSERT);
   }

   /**
    * Action executée après le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @AfterStep
   public final void end(final StepExecution stepExecution) {
      dfceManager.closeConnection();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void write(final List<? extends StorageDocument> items)
         throws Exception {

      Runnable command;
      int index = 0;

      for (StorageDocument storageDocument : Utils.nullSafeIterable(items)) {

         command = new InsertionRunnable(this.stepExecution.getReadCount()
               + index, storageDocument, this);

         poolExecutor.execute(command);

         index++;

      }

   }

   /**
    * Persistance du document
    * 
    * @param storageDocument
    *           document à sauvegarder
    * @return le document avec l'uuid renseigné
    * @throws InsertionServiceEx
    *            Exception levée lors de la persistance
    */
   public final StorageDocument insertStorageDocument(
         final StorageDocument storageDocument) throws InsertionServiceEx {

      try {
         // conversion du storageDocument en DFCE Document
         final Document docDfce = BeanMapper.storageDocumentToDfceDocument(getBase(),
               storageDocument);

         // ici on récupère le contenu du fichier.
         final byte[] docContentByte = FileUtils.readFileToByteArray(new File(
               storageDocument.getFilePath()));
         final InputStream docContent = new ByteArrayInputStream(docContentByte);

         // ici on récupère le nom et l'extension du fichier
         final String[] file = BeanMapper.findFileNameAndExtension(
               storageDocument, StorageTechnicalMetadatas.NOM_FICHIER
                     .getShortCode().toString());

         final StorageDocument retour = insertDocumentInStorage(docDfce,
               docContentByte, docContent, file, storageDocument.getMetadatas());

         return retour;
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }

   }

   private StorageDocument insertDocumentInStorage(final Document docDfce,
         final byte[] docContentByte, final InputStream documentContent, final String[] file,
         final List<StorageMetadata> datas) throws InsertionServiceEx {

      // Traces debug - entrée méthode

      LOGGER.debug("{} - Début", TRC_INSERT);
      // Fin des traces debug - entrée méthode
      try {

         Document createdDoc;

         if (cnxParameters.isCheckHash()) {

            // on récupère le paramètre général de l'algorithme de hachage des
            // documents dans DFCE

            String digest = null;

            // on récupère l'algorithme de hachage passé dans les métadonnées
            final String typeHash = StorageMetadataUtils.valueMetadataFinder(datas,
                  StorageTechnicalMetadatas.TYPE_HASH.getShortCode());

            final String digestAlgo = cnxParameters.getDigestAlgo();

            LOGGER.debug("{} - Vérification du hash '" + digestAlgo
                  + "' du document dans DFCE", TRC_INSERT);

            if (StringUtils.isNotEmpty(digestAlgo)
                  && StringUtils.isNotEmpty(typeHash)
                  && digestAlgo.equals(typeHash)) {

               // on récupère la valeur du hash contenu dans les métadonnées
               digest = StringUtils.trim(StorageMetadataUtils
                     .valueMetadataFinder(datas, StorageTechnicalMetadatas.HASH
                           .getShortCode()));

               LOGGER.debug("{} - Récupération du hash '" + digest
                     + "' des métadonnées", TRC_INSERT);

            } else {

               // on recalcule le hash
               digest = HashUtils.hashHex(docContentByte, digestAlgo);
               LOGGER
                     .debug("{} - Calcule du hash '" + digest + "'", TRC_INSERT);
            }

            createdDoc = insertStorageDocument(docDfce, file[0], file[1],
                  digest, documentContent);
         } else {

            createdDoc = insertStorageDocument(docDfce, file[0], file[1], null,
                  documentContent);
         }

         LOGGER.debug("{} - Document inséré dans DFCE (UUID: {})", TRC_INSERT,
               createdDoc.getUuid());
         LOGGER.debug("{} - Fin insertion du document dans DFCE", TRC_INSERT);
         LOGGER.debug("{} - Sortie", TRC_INSERT);

         return BeanMapper.dfceDocumentToStorageDocument(createdDoc, null,
               getDfceService(), false);
      } catch (TagControlException tagCtrlEx) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), tagCtrlEx.getMessage(),
               tagCtrlEx);
      } catch (Exception except) {

         throw new InsertionServiceEx(StorageMessageHandler
               .getMessage(Constants.INS_CODE_ERROR), except.getMessage(),
               except);
      }

   }

   protected final Document insertStorageDocument(final Document document,
         final String originalFilename, final String extension, final String digest,
         final InputStream inputStream) throws TagControlException {

      Document doc;

      if (StringUtils.isEmpty(digest)) {

         doc = getDfceService().getStoreService().storeDocument(document,
               originalFilename, extension, inputStream);

      } else {

         doc = getDfceService().getStoreService().storeDocument(document,
               originalFilename, extension, digest, inputStream);
      }

      return doc;
   }

   /**
    * @return
    */
   private ServiceProvider getDfceService() {
      return dfceManager.getDFCEService();
   }

   private Base getBase() {
      return getDfceService().getBaseAdministrationService().getBase(
            cnxParameters.getStorageBase().getBaseName());
   }
}
