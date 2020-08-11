package fr.urssaf.image.parser_opencsv.application.component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.javers.common.collections.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

import fr.urssaf.image.parser_opencsv.application.constantes.FileConst;
import fr.urssaf.image.parser_opencsv.application.constantes.Metadata;
import fr.urssaf.image.parser_opencsv.application.exception.CorrespondanceException;
import fr.urssaf.image.parser_opencsv.application.exception.CorrespondanceFormatException;
import fr.urssaf.image.parser_opencsv.application.exception.CountNbrePageFileException;
import fr.urssaf.image.parser_opencsv.application.exception.HashInexistantException;
import fr.urssaf.image.parser_opencsv.application.exception.UnknownHashCodeEx;
import fr.urssaf.image.parser_opencsv.application.model.Statistic;
import fr.urssaf.image.parser_opencsv.application.model.entity.JobEntity;
import fr.urssaf.image.parser_opencsv.application.reader.BndCsvReaderBuilder;
import fr.urssaf.image.parser_opencsv.application.service.ICorrespondanceService;
import fr.urssaf.image.parser_opencsv.application.service.IJobService;
import fr.urssaf.image.parser_opencsv.application.service.IValidatorService;
import fr.urssaf.image.parser_opencsv.application.writer.SommaireWriter;
import fr.urssaf.image.parser_opencsv.application.writer.XMLStatisticWriter;
import fr.urssaf.image.parser_opencsv.application.writer.csv.CSVErrorWriter;
import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;
import fr.urssaf.image.parser_opencsv.utils.FileUtils;
import fr.urssaf.image.parser_opencsv.utils.MetadataUtils;
import fr.urssaf.image.parser_opencsv.utils.ResourceUtils;
import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.service.EcdeFileService;

@Component
@Scope("prototype")
public class BndMigrationComponent {

   private static final Logger LOGGER = LoggerFactory.getLogger(BndMigrationComponent.class);

   @Autowired
   private BndCsvReaderBuilder bndCsvParser;

   private SommaireWriter sommaireWriter;

   private XMLStatisticWriter statWriter;

   private CSVErrorWriter csvErrorWriter;

   @Autowired
   private EcdeSource ecdeConfig;

   @Autowired
   private EcdeFileService ecdeService;

   @Autowired
   private ICorrespondanceService correspondanceService;

   @Autowired
   private IValidatorService validatorService;

   @Autowired
   private IJobService jobService;

   @Value("${bnd.source.path}")
   private String sourcePath;

   @Value("${bnd.activate.rtf}")
   private String activeRTF;

   /**
    * Reader du fichier CSV pivot
    */
   private CSVReader csvReader;

   /**
    * Chemin relatif où sont stockés les document sur l'ECDE
    */
   private static final String RELATIVE_DOCUMENT_PATH = "documents/";

   private static final int NOMBRE_COLONNES_CSV = 27;

   /**
    * Lancement de la génération du fichier sommaire à partir du CSV
    * 
    * @param csvPath
    *           chemin vers le CSV
    * @param sommairePath
    *           chemin vers le fichier de destination sommaire.xml
    * @throws IOException
    * @throws XMLStreamException
    */
   public JobEntity generateSommaireFromCSV(final String csvFileName, final JobEntity jobEntity) throws XMLStreamException, IOException {

      // On crée le repertoire dans lequel les fichiers seront déposés sur l'ECDE
      final String targetPath = ResourceUtils.createEcdeDir(ecdeConfig);

      csvErrorWriter = new CSVErrorWriter();
      statWriter = new XMLStatisticWriter();

      LOGGER.info("Ouverture du flux d'écriture du sommaire situé à l'emplacement : {} ", targetPath);
      ouvertureFluxEcritureSommaire(targetPath);

      LOGGER.info("Récupération du reader du fichier csv : {}", csvFileName);
      csvReader = bndCsvParser.getCsvBuilder(csvFileName);

      int countError = 0;
      int countIntegrated = 0;
      int ligneNumber = 2;  
      String[] nextLine;

      while ((nextLine = csvReader.readNext()) != null) {
         // Ce permet d'ignorer les lignes vides
         if (nextLine.length != NOMBRE_COLONNES_CSV) {
            ligneNumber++;
            continue;
         }

         DocumentType document;
         String messageError = "";
         try {
          
           final String mimeType = nextLine[24];
           final String extension = correspondanceService.getExtensionFromMimeType(mimeType);
           document = MetadataUtils.convertLigneArrayToDocument(nextLine, extension);
            LOGGER.info("Meta : {}", document.getMetadonnees().getMetadonnee());
            LOGGER.info("Le document est valide : {}", validatorService.validateRequireMetadatas(document));

            // Si les métadonnées requises sont bien renseignées
            if (validatorService.validateRequireMetadatas(document)) {

               // appliquer les correspondances SSTI GED
               correspondanceService.applyCorrespondance(document);

               // Chemin absolu du fichier (path + nom du fichier)
               final String nomFichierSource = document.getObjetNumerique().getPath();
               // Récupère le nom du fichier sans le Path
               final String nomFichierDestination = document.getObjetNumerique().getCheminEtNomDuFichier();
               String sourceFile = sourcePath + RELATIVE_DOCUMENT_PATH + nomFichierSource;
               sourceFile = FilenameUtils.separatorsToSystem(sourceFile);

               // Chemin absolu du fichier (path + nom du fichier) après application des correspodances
               String destinationFile = targetPath + RELATIVE_DOCUMENT_PATH + nomFichierDestination;
               destinationFile = FilenameUtils.separatorsToSystem(destinationFile);

               if (new File(sourceFile).exists()) {

                 // ajout du test sur le hash du document
                  final String hashInitial = document.getMetadonnees().getMetaValue("Hash");
                  final String hashCalculated = FileUtils.getHash(sourceFile);
                  if (!StringUtils.equalsIgnoreCase(hashCalculated, hashInitial.trim())) {
                    throw new UnknownHashCodeEx("Le hash du document ne correspond pas");
                 }

                  LOGGER.info("Copie du binaire du doc {} ==> {}", sourceFile, destinationFile);
                  ResourceUtils.copyResourceToFile(sourceFile, destinationFile);
                  // Calcul du nombre du binaire associé
                  correspondanceService.calculateNbPages(destinationFile, document, Boolean.valueOf(activeRTF));
                  LOGGER.info("Ajout du Document {} au fichier sommaire.xml", document);

                  sommaireWriter.addDocument(document);
                  countIntegrated++;
               } else {
                  messageError = "Le binaire du document est manquant!";
                  LOGGER.error(messageError);
               }
            } else {
               messageError = "Certaines métadonnées requises au stockage sont manquantes";
            }
         }
         catch (final CorrespondanceException | CorrespondanceFormatException | ParseException | HashInexistantException | CountNbrePageFileException | UnknownHashCodeEx e) {
            LOGGER.error("Le traitement a planté a la ligne N°{}, Contenu : {} ", ligneNumber, MetadataUtils.convertStringFromArray(nextLine));
            messageError = e.getMessage();
         }

         // Ecriture des lignes en erreurs dans un CSV
         if (!"".equals(messageError)) {
            if (countError == 0) {
               csvErrorWriter.openErrorfile(targetPath);
               csvErrorWriter.write(Metadata.CSV_HEADERS);
            }
            writeCSVErrorLigne(nextLine, ligneNumber, messageError);
            countError++;
         }
         ligneNumber++;
      }

      LOGGER.info("Fin d'ecriture du sommaire sommaire");
      sommaireWriter.endSommaire();

      // Sauvegarder les stats en base et produire le fichier XML de Stat
      final Statistic statistic = new Statistic(countError + countIntegrated, countIntegrated, countError);
      writeStatistics(jobEntity, statistic, targetPath);

      final URI uri = generateEcdeURI(targetPath);
      jobEntity.setEcdeUrl(uri.toString());

      jobEntity.setTargetPath(targetPath);
      jobEntity.setSourcePath(sourcePath);

      if (countError != 0) {
         LOGGER.info("Fermeture du flux d'ecriture des erreurs de parsing du CSV s'il y a eu des erreurs");
         csvErrorWriter.close();
      }

      return jobEntity;
   }

   /**
    * Ouverture du flux d'écriture du fichier sommaire.xml
    * 
    * @param sommairePath
    * @throws IOException
    * @throws XMLStreamException
    */
   private void ouvertureFluxEcritureSommaire(final String sommairePath) throws IOException, XMLStreamException {
      sommaireWriter = new SommaireWriter();
      sommaireWriter.openFile(sommairePath);
      sommaireWriter.startSommaire();
      sommaireWriter.addSommaireHeader();
   }

   /**
    * Femeture des flux d'ecriture du sommaire.xml
    */
   public void closeStreamWriter() {
      if (sommaireWriter != null) {
         try {
            sommaireWriter.closeStream();
         }
         catch (IOException | XMLStreamException e) {
            LOGGER.error("Une erreur est survenue au moment de la fermeture du flux de d'écriture du sommaire");
            throw new RuntimeException("Erreur de la fermeture des flux d'écriture du sommaire", e);
         }
      }
   }

   /**
    * Fermeture des flux de lecture du fichier CSV en entrée
    */
   public void closeStreamReader() {
      try {
         if (bndCsvParser != null) {
            bndCsvParser.closeStream();
         }

         if (csvReader != null) {
            csvReader.close();
         }
      }
      catch (final IOException e) {
         LOGGER.error("Une erreur est survenue au moment de la fermeture du flux de lecture du fichier CSV");
         throw new RuntimeException("Erreur de la fermeture des flux de lecture du fichier CSV", e);
      }
   }

   /**
    * Ecriture des statistiques du Job
    * 
    * @param jobEntity
    */
   private void writeStatistics(final JobEntity jobEntity, final Statistic xmlStat, final String xmlTargetPath) {
      jobEntity.setNbreAjouteSommaire(xmlStat.getAddedDocumentsCount());
      jobEntity.setNbreDocumentsInitial(xmlStat.getInitialDocumentsCount());

      jobService.saveJob(jobEntity);
      statWriter.write(xmlStat, xmlTargetPath);
   }

   /**
    * Ecrire une ligne en erreur dans le fichier CSV
    * 
    * @param ligneNumber
    *           le numero de la ligne où l'erreur est survenue
    * @param message
    *           le message d'erreur
    */
   private void writeCSVErrorLigne(String[] nextLine, final int ligneNumber, final String message) {
      final List<Object> listObj = Arrays.asList(nextLine);
      listObj.add("Ligne : " + ligneNumber);
      listObj.add(message);
      nextLine = listObj.toArray(new String[0]);
      LOGGER.info("Ecriture de l'erreur survenue à la ligne : {}", ligneNumber);
      csvErrorWriter.write(nextLine);
   }

   /**
    * Génère URL vers l'ECDE à partir d'un fichier sommaire.xml
    * 
    * @param sommairePath
    * @return
    */
   private URI generateEcdeURI(final String sommairePath) {
      final String sommaireAbsolutePath = sommairePath + FileConst.SOMMAIRE_FILE_NAME;
      LOGGER.info("Generation de l'URI vers l'ecde à partir du fichier : {} ", sommaireAbsolutePath);
      try {
         final File ecdeFile = new File(sommaireAbsolutePath);
         ResourceUtils.setFilePermissions(ecdeFile);
         
        final URI uri = ecdeService.convertFileToURI(ecdeFile, ecdeConfig);
         LOGGER.info("URL du sommaire {}", uri.getPath());

         return uri;
      }
      catch (final EcdeBadFileException | IOException e) {
         LOGGER.error("L'url de l'ECDE n'est pas conforme");
         LOGGER.error("Details de l'erreur : {}", e.getMessage());
         throw new RuntimeException(e);
      }
   }

}
