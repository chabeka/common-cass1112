package fr.urssaf.image.sae.regionalisation.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.SearchCriterionDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

/**
 * Implémentation du service {@link ProcessingService}
 * 
 * 
 */
@Service
public class ProcessingServiceImpl implements ProcessingService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessingServiceImpl.class);

   private final SearchCriterionDao searchCriterionDao;

   private final MetadataDao metadataDao;

   private final SaeDocumentDao saeDocumentDao;

   private final TraceDao traceDao;

   private final ServiceProviderSupport serviceSupport;

   private static final int SIZE_BLOCK = 5;

   /**
    * 
    * @param searchCriterionDao
    *           dao des critères de recherche
    * @param metadataDao
    *           dao des métadonnées
    * @param saeDocumentDao
    *           dao des documents SAE
    * @param traceDao
    *           dao des traces
    * @param serviceSupport
    *           services DFCE
    */
   @Autowired
   public ProcessingServiceImpl(SearchCriterionDao searchCriterionDao,
         MetadataDao metadataDao, SaeDocumentDao saeDocumentDao,
         TraceDao traceDao, ServiceProviderSupport serviceSupport) {

      this.searchCriterionDao = searchCriterionDao;
      this.metadataDao = metadataDao;
      this.saeDocumentDao = saeDocumentDao;
      this.traceDao = traceDao;
      this.serviceSupport = serviceSupport;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void launch(boolean updateDatas, int firstRecord,
         int processingCount) {

      // connexion à DFCE
      this.serviceSupport.connect();

      try {

         // nombre d'enregistrement sans correspondance de documents
         int nbRecordSansDocument = 0;

         // nombre de documents traités
         int nbRecordDocumentTraites = 0;

         int count = 0;

         do {

            // on récupère des blocs de recherches
            List<SearchCriterion> searchCriterions = this.searchCriterionDao
                  .getSearchCriteria(firstRecord, Math.min(SIZE_BLOCK,
                        processingCount - count));

            LOGGER.debug("nombre de critères de recherche à traiter: {}",
                  searchCriterions.size());

            for (SearchCriterion searchCriterion : searchCriterions) {

               LOGGER.debug(
                     "critère de recherche {} avec la requête lucène: {}",
                     searchCriterion.getId(), searchCriterion.getLucene());

               Map<String, Object> metadatas = this.metadataDao
                     .getMetadatas(searchCriterion.getId());

               LOGGER.debug("nombre de métadonnées à mettre à jour: {}",
                     metadatas.size());

               List<Document> documents = this.saeDocumentDao
                     .getDocuments(searchCriterion.getLucene());

               // on incrémente de 1 si aucun document n'est retourné
               if (documents.isEmpty()) {
                  nbRecordSansDocument++;

                  LOGGER.debug("aucune document n'a été récupéré");
               }

               // si le flag est positionné à MISE_A_JOUR
               if (updateDatas) {

                  update(searchCriterion, documents, metadatas);

               } else {

                  // on trace le nombre de documents pour un critère de
                  // recherche pour le mode TIR_A_BLANC
                  this.traceDao.addTraceRec(searchCriterion.getId(), documents
                        .size(), false);

                  LOGGER.debug("nombre de documents à mettre à jour: {}",
                        documents.size());

               }

               // on incrémente de 1 le nombre de documents traités
               nbRecordDocumentTraites += documents.size();

            }

            count += SIZE_BLOCK;

         } while (count < processingCount);

         LOGGER.info("nombre de recherche sans documents associés: {}",
               nbRecordSansDocument);
         LOGGER
               .info("nombre de documents traités: {}", nbRecordDocumentTraites);

      } finally {

         // dans tous les cas, on se déconnecte de DFCE
         this.serviceSupport.disconnect();
      }

   }

   private void update(SearchCriterion searchCriterion,
         List<Document> documents, Map<String, Object> metadatas) {

      // on trace le nombre de documents pour un critère de
      // recherche pour le mode MISE_A_JOUR
      this.traceDao
            .addTraceRec(searchCriterion.getId(), documents.size(), true);

      LOGGER.debug("nombre de documents à mettre à jour: {}", documents.size());

      // int nbRecordDocumentTraites = 0;

      for (Document document : documents) {

         List<Trace> traces = new ArrayList<Trace>();

         // mettre à jour les métadonnées
         for (Entry<String, Object> metadata : metadatas.entrySet()) {

            Trace trace = this.update(document, metadata);

            if (trace != null) {
               traces.add(trace);
            }
         }

         // persistance des modifications
         this.saeDocumentDao.update(document);

         LOGGER.debug("document n°{} a été mise à jour", document.getUuid());

         // ajout des traces de modifications des données
         for (Trace trace : traces) {

            LOGGER
                  .debug(
                        "document n°{} a mis à jour la métadonnée '{}' avec une nouvelle valeur '{}' pour remplacer l'ancienne '{}'",
                        new Object[] { document.getUuid(), trace.getMetaName(),
                              trace.getNewValue(), trace.getOldValue() });

            trace.setIdDocument(document.getUuid());
            trace.setIdSearch(searchCriterion.getId());

            this.traceDao.addTraceMaj(trace);
         }

      }

      // mise à jour flag traite du critères de recherche
      this.searchCriterionDao.updateSearchCriterion(searchCriterion.getId());

      LOGGER.debug(
            "critère de recherche {} avec la requête lucène: {} a été traitée",
            searchCriterion.getId(), searchCriterion.getLucene());

   }

   private Trace update(Document document, Entry<String, Object> metadata) {

      Trace trace = null;

      if (ArrayUtils.contains(MetadataDao.METADATAS, metadata.getKey())) {

         trace = new Trace();
         trace.setMetaName(metadata.getKey());

         if (document.getSingleCriterion(metadata.getKey()) != null) {

            trace.setOldValue(ObjectUtils.toString(document.getSingleCriterion(
                  metadata.getKey()).getWord()));
         }

         trace.setNewValue(ObjectUtils.toString(metadata.getValue()));

         this.serviceSupport.updateCriterion(document, metadata.getKey(),
               metadata.getValue());

      } else {

         LOGGER.warn("la métadonnée " + metadata.getKey()
               + " ne peut être modifiée");
      }

      return trace;
   }

}
