package fr.urssaf.image.sae.regionalisation;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.sae.regionalisation.factory.SAEApplicationContextFactory;
import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.util.ValidateUtils;

/**
 * Classe de lancement de la régionalisation
 * <ul>
 * <li><code>{0} : fichier de configuration connexion DFCE</code></li>
 * <li><code>{1} : fichier de configuration connexion POSTGRESQL</code></li>
 * <li><code>{2} : index de l'enregistrement de départ</code></li>
 * <li><code>{3} : nombre d'enregistrement à traiter</code></li>
 * <li><code>{4} : mode TIR_A_BLANC/MISE_A_JOUR</code></li>
 * </ul>
 * 
 */
public class BootStrap {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(BootStrap.class);

   private final String configLocation;

   private final AuthenticateSupport authenticationSupport;

   protected BootStrap(String configLocation,
         AuthenticateSupport authenticationSupport) {

      this.configLocation = configLocation;
      this.authenticationSupport = authenticationSupport;

   }

   private static final int DFCE_ARG_INDEX = 0;
   private static final int POSTGRESQL_ARG_INDEX = 1;
   private static final int FIRST_ARG_INDEX = 2;
   private static final int COUNT_ARG_INDEX = 3;
   private static final int MODE_ARG_INDEX = 4;

   private static final String MODE_0 = "TIR_A_BLANC";
   private static final String MODE_1 = "MISE_A_JOUR";

   private String dfceConfig;
   private String postgresqlConfig;
   private int firstRecord;
   private int processingCount;
   private boolean updateDatas;

   protected final void validate(String[] args) {

      // fichier de configuration connexion DFCE
      validate(
            args,
            DFCE_ARG_INDEX,
            "Le chemin complet du fichier de configuration connexion DFCE doit être renseigné.");

      dfceConfig = args[DFCE_ARG_INDEX];

      // fichier de configuration connexion POSTGRESQL
      validate(
            args,
            POSTGRESQL_ARG_INDEX,
            "Le chemin complet du fichier de configuration connexion POSTGRESQL doit être renseigné.");

      postgresqlConfig = args[POSTGRESQL_ARG_INDEX];

      // index de l'enregistrement de départ
      validate(args, FIRST_ARG_INDEX,
            "L'index de l'enregistrement de départ doit être renseigné.");

      try {

         firstRecord = Integer.valueOf(args[FIRST_ARG_INDEX]);

      } catch (NumberFormatException e) {

         throw new IllegalArgumentException(
               "L'index de l'enregistrement de départ doit être un nombre.", e);
      }

      // nombre d'enregistrement à traiter
      validate(args, COUNT_ARG_INDEX,
            "Le nombre d'enregistrement à traiter doit être renseigné.");

      try {

         processingCount = Integer.valueOf(args[COUNT_ARG_INDEX]);

      } catch (NumberFormatException e) {

         throw new IllegalArgumentException(
               "Le nombre d'enregistrement à traiter doit être un nombre.", e);
      }

      // mode TIR_A_BLANC/MISE_A_JOUR

      validate(args, MODE_ARG_INDEX,
            "Le mode TIR_A_BLANC/MISE_A_JOUR doit être renseigné.");

      if (!ArrayUtils.contains(new String[] { MODE_0, MODE_1 },
            args[MODE_ARG_INDEX])) {

         throw new IllegalArgumentException(
               "Le mode doit être TIR_A_BLANC ou MISE_A_JOUR.");
      }

      updateDatas = MODE_0.equals(args[MODE_ARG_INDEX]) ? false : true;

   }

   private static void validate(String[] args, int index,
         String messageException) {

      if (!ValidateUtils.isNotBlank(args, index)) {

         throw new IllegalArgumentException(messageException);

      }
   }

   protected final void execute(String[] args) {

      try {

         authenticationSupport.authenticate();

      } catch (Exception e) {

         LOGGER.error(e.getMessage());

         return;
      }

      try {

         this.validate(args);

      } catch (IllegalArgumentException e) {

         LOGGER.warn(e.getMessage());

         return;
      }

      // instanciation du contexte de SPRING
      ApplicationContext context = SAEApplicationContextFactory.load(
            configLocation, dfceConfig, postgresqlConfig);

      // appel du service ProcessingService
      ProcessingService service = context.getBean(ProcessingService.class);

      LOGGER
            .info(
                  "lancement de la régionalisation en mode {} avec l'index de départ {} et avec un nombre d'enregistrements à traiter de {}",
                  new Object[] { args[MODE_ARG_INDEX], args[FIRST_ARG_INDEX],
                        args[COUNT_ARG_INDEX] });

      // lancement de la régionalisation
      service.launch(updateDatas, firstRecord, processingCount);

      LOGGER.info("la régionalisation est terminée");
   }

   /**
    * 
    * 
    * @param args
    *           liste des paramètres d'entrée
    */
   public static void main(String[] args) {

      BootStrap booStrap = new BootStrap(
            "/applicationContext-sae-regionalisation.xml",
            new AuthenticateSupport());

      try {

         booStrap.execute(args);

      } catch (Exception e) {

         LOGGER.error("une erreur a eu lieu dans la régionalisation", e);
      }

   }

}
