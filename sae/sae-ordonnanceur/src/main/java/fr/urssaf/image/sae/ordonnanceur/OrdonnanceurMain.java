package fr.urssaf.image.sae.ordonnanceur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.ordonnanceur.exception.AucunJobALancerException;
import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.factory.OrdonnanceurContexteFactory;
import fr.urssaf.image.sae.ordonnanceur.model.OrdonnanceurConfiguration;
import fr.urssaf.image.sae.ordonnanceur.service.CoordinationService;
import fr.urssaf.image.sae.ordonnanceur.util.RandomUtils;
import fr.urssaf.image.sae.ordonnanceur.util.ValidateUtils;

/**
 * Classe de lancement de l'ordonnanceur
 * <ul>
 * <li><code>{0} : chemin du fichier de propriétés général</code></li>
 * </ul>
 * 
 */
public final class OrdonnanceurMain {

   private static final Logger LOG = LoggerFactory
         .getLogger(OrdonnanceurMain.class);

   private static final String PREFIX_LOG = "ordonnanceur()";

   private final String configLocation;

   private CoordinationService coordination;

   private int intervalle;

   private static final long INTERVAL = 1000;

   private static boolean stop = false;

   protected OrdonnanceurMain(String configLocation) {

      this.configLocation = configLocation;

   }

   protected void loadOrdonnanceurApplicationContext(String[] args) {

      // Vérification des paramètres d'entrée
      if (!ValidateUtils.isNotBlank(args, 0)) {
         throw new IllegalArgumentException(
               "Le chemin complet du fichier de configuration générale du SAE doit être renseigné.");
      }

      String saeConfiguration = args[0];

      // instanciation du contexte de SPRING
      ApplicationContext context = OrdonnanceurContexteFactory.creerContext(
            configLocation, saeConfiguration);

      try {
         coordination = context.getBean(CoordinationService.class);

         OrdonnanceurConfiguration configuration = context
               .getBean(OrdonnanceurConfiguration.class);
         intervalle = configuration.getIntervalle();

      } catch (Exception e) {

         LOG.warn("Erreur grave lors du traitement de l'ordonnanceur.", e);
         throw new OrdonnanceurRuntimeException(e);
      }

   }

   protected void launchTraitement() {

      Assert.notNull(coordination, "'coordination' is required");
      Assert.state(intervalle >= 1,
            "'intervalle' must be greater than or equal to 1.");

      try {

         coordination.lancerTraitement();

      } catch (AucunJobALancerException e) {

         LOG.debug("{} - il n'y a aucun traitement à lancer", PREFIX_LOG);

      } catch (Exception e) {

         LOG.warn("Erreur grave lors du traitement de l'ordonnanceur.", e);
      }

      int min = intervalle;
      int max = min * 2;
      int waitTime = RandomUtils.random(min, max);

      LOG
            .debug(
                  "{} - prochaine tentative de lancement d'un traitement dans {} secondes",
                  PREFIX_LOG, waitTime);
      try {
         Thread.sleep(waitTime * INTERVAL);
      } catch (InterruptedException e) {
         throw new OrdonnanceurRuntimeException(e);
      }

   }

   /**
    * méthode de lancement de l'ordonnnceur.
    * 
    * @param args
    *           arguments de l'exécutable
    */
   public static void main(String[] args) {

      LOG.info("Démarrage de l'ordonnanceur");

      OrdonnanceurMain instance = new OrdonnanceurMain(
            "/applicationContext-sae-ordonnanceur.xml");

      instance.loadOrdonnanceurApplicationContext(args);

      while (true) {

         instance.launchTraitement();

         // TODO commande d'arrêt propre de l'ordonnanceur
         if (stop) {
            break;
         }

      }

      LOG.info("Arrêt de l'ordonnanceur");

   }

}
