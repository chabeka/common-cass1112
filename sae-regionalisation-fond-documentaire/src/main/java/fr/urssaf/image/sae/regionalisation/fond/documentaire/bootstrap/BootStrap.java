/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.bootstrap;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.factory.SAEApplicationContextFactory;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TraitementService;

/**
 * 
 * 
 */
public final class BootStrap {

   /**
    * 
    */
   private static final int CONVERSION_MIN = 60000;
   private static final String MODE_LISTE_CODE_ORG = "listeOrgs";
   private static final String MODE_LISTE_DOCUMENTS = "listeDocs";
   private static final String MODE_MAJ = "maj";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(BootStrap.class);

   /**
    * Constructeur
    */
   private BootStrap() {
   }

   /**
    * Classe de démarrage de l'application
    * 
    * @param args
    *           liste des arguments
    */
   public static void main(String[] args) {

      String trcPrefix = "main";

      LOGGER.debug("{} - Début du traitement", trcPrefix);
      long startTime = new Date().getTime();

      checkParams(args);

      // instanciation du contexte de SPRING
      ApplicationContext context = SAEApplicationContextFactory.load(
            "/applicationContext-sae-regionalisation-fond-documentaire.xml",
            args[1]);

      if (MODE_LISTE_CODE_ORG.equals(args[0])) {
         TraitementService service = context.getBean(TraitementService.class);
         service.writeCodesOrganismes(args[2]);

      } else if (MODE_LISTE_DOCUMENTS.equals(args[0])) {
         TraitementService service = context.getBean(TraitementService.class);
         service.writeDocUuidsToUpdate(args[2], args[3]);

      } else if (MODE_MAJ.equals(args[0])) {
         // TODO
      }

      long endTime = new Date().getTime();
      long time = (endTime - startTime) / CONVERSION_MIN;
      LOGGER.debug("{} - Fin du traitement", trcPrefix);
      LOGGER.debug("{} - Traitement réalisé en {} min.", new Object[] {
            trcPrefix, time });

   }

   private static void checkParams(String[] args) {

      if (args.length < 2) {
         throw new IllegalArgumentException("la ligne de commande est erronée");
      }

      checkCommand(args[0]);

      String configPath = args[1];
      File configFile = new File(configPath);
      if (!configFile.exists()) {
         throw new IllegalArgumentException(
               "le fichier de configuration est inexistant");
      }

      if (MODE_LISTE_CODE_ORG.equals(args[0])) {
         checkCodeOrg(args);

      } else if (MODE_LISTE_DOCUMENTS.equals(args[0])) {
         checkDocs(args);

      } else if (MODE_MAJ.equals(args[0])) {
         String pathCorresp = args[2];
         File fileCorresp = new File(pathCorresp);

         if (!fileCorresp.exists()) {
            throw new IllegalArgumentException(
                  "le fichier de correspondances est inexistant");
         }
      }

   }

   private static void checkDocs(String[] args) {
      if (args.length != 4) {
         throw new IllegalArgumentException(
               "la commande est incorrecte. Les paramètres sont les suivants : \n"
                     + "1. commande du programme à lancer ("
                     + MODE_LISTE_DOCUMENTS
                     + "\n"
                     + "2. fichier de configuration pour les accès CASSANDRA et DFCE\n"
                     + "3. fichier de sortie\n"
                     + "4. fichier de correspondance des organismes\n");
      }

      String pathOutput = args[2];
      File fileOutput = new File(pathOutput);

      if (fileOutput.exists()) {
         throw new IllegalArgumentException(
               "le fichier de sortie est existe déjà");
      }

      String propPath = args[3];
      File propFile = new File(propPath);
      if (!propFile.exists()) {
         throw new IllegalArgumentException(
               "le fichier de correspondance est inexistant");
      }

   }

   private static void checkCodeOrg(String[] args) {
      if (args.length != 3) {
         throw new IllegalArgumentException(
               "la commande est incorrecte. Les paramètres sont les suivants : \n"
                     + "1. commande du programme à lancer ("
                     + MODE_LISTE_CODE_ORG
                     + "\n"
                     + "2. fichier de configuration pour les accès CASSANDRA et DFCE\n"
                     + "3. fichier de sortie\n");
      }

      String pathOutput = args[2];
      File fileOutput = new File(pathOutput);

      if (fileOutput.exists()) {
         throw new IllegalArgumentException(
               "le fichier de sortie est existe déjà");
      }

   }

   private static void checkCommand(String command) {

      List<String> commands = Arrays.asList(MODE_LISTE_CODE_ORG,
            MODE_LISTE_DOCUMENTS, MODE_MAJ);

      if (!commands.contains(command)) {
         throw new IllegalArgumentException(
               "La commande désirée est inexistante");
      }
   }

}
