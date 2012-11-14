/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.bootstrap;

import java.io.File;
import java.util.Date;

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
   private static final String MODE_LISTE = "liste";
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

      if (MODE_LISTE.equals(args[0])) {
         TraitementService service = context.getBean(TraitementService.class);
         service.writeCodesOrganismes(args[2]);

      } else {
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

      if (!MODE_LISTE.equals(args[0]) && !MODE_MAJ.equals(args[0])) {
         throw new IllegalArgumentException("le mode voulu n'existe pas");
      }

      String configPath = args[1];
      File configFile = new File(configPath);
      if (!configFile.exists()) {
         throw new IllegalArgumentException(
               "le fichier de configuration est inexistant");
      }

      if (MODE_LISTE.equals(args[0])) {
         String pathOutput = args[2];
         File fileOutput = new File(pathOutput);

         if (fileOutput.exists()) {
            throw new IllegalArgumentException(
                  "le fichier de sortie est existe déjà");
         }

      } else if (MODE_MAJ.equals(args[0])) {
         String pathCorresp = args[2];
         File fileCorresp = new File(pathCorresp);

         if (!fileCorresp.exists()) {
            throw new IllegalArgumentException(
                  "le fichier de correspondances est inexistant");
         }
      }

   }

}
