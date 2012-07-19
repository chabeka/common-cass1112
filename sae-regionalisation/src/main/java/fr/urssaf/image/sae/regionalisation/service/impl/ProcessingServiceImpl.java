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

         List<SearchCriterion> searchCriterions;

         int indexRecord = firstRecord;

         do {

            // on récupère des blocs de recherches
            searchCriterions = this.searchCriterionDao.getSearchCriteria(
                  indexRecord, Math.min(SIZE_BLOCK, processingCount));

            for (SearchCriterion searchCriterion : searchCriterions) {

               Map<String, Object> metadatas = this.metadataDao
                     .getMetadatas(searchCriterion.getId());

               List<Document> documents = this.saeDocumentDao
                     .getDocuments(searchCriterion.getLucene());

               // on incrémente de 1 si aucun document n'est retourné
               if (documents.isEmpty()) {
                  nbRecordSansDocument++;
               }

               // on trace le nombre de documents pour un critère de recherche
               this.traceDao.addTraceRec(searchCriterion.getId(), documents
                     .size());

               // si le flag est positionné à MISE_A_JOUR
               if (updateDatas) {

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

                     // ajout des traces de modifications des données
                     for (Trace trace : traces) {

                        trace.setIdDocument(document.getUuid());
                        trace.setIdSearch(searchCriterion.getId());

                        this.traceDao.addTraceMaj(trace);
                     }

                     // on incrémente de 1 le nombre de documents traités
                     nbRecordDocumentTraites++;

                  }

                  // mise à jour flag traite du critères de recherche
                  this.searchCriterionDao.updateSearchCriterion(searchCriterion
                        .getId());

               }

            }

            indexRecord += SIZE_BLOCK;

         } while (!searchCriterions.isEmpty());

         LOGGER.debug("nombre de recherche sans documents associés: {}",
               nbRecordSansDocument);
         LOGGER.debug("nombre de documents traités: {}",
               nbRecordDocumentTraites);

      } finally {

         // dans tous les cas, on se déconnecte de DFCE
         this.serviceSupport.disconnect();
      }

   }

   protected final Trace update(Document document,
         Entry<String, Object> metadata) {

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
               + " ne peut être modifié");
      }

      return trace;
   }

}
