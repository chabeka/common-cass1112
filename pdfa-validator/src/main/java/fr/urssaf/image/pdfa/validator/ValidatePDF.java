package fr.urssaf.image.pdfa.validator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.commons.pdfbox.service.FormatValidationService;
import fr.urssaf.image.commons.pdfbox.service.impl.FormatValidationServiceImpl;
import fr.urssaf.image.pdfa.exception.FileExisteException;
import fr.urssaf.image.pdfa.exception.NoAnalysisFolderOrLogFolderException;
import fr.urssaf.image.pdfa.exception.NotAFileException;
import fr.urssaf.image.pdfa.exception.NotAFolderException;

public class ValidatePDF {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ValidatePDF.class);

   /**
    * @param args
    *           liste des arguments fournies par les utilisateurs. En première
    *           position se trouve le chemin complet où se trouve le(s)
    *           fichier(s) à analyser en deuxième position le chemin complet du
    *           répertoire qui va contenir le rapport d'analyse.
    * @throws IOException
    * @throws NotAFileException
    * @throws FileExisteException
    * @throws NotAFolderException
    * @throws NonAnalysisFolderOrLogFolderException
    */
   public static void main(String[] args) throws IOException,
         NoAnalysisFolderOrLogFolderException, NotAFileException,
         FileExisteException, NotAFolderException {

      // on vérifie qu'il y a bien deux arguments ou plus
      if (!ArrayUtils.isEmpty(args)) {


         
         if(args[0].equals("-h")||args[0].equals("-help")||args[0].equals("h")||args[0].equals("help")||args[0].equals("?")){
            displayHelp();
         }
         if (StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1])) {
            LOGGER
                  .error("Le chemin complet vers les fichiers à analyser et le répertoire des log sont obligatoires");
            displayHelp();
            throw new NoAnalysisFolderOrLogFolderException();
            
         }

         File cheminPdf = new File(args[0]);
         File fichierLog = new File(args[1]);
         // On vérifie que l'argument précisé pour le fichier log est bien un
         // fichier et non un répertoire
         // on vérifie que le fichier est inexistant
         if (fichierLog.exists()) {
            throw new FileExisteException(fichierLog.getName());
         } else {
            FileUtils.write(fichierLog,
                  "Début de l'analyse des fichiers PDF \n",Charset.forName("UTF-8"));
         }

         if (cheminPdf.isDirectory()) {

            // Récupère la liste des fichiers *.pdf présents dans le
            // répertoire
            List<File> files = (List<File>) FileUtils.listFiles(new File(
                  args[0]), new String[] { "pdf" }, true);

            // Tri par ordre alphabétique de nom de fichier, case insensitive
            Collections.sort(files);

            // Boucle sur la liste des fichiers

            for (File file : files) {
               validate(file, fichierLog);
            }

         } else if (cheminPdf.isFile()) {
            validate(cheminPdf, fichierLog);
         } else {
            throw new NotAFolderException(cheminPdf.getAbsolutePath());
         }
         LOGGER
               .debug(
                     "Le rapport de traitement se trouve à cet emplacement suivant {}",
                     args[1]);
      } else {
         displayHelp();
         throw new NoAnalysisFolderOrLogFolderException();
      }

   }

   private static void validate(File file, File log) throws IOException {
      LOGGER.debug("Validation de {}", file.getAbsolutePath());
      // Instantiation du service
      FormatValidationService formatValService = new FormatValidationServiceImpl();
      List<String> erreurs;
      try {

         erreurs = formatValService.validate(file);
         if (erreurs.isEmpty()) {
            FileUtils.write(log, MessageFormat.format("{0} --> OK \n", file
                  .getName()), true);
            LOGGER.info("{} --> OK ", file.getAbsolutePath());
         } else {
            // FileUtils.write(log, String.format("%s;KO",
            // file.getAbsolutePath()), true);
            FileUtils.write(log, MessageFormat.format("{0} --> KO \n", file
                  .getName()), true);
            Map<String, String> errorNoDoublon = new HashMap<String, String>();
            for(String error : erreurs){
               int count = Collections.frequency(erreurs, error);
               Object[] args={count};
               if(!errorNoDoublon.containsKey(error)){
                  errorNoDoublon.put(error, error.concat(new MessageFormat(" ({0} fois)").format(args)));
               }
            }
            FileUtils.write(log, "Le document est n'est pas un PDF/A conforme\n", true);
            FileUtils.writeLines(log, errorNoDoublon.values(), true);
            FileUtils.write(log, "\n", true);
            LOGGER.info("{} --> KO ", file.getName());
         }

      } catch (FormatValidationException ex) {

         LOGGER.debug("Une exception de validation a été levée : {}", ex);
         FileUtils.write(log, String
               .format("%s;Erreur", file.getAbsolutePath()), true);
         FileUtils.write(log, String.format("%s;Erreur", (Object[]) ex
               .getStackTrace()), true);
      }
   }
   
   private static void displayHelp() throws IOException{
      
         //LOGGER.warn(FileUtils.readFileToString(new File(ValidatePDF.class.getClassLoader().getResource("readme.txt").getFile())));
         LOGGER.warn(IOUtils.toString(ValidatePDF.class.getResourceAsStream("/readme.txt"), Charset.forName("UTF-8")));
   }
}
