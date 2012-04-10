package fr.urssaf.image.sae.services.executable.capturemasse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.services.document.SAEBulkCaptureService;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeWriteFileEx;
import fr.urssaf.image.sae.services.executable.capturemasse.exception.CaptureMasseMainException;
import fr.urssaf.image.sae.services.executable.factory.SAEApplicationContextFactory;
import fr.urssaf.image.sae.services.executable.util.ValidateUtils;

/**
 * Exécutable d'un traitement de capture en masse pour les arguments suivants :
 * <ul>
 * <li><code>{0} : URL ECDE du sommaire.xml</code></li>
 * <li>
 * <code>{1} : chemin absolu du fichier de configuration globale du SAE</code></li>
 * <li><code>{2} : UUID du contexte LOGBACK</code></li>
 * </ul>
 * Tous les arguments sont obligatoires.<br>
 * 
 */
@Deprecated
public final class CaptureMasseMain {

   private static final Logger LOG = LoggerFactory
         .getLogger(CaptureMasseMain.class);

   private final String configLocation;

   protected CaptureMasseMain(String configLocation) {

      this.configLocation = configLocation;
   }

   protected void captureMasse(String args[]) {

      // Vérification des paramètres d'entrée
      if (!ValidateUtils.isNotBlank(args, 0)) {
         throw new IllegalArgumentException(
               "L'URL ECDE du fichier sommaire.xml décrivant le traitement de capture en masse doit être renseigné.");
      }

      if (!ValidateUtils.isNotBlank(args, 1)) {
         throw new IllegalArgumentException(
               "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
      }

      if (!ValidateUtils.isNotBlank(args, 2)) {
         throw new IllegalArgumentException(
               "L'identifiant du contexte de log doit être renseigné.");
      }

      String sommaire = args[0];
      String saeConfiguration = args[1];
      String contexteLog = args[2];

      // initialisation du contexte du LOGBACK
      MDC.put("log_contexte_uuid", contexteLog);

      // instanciation du contexte de SPRING
      ApplicationContext context = SAEApplicationContextFactory
            .createSAEApplicationContext(configLocation, saeConfiguration);

      // appel du service de capture en masse
      SAEBulkCaptureService bulkCapture = context
            .getBean(SAEBulkCaptureService.class);

      try {

         bulkCapture.bulkCapture(sommaire);

      } catch (CaptureBadEcdeUrlEx e) {

         throw new CaptureMasseMainException(e);

      } catch (CaptureEcdeUrlFileNotFoundEx e) {

         throw new CaptureMasseMainException(e);

      } catch (CaptureEcdeWriteFileEx e) {

         throw new CaptureMasseMainException(e);

      }

   }

   /**
    * Méthode appelée lors de l'exécution du traitement
    * 
    * @param args
    *           arguments de l'exécutable
    */
   public static void main(String[] args) {

      CaptureMasseMain instance = new CaptureMasseMain(
            "/applicationContext-sae-services-executable.xml");

      try {
         instance.captureMasse(args);
      } catch (Exception e) {
         LOG
               .error(
                     "Une erreur a eu lieu dans le processus du traitement de capture en masse.",
                     e);
      }
   }

}
