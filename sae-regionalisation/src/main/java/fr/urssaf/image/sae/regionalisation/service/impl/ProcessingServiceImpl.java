package fr.urssaf.image.sae.regionalisation.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.docubase.toolkit.model.document.Document;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.bean.RepriseConfiguration;
import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.comparator.DocumentCogComparator;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TermInfoRangeStringDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.datas.TermInfoResultSet;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.support.CassandraSupport;
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
   private static final int END_COG_POS = 3;

   private static final DocumentCogComparator DOC_COMPARATOR = new DocumentCogComparator();

   private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
         "dd/MM/yyyy HH:mm:ss");

   private static final int MILLISEC_CONVERSION = 1000;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessingServiceImpl.class);

   private final SaeDocumentDao saeDocumentDao;

   private final TraceDao traceDao;

   private final ServiceProviderSupport serviceSupport;

   private final RepriseConfiguration repriseConfiguration;

   private final CassandraSupport cassandraSupport;

   private final TermInfoRangeStringDao termInfoDao;

   private int currentRecord;

   private int nbRecordSansDocument = 0;

   private int nbRecordDocumentTraites = 0;

   private int nbRecordUpdated = 0;

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
    * @param cassandraSupport
    *           opérations sur la base cassandra
    * @param termInfoDao
    *           opérations sur la famille de colonne TermInfoRangeString
    * 
    */
   @Autowired
   public ProcessingServiceImpl(SaeDocumentDao saeDocumentDao,
         TraceDao traceDao, ServiceProviderSupport serviceSupport,
         RepriseConfiguration repriseConfiguration,
         CassandraSupport cassandraSupport, TermInfoRangeStringDao termInfoDao) {

      this.saeDocumentDao = saeDocumentDao;
      this.traceDao = traceDao;
      this.serviceSupport = serviceSupport;
      this.repriseConfiguration = repriseConfiguration;
      this.cassandraSupport = cassandraSupport;
      this.termInfoDao = termInfoDao;
   }

   private void updateDocuments(SearchCriterion searchCriterion,
         List<Document> documents, Map<Integer, String> lines) {

      // int nbRecordDocumentTraites = 0;

      int indexDoc = 0;
      int indexLine = 0;
      List<Integer> listLines = new ArrayList<Integer>(lines.keySet());
      Collections.sort(listLines);
      String line;
      String donnees;
      String[] tabLine;
      List<Document> modifiedDocs = new ArrayList<Document>();
      String lastRequest = "";
      String currentRequest = "";
      while (indexLine < listLines.size() && indexDoc < documents.size()) {

         line = lines.get(listLines.get(indexLine));
         tabLine = line.split(";");

         Map<String, Object> metadonnees = new HashMap<String, Object>();
         Map<String, Object> oldMetadonnees = new HashMap<String, Object>();

         for (int j = 1; j < tabLine.length; j = j + 2) {
            donnees = tabLine[j + 1];
            metadonnees.put(tabLine[j], donnees.split(">")[1]);
            oldMetadonnees.put(tabLine[j], donnees.split(">")[0]);
            currentRequest = tabLine[0];
         }

         if (!currentRequest.equals(lastRequest)) {
            LOGGER
                  .debug(
                        "nombre de métadonnées à mettre à jour pour la requête lucène '{}': {}",
                        searchCriterion.getLucene(), metadonnees.size());
            lastRequest = currentRequest;
         }

         while (indexDoc < documents.size()
               && ((String) documents.get(indexDoc).getSingleCriterion("cog")
                     .getWord()).compareTo((String) oldMetadonnees.get("cog")) < 0) {
            indexDoc++;
         }

         while (indexDoc < documents.size()
               && ((String) documents.get(indexDoc).getSingleCriterion("cog")
                     .getWord()).equals((String) oldMetadonnees.get("cog"))) {

            List<Trace> traces = new ArrayList<Trace>();

            // mettre à jour les métadonnées
            for (Entry<String, Object> metadata : metadonnees.entrySet()) {

               Trace trace = this.updateDocument(documents.get(indexDoc),
                     metadata, listLines.get(indexLine));

               if (trace != null) {
                  traces.add(trace);
               }
            }

            // persistance des modifications
            this.saeDocumentDao.update(documents.get(indexDoc));
            modifiedDocs.add(documents.get(indexDoc));

            LOGGER.debug("document n°{} a été mise à jour", documents.get(
                  indexDoc).getUuid());

            // ajout des traces de modifications des données
            for (Trace trace : traces) {

               LOGGER
                     .debug(
                           "document n°{} a mis à jour la métadonnée '{}' avec une nouvelle valeur '{}' pour remplacer l'ancienne '{}'",
                           new Object[] { documents.get(indexDoc).getUuid(),
                                 trace.getMetaName(), trace.getNewValue(),
                                 trace.getOldValue() });

               trace.setIdDocument(documents.get(indexDoc).getUuid());

               this.traceDao.addTraceMaj(trace);
            }

            nbRecordUpdated += modifiedDocs.size();

            indexDoc++;
         }

         indexLine++;

         // TraceDatasUtils.traceMetas(modifiedDocs, metadonnees,
         // oldMetadonnees,
         // currentRecord);
      }

      LOGGER
            .debug(
                  "critère de recherche {} avec la requête lucène '{}' a été traitée",
                  searchCriterion.getId(), searchCriterion.getLucene());

   }

   private Trace updateDocument(Document document,
         Entry<String, Object> metadata, int lineNumber) {

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
            processFile(updateDatas, source, lastRecord);

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
         fileWriter.write("Date : " + SIMPLE_DATE_FORMAT.format(new Date())
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

   private void processFile(boolean updateDatas, File source, int lastRecord) {
      final String trcPrefixe = "processFile()";

      this.serviceSupport.connect();
      this.cassandraSupport.connect();

      String indexName = getIndexNameFromFile(source);
      Map<Integer, String> map = getValuesFromLineNumber(currentRecord,
            lastRecord, source);

      List<Integer> keys = new ArrayList<Integer>(map.keySet());
      Collections.sort(keys);

      Date dateStart = new Date();
      RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> query = termInfoDao
            .getQuery(map.get(currentRecord), map.get(keys.get(1)), indexName);

      Date dateEnd = new Date();
      LOGGER.debug("temps de création de la recherche (en ms) : {}", (dateEnd
            .getTime() - dateStart.getTime()));

      Reader fileReader = null;
      BufferedReader reader = null;

      try {
         fileReader = new FileReader(source);
         reader = new BufferedReader(fileReader);

         String line = gotoLine(reader, currentRecord);

         TermInfoResultSet resultSet = new TermInfoResultSet(query);
         String reference;

         while ((reference = resultSet.getNextValue()) != null
               && StringUtils.isNotBlank(line) && currentRecord <= lastRecord) {

            line = logInexistingLines(reader, reference, line, lastRecord,
                  updateDatas);
            line = updateExistingDatas(reader, reference, line, updateDatas,
                  lastRecord);

         }

         while (StringUtils.isNotBlank(line) && currentRecord <= lastRecord) {

            line = logInexistingLines(reader, line, lastRecord, updateDatas);
         }

         LOGGER.info("nombre de recherche sans documents associés: {}",
               nbRecordSansDocument);
         LOGGER.info(
               "nombre de documents traités (récupérés par les requêtes) : {}",
               nbRecordDocumentTraites);
         LOGGER.info("nombre de documents mis à jour : {}", nbRecordUpdated);

      } catch (FileNotFoundException exception) {
         throw new ErreurTechniqueException(exception);

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcPrefixe);
            }
         }

         if (fileReader != null) {
            try {
               fileReader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcPrefixe);
            }
         }

         cassandraSupport.disconnect();
         serviceSupport.disconnect();
      }
   }

   private String updateExistingDatas(BufferedReader reader, String reference,
         String line, boolean updateDatas, int lastRecord) {

      String currentLine = line;
      try {
         Map<Integer, String> lines = new HashMap<Integer, String>();
         while (StringUtils.isNotBlank(currentLine)
               && currentLine.split(";")[2].split(">")[0].equals(reference)
               && currentRecord <= lastRecord) {

            lines.put(currentRecord, currentLine);
            currentLine = reader.readLine();
            currentRecord++;
         }

         if (!lines.isEmpty()) {
            searchAndUpdateDocuments(lines, updateDatas);
         }

         return currentLine;

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);
      }

   }

   /**
    * @param lines
    */
   private void searchAndUpdateDocuments(Map<Integer, String> lines,
         boolean updateDatas) {

      Date dateStart, dateEnd;
      List<String> lLines = new ArrayList<String>(lines.values());
      String line = lLines.get(0);
      String[] tabLine = line.split(";");

      SearchCriterion criterion = new SearchCriterion();
      criterion.setId(currentRecord);
      criterion.setLucene(tabLine[0]);

      List<Document> documents = this.saeDocumentDao.getDocuments(criterion
            .getLucene());

      // on incrémente de 1 si aucun document n'est retourné
      if (documents.isEmpty()) {
         nbRecordSansDocument++;

         LOGGER.debug(
               "aucun document n'a été récupéré pour la requête lucène '{}'",
               criterion.getLucene());
      }

      List<Integer> listLines = new ArrayList<Integer>(lines.keySet());
      Collections.sort(listLines);
      for (Integer lineNumber : listLines) {
         this.traceDao.addTraceRec(criterion.getLucene(), lineNumber, documents
               .size(), updateDatas);
      }

      // si le flag est positionné à MISE_A_JOUR
      if (updateDatas) {

         dateStart = new Date();

         Collections.sort(documents, DOC_COMPARATOR);
         updateDocuments(criterion, documents, lines);

         dateEnd = new Date();
         LOGGER.debug(
               "temps de mise a jour des documents pour le critere {} = {} ms",
               criterion.getId(), (dateEnd.getTime() - dateStart.getTime()));

      } else {

         LOGGER
               .debug(
                     "nombre de documents à mettre à jour pour la requête lucène '{}': {}",
                     criterion.getLucene(), documents.size());

      }

      // on incrémente le nombre de documents traités d'autant que
      // de documents retournés
      nbRecordDocumentTraites += documents.size();

   }

   private String logInexistingLines(BufferedReader reader, String reference,
         String line, int lastRecord, boolean updateDatas) {

      String currentLine = line;

      try {
         while (StringUtils.isNotBlank(currentLine)
               && currentLine.split(";")[2].split(">")[0].compareTo(reference) < 0
               && currentRecord <= lastRecord) {

            currentLine = logLine(currentLine, updateDatas, reader);
            currentRecord++;
         }

         return currentLine;

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

   private String logInexistingLines(BufferedReader reader, String line,
         int lastRecord, boolean updateDatas) {

      String currentLine = line;

      try {
         while (StringUtils.isNotBlank(currentLine)
               && currentRecord <= lastRecord) {

            currentLine = logLine(currentLine, updateDatas, reader);
            nbRecordSansDocument++;
            currentRecord++;

         }

         return currentLine;

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

   private String logLine(String currentLine, boolean updateDatas,
         BufferedReader reader) throws IOException {
      String currentValue = currentLine.split(";")[0];

      this.traceDao.addTraceRec(currentValue, currentRecord, 0, updateDatas);
      LOGGER
            .debug(
                  "nombre de documents à mettre à jour pour la requête lucène '{}': {}",
                  currentValue, 0);
      nbRecordSansDocument++;
      return reader.readLine();
   }

   /**
    * @param reader
    * @param currentRecord2
    * @return
    */
   private String gotoLine(BufferedReader reader, int record) {
      try {
         int index = 0;
         String line = null;

         while (index < record && (line = reader.readLine()) != null) {
            index++;
         }

         return line;

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

   private Map<Integer, String> getValuesFromLineNumber(int firstRecord,
         int lastRecord, File source) {

      Map<Integer, String> map = new HashMap<Integer, String>();
      FileReader fileReader = null;
      BufferedReader reader = null;
      String trcMethodName = "getValuesFromLineNumber()";

      try {
         fileReader = new FileReader(source);
         reader = new BufferedReader(fileReader);
         int index = 0;

         index = addReferenceToMap(map, index, firstRecord, reader);
         addReferenceToMap(map, index, lastRecord, reader);

         return map;

      } catch (FileNotFoundException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcMethodName);
            }
         }

         if (fileReader != null) {
            try {
               fileReader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcMethodName);
            }
         }
      }

   }

   private int addReferenceToMap(Map<Integer, String> map, int index,
         int record, BufferedReader reader) {

      int currentIndex = index;
      try {
         String line = null;
         String value = null;
         while (currentIndex < record && (line = reader.readLine()) != null) {
            value = line;
            currentIndex++;
         }

         String requete = value.split(";")[0];
         String reference = requete.split(":")[1];
         map.put(currentIndex, reference);

         return currentIndex;

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);
      }

   }

   /**
    * @param source
    * @return
    */
   private String getIndexNameFromFile(File source) {

      String trcMethodName = "getModeFromFile()";
      FileReader fileReader = null;
      BufferedReader reader = null;

      try {
         fileReader = new FileReader(source);
         reader = new BufferedReader(fileReader);

         String line = reader.readLine();
         String indexName = line.substring(0, END_COG_POS);

         return indexName;

      } catch (FileNotFoundException exception) {
         throw new ErreurTechniqueException(exception);

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcMethodName);
            }
         }

         if (fileReader != null) {
            try {
               fileReader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcMethodName);
            }
         }
      }
   }
}
