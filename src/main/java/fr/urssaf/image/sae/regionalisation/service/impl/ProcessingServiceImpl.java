package fr.urssaf.image.sae.regionalisation.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;
import fr.urssaf.image.sae.regionalisation.bean.RepriseConfiguration;
import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.exception.LineFormatException;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;
import fr.urssaf.image.sae.regionalisation.util.Constants;

/**
 * Implémentation du service {@link ProcessingService}
 * 
 * 
 */
@Service
public class ProcessingServiceImpl implements ProcessingService {

   /**
    * 
    */
   private static final int MILLISEC_CONVERSION = 1000;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessingServiceImpl.class);

   private final SaeDocumentDao saeDocumentDao;

   private final TraceDao traceDao;

   private final ServiceProviderSupport serviceSupport;

   private final RepriseConfiguration repriseConfiguration;

   private int currentRecord;

   /**
    * 
    @param saeDocumentDao
    *           dao des documents SAE
    * @param traceDao
    *           dao des traces
    * @param serviceSupport
    *           services DFCE
    * @param repriseConfiguration
    *           configuration de reprise de traitement automatique
    */
   @Autowired
   public ProcessingServiceImpl(SaeDocumentDao saeDocumentDao,
         TraceDao traceDao, ServiceProviderSupport serviceSupport,
         RepriseConfiguration repriseConfiguration) {

      this.saeDocumentDao = saeDocumentDao;
      this.traceDao = traceDao;
      this.serviceSupport = serviceSupport;
      this.repriseConfiguration = repriseConfiguration;
   }

   private void update(SearchCriterion searchCriterion,
         List<Document> documents, Map<String, Object> metadatas, int lineNumber) {

      // int nbRecordDocumentTraites = 0;

      for (Document document : documents) {

         List<Trace> traces = new ArrayList<Trace>();

         // mettre à jour les métadonnées
         for (Entry<String, Object> metadata : metadatas.entrySet()) {

            Trace trace = this.update(document, metadata, lineNumber);

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

            this.traceDao.addTraceMaj(trace);
         }

      }

      LOGGER
            .debug(
                  "critère de recherche {} avec la requête lucène '{}' a été traitée",
                  searchCriterion.getId(), searchCriterion.getLucene());

   }

   private Trace update(Document document, Entry<String, Object> metadata,
         int lineNumber) {

      Trace trace = null;

      if (ArrayUtils.contains(Constants.METADATAS, metadata.getKey())) {

         trace = new Trace();
         trace.setMetaName(metadata.getKey());

         if (document.getSingleCriterion(metadata.getKey()) != null) {

            trace.setOldValue(ObjectUtils.toString(document.getSingleCriterion(
                  metadata.getKey()).getWord()));
         }

         trace.setNewValue(ObjectUtils.toString(metadata.getValue()));
         trace.setLineNumber(lineNumber);

         this.serviceSupport.updateCriterion(document, metadata.getKey(),
               metadata.getValue());

      } else {

         LOGGER.warn("la métadonnée " + metadata.getKey()
               + " ne peut être modifiée");
      }

      return trace;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void launchWithFile(boolean updateDatas, File source,
         String uuid, int firstRecord, int lastRecord, String dirPath) {
      boolean success = false;
      int count = 1;
      currentRecord = firstRecord;

      File dirParent = new File(dirPath);
      File endFile = new File(dirParent, "fin_traitement_" + uuid + ".flag");
      FileUtils.deleteQuietly(endFile);

      while (!success && count <= repriseConfiguration.getMaxTestCount()) {
         try {

            if (count > 1) {
               Thread.sleep(repriseConfiguration.getMaxTestCount()
                     * MILLISEC_CONVERSION);
            }

            traceDao.open(uuid);
            createOrUpdateDebutTraitement(dirParent, uuid, count);
            process(updateDatas, source, uuid, lastRecord);
            success = true;

         } catch (Throwable throwable) {
            LOGGER.error(
                  "Echec de la tentative de traitement {} à la ligne {}\n",
                  count, currentRecord);
            LOGGER.error("erreur source :", throwable);

         } finally {
            traceDao.close();
         }

         count++;
      }

      createFinTraitement(dirParent, success, uuid);

   }

   /**
    * @param dirParent
    */
   private void createOrUpdateDebutTraitement(File dirParent, String uuid,
         int tentative) {

      File startFile = new File(dirParent, "debut_traitement_" + uuid + ".flag");
      FileWriter fileWriter = null;

      try {
         fileWriter = new FileWriter(startFile, true);
         fileWriter.write("tentative " + tentative + " - ");
         fileWriter.write("Date : "
               + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
               + "\n");

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);

      } finally {
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException e) {
               LOGGER.info("impossible de fermer le flux de "
                     + startFile.getName());
            }
         }
      }

   }

   private void createFinTraitement(File dirParent, boolean succes, String uuid) {
      File endFile = new File(dirParent, "fin_traitement_" + uuid + ".flag");
      FileWriter fileWriter = null;

      try {
         fileWriter = new FileWriter(endFile);
         fileWriter.write(succes ? "OK" : "KO");
         fileWriter.write("\n");

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);

      } finally {
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException e) {
               LOGGER.info("impossible de fermer le flux de "
                     + endFile.getName());
            }
         }
      }

   }

   private void process(boolean updateDatas, File source, String uuid,
         int lastRecord) {
      final String trcPrefixe = "launchWithFile()";

      FileReader fileReader = null;
      BufferedReader reader = null;
      int nbRecordSansDocument = 0;
      int nbRecordDocumentTraites = 0;

      // connexion à DFCE
      this.serviceSupport.connect();

      try {
         Date dateStart, dateEnd;
         fileReader = new FileReader(source);
         reader = new BufferedReader(fileReader);
         CSVReader csvReader = new CSVReader(fileReader, ';', '\'',
               currentRecord -1);
         String[] tabLine;

         while (ArrayUtils.isNotEmpty((tabLine = csvReader.readNext()))
               && currentRecord <= lastRecord) {

            if ((tabLine.length - 1) % 2 != 0 || tabLine.length < 2) {
               throw new LineFormatException(currentRecord);
            }

            SearchCriterion criterion = new SearchCriterion();
            criterion.setId(currentRecord);
            criterion.setLucene(tabLine[0]);

            Map<String, Object> metadonnees = new HashMap<String, Object>();

            for (int j = 1; j < tabLine.length; j = j + 2) {
               metadonnees.put(tabLine[j], tabLine[j + 1]);
            }

            LOGGER
                  .debug(
                        "nombre de métadonnées à mettre à jour pour la requête lucène '{}': {}",
                        criterion.getLucene(), metadonnees.size());

            dateStart = new Date();

            List<Document> documents = this.saeDocumentDao
                  .getDocuments(criterion.getLucene());

            dateEnd = new Date();

            LOGGER.debug("temps de recherche pour le critere {} = {} ms",
                  criterion.getId(), (dateEnd.getTime() - dateStart.getTime()));

            // on incrémente de 1 si aucun document n'est retourné
            if (documents.isEmpty()) {
               nbRecordSansDocument++;

               LOGGER
                     .debug(
                           "aucune document n'a été récupéré pour la requête lucène '{}'",
                           criterion.getLucene());
            }

            this.traceDao.addTraceRec(criterion.getLucene(), currentRecord,
                  documents.size(), updateDatas);

            // si le flag est positionné à MISE_A_JOUR
            if (updateDatas) {

               dateStart = new Date();

               update(criterion, documents, metadonnees, currentRecord);

               dateEnd = new Date();
               LOGGER
                     .debug(
                           "temps de mise a jour des documents pour le critere {} = {} ms",
                           criterion.getId(), (dateEnd.getTime() - dateStart
                                 .getTime()));

            } else {

               LOGGER
                     .debug(
                           "nombre de documents à mettre à jour pour la requête lucène '{}': {}",
                           criterion.getLucene(), documents.size());

            }

            // on incrémente de 1 le nombre de documents traités
            nbRecordDocumentTraites += documents.size();

            LOGGER.debug("ligne " + currentRecord);
            currentRecord++;

         }

         LOGGER.info("nombre de recherche sans documents associés: {}",
               nbRecordSansDocument);
         LOGGER
               .info("nombre de documents traités: {}", nbRecordDocumentTraites);

      } catch (FileNotFoundException exception) {
         throw new ErreurTechniqueException(exception);

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOGGER.info(
                     "{} - Impossible de fermer le flux de lecture buffer",
                     trcPrefixe);
            }
         }

         if (fileReader != null) {
            try {
               fileReader.close();
            } catch (IOException e) {
               LOGGER.info(
                     "{} - Impossible de fermer le flux de lecture fichier",
                     trcPrefixe);
            }
         }

         // deconnexion de DFCE
         serviceSupport.disconnect();
      }
   }

}
