package fr.urssaf.image.sae.rnd.executable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import exception.MajCorrespondancesMainException;
import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.rnd.exception.MajCorrespondancesException;
import fr.urssaf.image.sae.rnd.service.MajCorrespondancesService;

/**
 * Lancement du traitement de mise à jour des correspondances du RND
 * 
 * 
 */
public class MajCorrespondancesMain {

   private static final Logger LOG = LoggerFactory
         .getLogger(MajCorrespondancesMain.class);

   private final String contextConfig;

   protected MajCorrespondancesMain(String contextConfig) {

      this.contextConfig = contextConfig;
   }

   protected final void execute(String args[]) {

      if (args == null || ArrayUtils.getLength(args) == 0) {
         throw new IllegalArgumentException(
               "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
      }

      if (ArrayUtils.getLength(args) > 0 && !StringUtils.isNotBlank(args[0])) {
         throw new IllegalArgumentException(
               "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
      }

      String saeConfiguration = args[0];

      // instanciation du contexte de SPRING
      ClassPathXmlApplicationContext context = ContextFactory
            .createSAEApplicationContext(contextConfig, saeConfiguration);

      try {

         // appel du service de MAJ des correspondances
         MajCorrespondancesService majCorrespondancesService = (MajCorrespondancesService) context
               .getBean(MajCorrespondancesService.class);

         try {
            majCorrespondancesService.lancer();
         } catch (MajCorrespondancesException e) {
            LOG
                  .error(
                        "Une erreur a eu lieu dans le processus de mise à jour des correspondances du RND.",
                        e);
            throw new MajCorrespondancesMainException(e);
         }

      } finally {

         // on force ici la fermeture du contexte de Spring
         // ceci a pour but de forcer la déconnexion avec Cassandra, la SGBD
         // chargé de la persistance de la pile des travaux
         LOG.debug("execute - fermeture du contexte d'application");
         context.close();
         LOG.debug("execute - fermeture du contexte d'application effectuée");
      }

   }

   /**
    * Méthode appelée lors de l'exécution de la mise à jour du RND
    * 
    * @param args
    *           arguments de l'exécutable
    */
   public static void main(String[] args) {

      MajCorrespondancesMain instance = new MajCorrespondancesMain(
            "/applicationContext-sae-rnd-executable.xml");

      try {
         instance.execute(args);
      } catch (Exception e) {
         LOG
               .error(
                     "Une erreur a eu lieu dans le processus de mise à jour des correspondances du RND.",
                     e);
      }
   }

}
