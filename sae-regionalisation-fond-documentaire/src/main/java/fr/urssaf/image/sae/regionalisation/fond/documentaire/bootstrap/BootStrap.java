/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.bootstrap;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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

   private static final int ARG_0 = 0;
   private static final int ARG_1 = 1;
   private static final int ARG_2 = 2;
   private static final int ARG_3 = 3;
   private static final int ARG_4 = 4;
   private static final int ARG_5 = 5;
   private static final int ARG_6 = 6;
   private static final int CONVERSION_MIN = 60000;
   private static final String MODE_LISTE_CODE_ORG = "listeOrgs";
   private static final String MODE_LISTE_DOCUMENTS = "listeDocs";
   private static final String MODE_LISTE_NON_INTEGRES = "listeNonIntegres";
   private static final String MODE_MAJ = "maj";

   private static final int MAX_DOC_ARGS = 4;
   private static final int MAX_MAJ_ARGS = 7;
   private static final int MAX_ORG_ARGS = 3;

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

      LOGGER.info("Arguments de la ligne de commande : {}", ArrayUtils
            .toString(args));

      // instanciation du contexte de SPRING
      ApplicationContext context = SAEApplicationContextFactory.load(
            "/applicationContext-sae-regionalisation-fond-documentaire.xml",
            args[1]);

      TraitementService service = context.getBean(TraitementService.class);

      try {
         if (MODE_LISTE_CODE_ORG.equals(args[ARG_0])) {
            service.writeCodesOrganismes(args[ARG_2]);

         } else if (MODE_LISTE_DOCUMENTS.equals(args[ARG_0])) {
            service.writeDocUuidsToUpdate(args[ARG_2], args[ARG_3]);

         } else if (MODE_MAJ.equals(args[ARG_0])) {
            service.updateDocuments(args[ARG_2], args[ARG_3], args[ARG_4],
                  Integer.valueOf(args[ARG_5]), Integer.valueOf(args[ARG_6]));

         } else if (MODE_LISTE_NON_INTEGRES.equals(args[ARG_0])) {
            service.writeDocStartingWithCodeOrga(args[ARG_2], args[ARG_3]);
         }

      } catch (Throwable exception) {
         LOGGER.error("erreur lors du traitement", exception);
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

      checkCommand(args[ARG_0]);

      String configPath = args[ARG_1];
      File configFile = new File(configPath);
      if (!configFile.exists()) {
         throw new IllegalArgumentException(
               "le fichier de configuration est inexistant");
      }

      if (MODE_LISTE_CODE_ORG.equals(args[ARG_0])) {
         checkCodeOrg(args);

      } else if (MODE_LISTE_DOCUMENTS.equals(args[ARG_0])) {
         checkDocs(args);

      } else if (MODE_MAJ.equals(args[ARG_0])) {
         checkMaj(args);

      } else if (MODE_LISTE_NON_INTEGRES.equals(args[ARG_0])) {
         checkNonIntegres(args);
      }

   }

   private static void checkMaj(String[] args) {
      if (args.length != MAX_MAJ_ARGS) {
         throw new IllegalArgumentException(
               "la commande est incorrecte. Les paramètres sont les suivants : \n"
                     + "1. commande du programme à lancer ("
                     + MODE_MAJ
                     + "\n"
                     + "2. fichier de configuration pour les accès CASSANDRA et DFCE\n"
                     + "3. fichier de données\n"
                     + "4. fichier de sortie des traces de mise à jour\n"
                     + "5. fichier de correspondances\n"
                     + "6. index du premier enregistrement à traiter\n"
                     + "7. index du dernier enregistrement à traiter\n");
      }

      String pathDatas = args[ARG_2];
      File fileDatas = new File(pathDatas);

      if (!fileDatas.exists()) {
         throw new IllegalArgumentException(
               "le fichier de données est inexistant");
      }

      String pathOut = args[ARG_3];
      File fileOut = new File(pathOut);

      if (fileOut.exists()) {
         throw new IllegalArgumentException(
               "le fichier de sortie existe déjà");
      }

      String pathCorresp = args[ARG_4];
      File fileCorresp = new File(pathCorresp);

      if (!fileCorresp.exists()) {
         throw new IllegalArgumentException(
               "le fichier de correspondances est inexistant");
      }
      
      String sFirstIndex = args[ARG_5];
      if (!StringUtils.isNumeric(sFirstIndex)) {
         throw new IllegalArgumentException(
               "l'index du premier enregistrement doit être un numérique");
      }

      String sLastIndex = args[ARG_6];
      if (!StringUtils.isNumeric(sLastIndex)) {
         throw new IllegalArgumentException(
               "l'index du dernier enregistrement doit être un numérique");
      }

      int firstIndex = Integer.valueOf(sFirstIndex);
      int lastIndex = Integer.valueOf(sLastIndex);

      if (firstIndex > lastIndex) {
         throw new IllegalArgumentException(
               "l'index du premier enregistrement doit être un supérieur "
                     + "à l'index du dernier enregistrement");
      }
   }

   private static void checkDocs(String[] args) {
      if (args.length != MAX_DOC_ARGS) {
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

      String propPath = args[ARG_3];
      File propFile = new File(propPath);
      if (!propFile.exists()) {
         throw new IllegalArgumentException(
               "le fichier de correspondance est inexistant");
      }

   }

   private static void checkNonIntegres(String[] args) {
      if (args.length != MAX_DOC_ARGS) {
         throw new IllegalArgumentException(
               "la commande est incorrecte. Les paramètres sont les suivants : \n"
                     + "1. commande du programme à lancer ("
                     + MODE_LISTE_NON_INTEGRES
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

      String propPath = args[ARG_3];
      File propFile = new File(propPath);
      if (!propFile.exists()) {
         throw new IllegalArgumentException(
               "le fichier de correspondance est inexistant");
      }

   }

   private static void checkCodeOrg(String[] args) {
      if (args.length != MAX_ORG_ARGS) {
         throw new IllegalArgumentException(
               "la commande est incorrecte. Les paramètres sont les suivants : \n"
                     + "1. commande du programme à lancer ("
                     + MODE_LISTE_CODE_ORG
                     + "\n"
                     + "2. fichier de configuration pour les accès CASSANDRA et DFCE\n"
                     + "3. fichier de sortie\n");
      }

      String pathOutput = args[ARG_2];
      File fileOutput = new File(pathOutput);

      if (fileOutput.exists()) {
         throw new IllegalArgumentException(
               "le fichier de sortie est existe déjà");
      }

   }

   private static void checkCommand(String command) {

      List<String> commands = Arrays.asList(MODE_LISTE_CODE_ORG,
            MODE_LISTE_DOCUMENTS, MODE_MAJ, MODE_LISTE_NON_INTEGRES);

      if (!commands.contains(command)) {
         throw new IllegalArgumentException(
               "La commande désirée est inexistante");
      }
   }

}
