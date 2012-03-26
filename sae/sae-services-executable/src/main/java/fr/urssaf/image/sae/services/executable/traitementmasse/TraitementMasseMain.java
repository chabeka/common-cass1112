package fr.urssaf.image.sae.services.executable.traitementmasse;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;
import fr.urssaf.image.sae.services.executable.factory.SAEApplicationContextFactory;
import fr.urssaf.image.sae.services.executable.traitementmasse.exception.TraitementMasseMainException;
import fr.urssaf.image.sae.services.executable.util.ValidateUtils;

/**
 * Exécutable d'un traitement de masse pour les arguments suivants :
 * <ul>
 * <li><code>{0} : identifiant du traitement de masse</code></li>
 * <li>
 * <code>{1} : chemin absolu du fichier de configuration globale du SAE</code></li>
 * </ul>
 * Tous les arguments sont obligatoires.<br>
 * 
 */
public class TraitementMasseMain {

   private static final Logger LOG = LoggerFactory
         .getLogger(TraitementMasseMain.class);

   private final String configLocation;

   protected TraitementMasseMain(String configLocation) {

      this.configLocation = configLocation;
   }

   protected final void execute(String args[]) {

      // Vérification des paramètres d'entrée
      if (!ValidateUtils.isNotBlank(args, 0)) {
         throw new IllegalArgumentException(
               "L'identifiant du traitement de masse doit être renseigné.");
      }

      if (!ValidateUtils.isNotBlank(args, 1)) {
         throw new IllegalArgumentException(
               "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
      }

      UUID idJob = UUID.fromString(args[0]);
      String saeConfiguration = args[1];
      String contexteLog = args[0];

      // initialisation du contexte du LOGBACK
      MDC.put("log_contexte_uuid", contexteLog);

      // instanciation du contexte de SPRING
      ApplicationContext context = SAEApplicationContextFactory
            .createSAEApplicationContext(configLocation, saeConfiguration);

      // appel du service de traitement de masse
      TraitementAsynchroneService traitementService = context
            .getBean(TraitementAsynchroneService.class);

      try {

         traitementService.lancerJob(idJob);

      } catch (JobInexistantException e) {

         throw new TraitementMasseMainException(e);

      } catch (JobNonReserveException e) {

         throw new TraitementMasseMainException(e);
      }

   }

   /**
    * Méthode appelée lors de l'exécution du traitement
    * 
    * @param args
    *           arguments de l'exécutable
    */
   public static void main(String[] args) {

      TraitementMasseMain instance = new TraitementMasseMain(
            "/applicationContext-sae-services-executable.xml");

      try {
         instance.execute(args);
      } catch (Exception e) {
         LOG
               .error(
                     "Une erreur a eu lieu dans le processus du traitement de masse.",
                     e);
      }
   }
}
