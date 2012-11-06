package fr.urssaf.image.sae.ordonnanceur;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.ordonnanceur.commande.LancementTraitement;
import fr.urssaf.image.sae.ordonnanceur.commande.support.CommandeThreadPoolExecutor;
import fr.urssaf.image.sae.ordonnanceur.factory.OrdonnanceurContexteFactory;
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

   private final String configLocation;

   private CommandeThreadPoolExecutor commandePool;

   private ApplicationContext context;

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
      context = OrdonnanceurContexteFactory.creerContext(configLocation,
            saeConfiguration);

      commandePool = context.getBean(CommandeThreadPoolExecutor.class);

   }

   protected void launchTraitement() {

      LancementTraitement traitement = new LancementTraitement(context);

      try {

         commandePool.schedule(traitement, 0, TimeUnit.SECONDS);

      } catch (Exception e) {

         LOG.error("Erreur grave lors du traitement de l'ordonnanceur.", e);
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

      final OrdonnanceurMain instance = new OrdonnanceurMain(
            "/applicationContext-sae-ordonnanceur.xml");

      instance.loadOrdonnanceurApplicationContext(args);

      Runtime.getRuntime().addShutdownHook(new Thread() {
         @Override
         public void run() {
            LOG
                  .info("Une demande d'arrêt de l'ordonnanceur a été prise en compte");

            // on appel les différents Pool de Threads pour les arrêter

            // ici on appelle l'instance de ScheduledThreadPoolExecutor

            instance.commandePool.shutdown();

            instance.commandePool.waitFinish();

            LOG.info("L'ordonnanceur est arrêté.");

         }
      });

      instance.launchTraitement();

   }

}
