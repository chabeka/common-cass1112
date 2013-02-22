/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.bootstrap;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableRuntimeException;
import fr.urssaf.image.sae.trace.executable.factory.TraceContextFactory;
import fr.urssaf.image.sae.trace.executable.service.TraitementService;
import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Classe de lancement des traitements
 * 
 */
public final class Main {

   private static final int PURGE_ARGS_LENGTH = 3;
   private static final int CONVERSION_MINUTES = 60000;
   private static final String CONFIGURATION_FILE = "/applicationContext-sae-trace-executable.xml";
   private static final String HELP = "HELP";
   private static final String PURGE = "PURGE";

   private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

   private Main() {
   }

   /**
    * Méthode de lancement des traitements
    * 
    * @param args
    *           arguments de la ligne de commande
    * @throws Throwable
    *            exception levée lors du traitement
    */
   public static void main(String[] args) throws Throwable {

      MDC.put("log_contexte_uuid", UUID.randomUUID().toString());

      String prefix = "main()";
      LOGGER.debug("{} - début", prefix);
      LOGGER.info("{} - Arguments de la ligne de commande : {}", new Object[] {
            prefix, args });

      ApplicationContext context = null;

      try {

         long startDate = new Date().getTime();

         checkArguments(args);

         context = TraceContextFactory.loadContext(CONFIGURATION_FILE, args[0]);
         PurgeType purgeType = PurgeType.valueOf(args[2]);
         TraitementService service = context.getBean(TraitementService.class);
         service.purgerRegistre(purgeType);

         long endDate = new Date().getTime();
         long duree = (endDate - startDate) / CONVERSION_MINUTES;
         LOGGER.debug("{} - fin. Traitement réalisé en {} min", new Object[] {
               prefix, duree });

      } catch (Throwable ex) {

         LOGGER.error("Une erreur a eu lieu", ex);
         throw ex;

      } finally {
         // Fermeture du contexte d'application
         if (context != null) {
            ((ClassPathXmlApplicationContext) context).close();
         }
      }

   }

   @SuppressWarnings("PMD.PreserveStackTrace")
   private static void checkArguments(String[] args) {
      String prefix = "checkArguments()";
      LOGGER.debug("{} - début de vérification des arguments", prefix);

      // récupération de l'aide à la commande
      String helpCmd;
      try {

         ClassPathResource resource = new ClassPathResource(
               "manuel/_LISEZ_MOI.txt");
         File file = resource.getFile();
         helpCmd = FileUtils.readFileToString(file);

      } catch (IOException exception) {
         throw new TraceExecutableRuntimeException(exception);
      }

      if (ArrayUtils.isEmpty(args)) {
         throw new TraceExecutableRuntimeException(
               "la commande est incorrecte.\n" + helpCmd);
      }

      // demande d'aide dans la ligne de commande
      if (args.length == 1 && HELP.equalsIgnoreCase(args[0])) {
         throw new TraceExecutableRuntimeException(helpCmd);
      }

      if (args.length != PURGE_ARGS_LENGTH) {
         throw new TraceExecutableRuntimeException(
               "la commande est incorrecte.\n" + helpCmd);
      }

      // vérification du fichier de configuration SAE
      String chemin = args[0];
      File file = new File(chemin);
      if (!file.exists()) {
         throw new TraceExecutableRuntimeException(
               "le fichier de configuration du SAE est inexistant");
      }

      if (!args[1].equalsIgnoreCase(PURGE)) {
         throw new TraceExecutableRuntimeException(
               "la commande est incorrecte.\n" + helpCmd);
      }

      try {
         PurgeType.valueOf(args[2]);
      } catch (IllegalArgumentException exception) {
         throw new TraceExecutableRuntimeException(
               "le registre à purger n'est pas référencé.\n" + helpCmd);
      }

      LOGGER.debug("{} - Fin de la vérification des arguments", prefix);

   }

}
