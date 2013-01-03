/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.common.Constants;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.iterator.CassandraIterator;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocumentService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TraitementService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.ServiceProviderSupport;

/**
 * Implémentation de l'interface {@link TraitementService}
 * 
 */
@Component
public class TraitementServiceImpl implements TraitementService {

   @Autowired
   private DocumentService documentService;

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private ServiceProviderSupport providerSupport;

   @Autowired
   private DocInfoDao dao;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraitementServiceImpl.class);
   private static final int LENGTH_CODE_ORGA = 3;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeCodesOrganismes(String filePath) {

      String trcPrefix = "writeCodesOrganismes";

      try {
         cassandraSupport.connect();

         LOGGER.debug("{} - récupération des codes organismes", trcPrefix);

         List<String> codesAutorisesOrga = Arrays.asList("cop", "cog");
         AllRowsQuery<DocInfoKey, String> query = dao.getQuery("SM_UUID",
               "cop", "cog");
         CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
               query);
         Map<String, String> infos;
         Map<String, Long> map = new HashMap<String, Long>();
         int nbDocInfo = 0;
         int nbDocInfoParTrace = 1000;

         while (iterator.hasNext()) {
            infos = iterator.next();

            if (infos.size() == 3) {
               for (String codeorga : codesAutorisesOrga) {

                  String value = infos.get(codeorga);

                  if (StringUtils.isNotBlank(value)) {

                     String cleMap = value + ";" + codeorga;

                     if (map.containsKey(cleMap)) {
                        map.put(cleMap, map.get(cleMap) + 1);
                     } else {
                        map.put(cleMap, 1L);
                     }

                  }
               }

               // Compteurs de lignes parcourues
               if (nbDocInfo % nbDocInfoParTrace == 0) {
                  LOGGER.info("Nombre de lignes de DocInfo parcourues : {}",
                        nbDocInfo);
               }
               nbDocInfo++;
            }

         }

         // Map<String, Long> codes = docInfoService.getCodesOrganismes();
         // File file = new File(filePath);
         //
         // LOGGER.debug(
         // "{} - Ecriture des codes organismes récupérés dans le fichier",
         // trcPrefix);

         writeFile(map, new File(filePath));

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         cassandraSupport.disconnect();
      }

   }

   private void writeFile(Map<String, Long> codes, File file)
         throws IOException {

      List<String> lines = new ArrayList<String>();

      for (Map.Entry<String, Long> entry : codes.entrySet()) {
         lines.add(String.format("%s;%s", entry.getKey(), entry.getValue()));
      }

      FileUtils.writeLines(file, lines);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeDocUuidsToUpdate(String outputPath,
         String propertiesFilePath) {

      String trcPrefix = "writeDocUuidsToUpdate";
      InputStream inStream = null;

      File fileCodeOrga = new File(propertiesFilePath);

      File outFile = new File(outputPath);
      Writer writer = null;

      try {
         cassandraSupport.connect();

         inStream = FileUtils.openInputStream(fileCodeOrga);

         Properties properties = new Properties();
         properties.load(inStream);

         writer = new FileWriter(outFile);

         LOGGER.debug(
               "{} - récupération de tous les documents et des métadonnées",
               trcPrefix);

         AllRowsQuery<DocInfoKey, String> query = dao.getQuery("SM_UUID",
               "cop", "cog");
         CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
               query);

         Map<String, String> document;

         int nbDocInfo = 0;
         int nbDocInfoParTrace = 1000;

         while (iterator.hasNext()) {

            // Traitement du document courant
            document = iterator.next();
            if (document.size() == 3
                  && (properties.keySet().contains(
                        document.get(Constants.CODE_ORG_GEST)) || properties
                        .keySet().contains(
                              document.get(Constants.CODE_ORG_PROP)))) {

               // Ecriture de l'id d'archivage dans le fichier de sortie
               writer
                     .write(String.format("%s\n", document.get(Constants.UUID)));

            }

            // Trace
            if (nbDocInfo % nbDocInfoParTrace == 0) {
               LOGGER.info("Nombre de lignes de DocInfo parcourues : {}",
                     nbDocInfo);
            }

            // Mise à jour du compteur du nombre de lignes parcourues
            nbDocInfo++;

         }

         // Trace
         LOGGER.info("Nombre total de lignes de DocInfo parcourues : {}",
               nbDocInfo - 1);

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {

         close(inStream, fileCodeOrga.getAbsolutePath());

         close(writer, outFile.getAbsolutePath());

         cassandraSupport.disconnect();

      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void updateDocuments(String inputFilePath,
         String outputFilePath, String propertiesFilePath, int firstRecord,
         int lastRecord) {

      File file = new File(inputFilePath);
      Reader reader = null;
      BufferedReader bReader = null;
      Reader propReader = null;
      String trcPrefix = "updateDocuments()";
      File outFile = new File(outputFilePath);
      Writer writer = null;

      try {
         providerSupport.connect();

         reader = new FileReader(file);
         bReader = new BufferedReader(reader);

         writer = new FileWriter(outFile);

         LOGGER.debug("{} - chargement des données du fichier de properties",
               trcPrefix);

         propReader = new FileReader(new File(propertiesFilePath));
         Properties properties = new Properties();
         properties.load(propReader);

         String line = skipLines(bReader, firstRecord);

         int index = firstRecord - 1;
         while (StringUtils.isNotEmpty(line) && index < lastRecord) {
            updateDocument(line, properties, writer);
            line = bReader.readLine();
            index++;
         }

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } catch (DfceException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         close(bReader, file.getName());
         close(reader, file.getName());
         close(propReader, new File(propertiesFilePath).getName());

         close(writer, new File(outputFilePath).getName());

         providerSupport.disconnect();
      }

   }

   private String skipLines(BufferedReader bReader, int firstRecord)
         throws IOException {

      int index = 0;
      String line;

      do {
         line = bReader.readLine();
         index++;

      } while (line != null && index < firstRecord);

      return line;
   }

   private void close(Reader reader, String name) {
      String trcPrefix = "close()";

      if (reader != null) {
         try {
            reader.close();

         } catch (IOException exception) {
            LOGGER.info("{} - impossible de fermer le flux {}", new Object[] {
                  trcPrefix, name });
         }
      }
   }

   private void close(Writer writer, String name) {
      String trcPrefix = "close()";

      if (writer != null) {
         try {
            writer.close();

         } catch (IOException exception) {
            LOGGER.info("{} - impossible de fermer le flux {}", new Object[] {
                  trcPrefix, name });
         }
      }
   }

   private void close(InputStream inputStream, String name) {
      String trcPrefix = "close()";

      if (inputStream != null) {
         try {
            inputStream.close();

         } catch (IOException exception) {
            LOGGER.info("{} - impossible de fermer le flux {}", new Object[] {
                  trcPrefix, name });
         }
      }

   }

   private void updateDocument(String uuid, Properties properties, Writer writer)
         throws DfceException {
      String trcPrefix = "updateDocument()";

      LOGGER.debug("{} - Recherche du document {}", new Object[] { trcPrefix,
            uuid });
      Document document = documentService.getDocument(UUID.fromString(uuid));
      if (document == null) {
         LOGGER.warn("{} - Document non trouvé : {}", new Object[] { trcPrefix,
               uuid });
      } else {
         updateCriterion(document, Constants.CODE_ORG_GEST, properties, writer);
         updateCriterion(document, Constants.CODE_ORG_PROP, properties, writer);

         LOGGER.debug("{} - Mise à jour du document {}", new Object[] {
               trcPrefix, uuid });
         documentService.updateDocument(document);
      }

   }

   private void updateCriterion(Document document, String codeCriterion,
         Properties properties, Writer writer) {

      Criterion criterion = document.getSingleCriterion(codeCriterion);
      if (criterion != null) {
         String value = (String) criterion.getWord();

         if (properties.keySet().contains(value)) {

            try {
               writer.write(document.getUuid().toString());
               writer.write(";");
               writer.write(codeCriterion);
               writer.write(";");
               writer.write(value);
               writer.write(";");
               writer.write(properties.getProperty(value));
               writer.write("\n");

            } catch (IOException exception) {
               throw new ErreurTechniqueException(exception);
            }

            document.getSingleCriterion(codeCriterion).setWord(
                  properties.getProperty(value));
         }
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void writeDocStartingWithCodeOrga(String outputPath,
         String propertiesFilePath) {
      String trcPrefix = "writeDocStartingWithCodeOrga()";
      LOGGER.debug("{} - début du listing des documents", trcPrefix);

      Writer writer = null;
      Reader reader = null;
      Properties properties = new Properties();
      try {
         File outputFile = new File(outputPath);
         writer = new FileWriter(outputFile);

         File propertiesFile = new File(propertiesFilePath);
         reader = new FileReader(propertiesFile);
         properties.load(reader);
         List<String> codesOrga = getCodesOrgaFromProperties(properties);

         cassandraSupport.connect();

         LOGGER.debug(
               "{} - recherche de tous les documents et leurs informations",
               trcPrefix);

         AllRowsQuery<DocInfoKey, String> query = dao
               .getQuery("nce", "SM_UUID", "cog", "cop", "SM_ARCHIVAGE_DATE",
                     "SM_DOCUMENT_TYPE");
         CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
               query);

         String nce, nceStart;
         Map<String, String> infos;
         int index = 0;
         while (iterator.hasNext()) {
            infos = iterator.next();

            if (infos.size() == 6) {
               nce = infos.get("nce");
               nceStart = StringUtils.left(nce, LENGTH_CODE_ORGA);
               if (codesOrga.contains(nceStart)) {
                  writer.write(nce);
                  writer.write(";");
                  writer.write(infos.get("SM_UUID"));
                  writer.write(";");
                  writer.write(infos.get("cog"));
                  writer.write(";");
                  writer.write(infos.get("cop"));
                  writer.write(";");
                  writer.write(infos.get("SM_ARCHIVAGE_DATE"));
                  writer.write(";");
                  writer.write(infos.get("SM_DOCUMENT_TYPE"));
                  writer.write("\n");
               }
            }

            if (index % 1000 == 0) {
               LOGGER.info("{} - nombre de docs traités : {}", new Object[] {
                     trcPrefix, index });
            }

            index++;
         }

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         close(reader, propertiesFilePath);
         close(writer, outputPath);
         cassandraSupport.disconnect();
      }

   }

   /**
    * @param properties
    * @return
    */
   private List<String> getCodesOrgaFromProperties(Properties properties) {

      List<String> list = new ArrayList<String>();

      String sKey;
      for (Object key : properties.keySet()) {
         sKey = (String) key;
         sKey = StringUtils.right(sKey, LENGTH_CODE_ORGA);
         list.add(sKey);
      }

      return list;
   }
}
