/**
 * 
 */
package fr.urssaf.image.sae.jobs.executable.bootstrap;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.jobs.executable.service.TraitementService;

/**
 * Classe de lancement des traitements
 * 
 */
public final class JobsMain {

   private static final Logger LOG = LoggerFactory.getLogger(JobsMain.class);

   private static final String CONFIGURATION_FILE = "/applicationContext-sae-jobs-executable.xml";

   private static final String MESSAGE_ARGUMENTS_MANQUANTS = "Le chemin complet du fichier de configuration générale du SAE doit être renseigné, ainsi que la méthode à invoquer.";

   private JobsMain() {

   }

   /**
    * Méthode de lancement des traitements
    * 
    * @param args
    * @throws Throwable
    */
   public static void main(String[] args) throws Throwable {

      // 2 arguments attendus
      if (args == null || ArrayUtils.getLength(args) == 0) {
         LOG.error(MESSAGE_ARGUMENTS_MANQUANTS);
         throw new IllegalArgumentException(MESSAGE_ARGUMENTS_MANQUANTS);
      }

      if (ArrayUtils.getLength(args) > 0 && StringUtils.isBlank(args[0])) {
         LOG
               .error("Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
         throw new IllegalArgumentException(
               "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
      }

      if (ArrayUtils.getLength(args) > 1 && StringUtils.isBlank(args[1])) {
         LOG.error("Le nom de la méthode à invoquer doit être renseigné");

         throw new IllegalArgumentException(
               "Le nom de la méthode à invoquer doit être renseigné");
      }

      String saeConfiguration = args[0];
      // Existence du fichier de configuration
      File ficSaeConf = new File(saeConfiguration);
      if (ficSaeConf.exists()) {

         String nomMethode = args[1];
         if ("PURGE_PILE_TRAVAUX".equals(nomMethode)) {

            ClassPathXmlApplicationContext context = ContextFactory
                  .createSAEApplicationContext(CONFIGURATION_FILE,
                        saeConfiguration);

            try {
               TraitementService traitementService = context
                     .getBean(TraitementService.class);
               traitementService.purger();
            } catch (Throwable ex) {
               LOG
                     .error(
                           "Une erreur a eu lieu dans l'execution du jar sae-job-executable",
                           ex);
               throw ex;
            } finally {

               // on force ici la fermeture du contexte de Spring
               // ceci a pour but de forcer la déconnexion avec Cassandra
               LOG.debug("execute - fermeture du contexte d'application");
               context.close();
               LOG
                     .debug("execute - fermeture du contexte d'application effectuée");
            }

         } else {
            LOG.error("Le nom de la méthode à invoquer est incorrect");
            throw new IllegalArgumentException(
                  "Le nom de la méthode à invoquer est incorrect");
         }
      } else {
         LOG.error("Le fichier de configuration du SAE n'existe pas");
         throw new IllegalArgumentException(
               "Le fichier de configuration du SAE n'existe pas");
      }

   }

}
