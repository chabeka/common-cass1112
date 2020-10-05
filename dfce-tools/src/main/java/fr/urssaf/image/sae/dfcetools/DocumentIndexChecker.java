package fr.urssaf.image.sae.dfcetools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.dao.BaseDAO;
import fr.urssaf.image.sae.dfcetools.helper.CompositeIndexHelper;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.index.IndexInformation;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Classe permettant de vérifier l'indexation de documents
 */
public class DocumentIndexChecker {

   private static final Logger LOGGER = LoggerFactory.getLogger(DocumentIndexChecker.class);
   private final CqlSession session;

   private final Map<String, Boolean> indexedMeta = new HashMap<>(100);

   private final UUID baseId;

   private final DFCEServices dfceServices;

   public DocumentIndexChecker(final CqlSession session, final DFCEServices dfceServices) {
      this.session = session;
      this.dfceServices = dfceServices;
      baseId = BaseDAO.getBaseUUID(session);
      initIndexedMeta();
   }

   /**
    * 
    */
   private void initIndexedMeta() {
      final List<IndexInformation> indexes = dfceServices.getIndexesInBase(baseId);
      for (final IndexInformation indexInformation : indexes) {
         indexedMeta.put(indexInformation.getIndexKey(), true);
      }
   }

   /**
    * Vérifie l'indexation d'un document
    * 
    * @param doc
    */
   public void checkDocument(final Document doc) {
      checkOrCleanDocument(doc, false);
   }

   /**
    * Supprime les indexation d'un document
    * 
    * @param doc
    */
   public void cleanDocument(final Document doc) {
      checkOrCleanDocument(doc, true);
   }

   /**
    * Teste si le document est existant.
    * S'il n'existe pas, on nettoie ses index
    * 
    * @param doc
    *           le document à nettoyer
    * @return true si le document a nécessité un nettoyage
    */
   public boolean cleanDocumentIfNeeded(final Document doc) {
      final Document foundDoc = dfceServices.getDocumentByUUID(doc.getUuid());
      if (foundDoc == null) {
         cleanDocument(doc);
         return true;
      }
      return false;
   }

   private void checkOrCleanDocument(final Document doc, final boolean shouldClean) {
      final Set<CompositeIndex> compositeIndexes = dfceServices.fetchAllCompositeIndex();
      for (final CompositeIndex compositeIndex : compositeIndexes) {
         if (compositeIndex.isComputed()) {
            checkOrCleanCompositeIndex(compositeIndex, doc, shouldClean);
         }
      }
      checkOrCleanIndexedMetadatas(doc, shouldClean);
   }

   private void checkOrCleanIndexedMetadatas(final Document doc, final boolean shouldClean) {
      final List<Criterion> criterions = doc.getAllCriterions();
      for (final Criterion criterion : criterions) {
         final String metaName = criterion.getCategoryName();
         if (indexedMeta.containsKey(metaName)) {
            final String metaValue = CompositeIndexHelper.normalizeMetaValue(criterion.getWordValue());
            LOGGER.info("Check {}", metaName);
            LOGGER.info("metaValue = {}", metaValue);
            final IndexCleaner cleaner = new IndexCleaner(session, metaName, criterion.getWordType());
            cleaner.verifyOrCleanOneEntry(metaValue, doc.getUuid(), shouldClean);
         }
      }
      // Catégories systèmes indexées
      for (final String metaName : new String[] {"SM_ARCHIVAGE_DATE", "SM_CREATION_DATE", "SM_UUID", "SM_FINAL_DATE", "SM_LIFE_CYCLE_REFERENCE_DATE",
      "SM_MODIFICATION_DATE"}) {
         final String metaValue = CompositeIndexHelper.getMetaValue(doc, metaName);
         if (!metaValue.isEmpty()) {
            LOGGER.info("Check {}", metaName);
            LOGGER.info("metaValue = {}", metaValue);
            final String dataType = metaName.equals("SM_UUID") ? "UUID" : "DATETIME";
            final IndexCleaner cleaner1 = new IndexCleaner(session, metaName, dataType);
            cleaner1.verifyOrCleanOneEntry(metaValue, doc.getUuid(), shouldClean);
         }
      }

   }

   private void checkOrCleanCompositeIndex(final CompositeIndex compositeIndex, final Document doc, final boolean shouldClean) {
      final String metaValue = CompositeIndexHelper.getMetaValue(compositeIndex, doc);
      if (!metaValue.isEmpty()) {
         LOGGER.info("Check {}", CompositeIndexHelper.getIndexName(compositeIndex));
         LOGGER.info("metaValue = {}", metaValue);
         final String indexName = CompositeIndexHelper.getIndexName(compositeIndex);
         final IndexCleaner cleaner = new IndexCleaner(session, indexName, "STRING");
         cleaner.verifyOrCleanOneEntry(metaValue, doc.getUuid(), shouldClean);
      }
   }

}
