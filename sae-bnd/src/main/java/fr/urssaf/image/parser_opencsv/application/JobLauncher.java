package fr.urssaf.image.parser_opencsv.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.parser_opencsv.application.configuration.GlobalConfiguration;
import fr.urssaf.image.parser_opencsv.application.exception.BNDScriptRuntimeException;
import fr.urssaf.image.parser_opencsv.application.job.Worker;
import fr.urssaf.image.parser_opencsv.application.service.impl.AsynchronousService;

/**
 * Classe de lancement du Script BND
 */
@Component
@Scope("prototype")
public class JobLauncher {

   @Value("${bnd.source.path}")
   private String sourcePath;

   /**
    * Nombre de traitement de csv terminés
    */
   public static AtomicInteger nombreTraitementsTerminee = new AtomicInteger(0);

   private static final Logger LOGGER = LoggerFactory.getLogger(JobLauncher.class);

   /**
    * @param args
    * @throws Exception
    */
   public static void main(final String[] args) throws Exception {
      final Class<?> clazz = Class.forName("org.postgresql.Driver");
      LOGGER.info("Classe org.postgresql.Driver bien trouvée {} ", clazz);

      final GenericApplicationContext context = new AnnotationConfigApplicationContext(GlobalConfiguration.class);
      final JobLauncher jobLauncher = context.getBean(JobLauncher.class);
      jobLauncher.launchScript(context);
   }

   /**
    * Lancement des Jobs
    * 
    * @param context
    */
   public void launchScript(final GenericApplicationContext context) {
      final List<String> csvs = getAllCSVFileNames();
      final AsynchronousService executor = context.getBean(AsynchronousService.class);
      executor.setNbreDeWorker(csvs.size());

      // Correspond à l'id du job commun aux traitements de masse qui seront crées
      final String jobUUID = UUID.randomUUID().toString();
      // Lancement des Workers
      csvs.forEach(fileName -> {
         final Worker worker = context.getBean(Worker.class, jobUUID, fileName);
         LOGGER.info("Traitement du fichier ---> {}", fileName);
         executor.executeAsyncTask(worker);
      });
   }

   /**
    * Récupère la listes de noms des fichiers CSV à traiter
    * 
    * @return
    */
   public List<String> getAllCSVFileNames() {
      try (Stream<Path> walk = Files.walk(Paths.get(sourcePath), 1)) {
         final List<String> result = walk.filter(Files::isRegularFile)
               .map(x -> x.toFile().getName())
               .filter(f -> f.contains(".csv"))
               .collect(Collectors.toList());

         return result;
      }
      catch (final IOException e) {
         throw new BNDScriptRuntimeException("Une erreur est survenue lors de la récupération des CSV", e);
      }
   }

}
