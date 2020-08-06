package fr.urssaf.image.parser_opencsv.application.configuration;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;

import fr.urssaf.image.parser_opencsv.application.exception.BNDScriptRuntimeException;
import fr.urssaf.image.parser_opencsv.webservice.factory.AddViHeaderHandlerResolver;
import fr.urssaf.image.parser_opencsv.webservice.factory.SaeServiceStubFactory;
import fr.urssaf.image.parser_opencsv.webservice.model.SaeService;
import fr.urssaf.image.parser_opencsv.webservice.model.SaeServicePortType;

@Configuration

@ImportResource({
   "classpath:/applicationContext-sae-ecde.xml",
   "classpath:/applicationContext-commons-cassandra.xml",
   "classpath:/applicationContext-sae-format.xml",
   "classpath:/applicationContext-commons-dfce-connection.xml",
   "classpath:/applicationContext-sae-storage-dfce.xml",
   "classpath:/applicationContext-sae-droit-controles.xml",
   "classpath:/applicationContext-sae-droit-permit-all.xml",
   "classpath:/applicationContext-sae-droit.xml"

})
@PropertySource("file:${sae.config.directory}/sae-config.properties")
@PropertySource("file:${sae.cassandra.cheminFichierConfig}")
@ComponentScan(basePackages = "fr.urssaf.image.sae")
public class SaeConfiguration {

   private static final Logger LOGGER = LoggerFactory.getLogger(SaeConfiguration.class);

   @Value("${sae.ws.endpoint}")
   private String endPoint;

   @Value("${sae.ws.passphrase}")
   private String passphrase;

   @Value("${sae.ws.pagms}")
   private String pagms;

   @Value("${sae.ws.contrat.services}")
   private String contratService;

   @Value("${sae.ws.private.key}")
   private String privateKeyFile;

   @Value("${sae.config.directory}")
   private String configPath;

   @Bean
   public SaeServicePortType getSaeServiceStub() {
      final SaeServiceStubFactory saeServiceStubFactory = new SaeServiceStubFactory(
            endPoint,
            new AddViHeaderHandlerResolver(privateKeyFile,
                  passphrase,
                  pagms,
                  contratService));
      SaeService saeService;
      try {
         saeService = saeServiceStubFactory.createStubAvecAuthentification();
      }
      catch (final IOException e) {
         final String message = "Erreur lors de la creation du stub du webservice SAE avec authentification";
         LOGGER.info(message);
         LOGGER.info("Détails : {}", e.getMessage());
         throw new BNDScriptRuntimeException(message, e);
      }

      return saeService.getSaeServicePort();
   }

   @Bean(name = "saeConfigResource")
   public FileSystemResource getSaeConfigBean() {
      final String globalSaeConfigFile = configPath + "/sae-config.properties";
      return new FileSystemResource(globalSaeConfigFile);
   }
}
