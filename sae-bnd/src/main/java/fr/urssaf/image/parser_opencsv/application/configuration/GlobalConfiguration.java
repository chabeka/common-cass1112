package fr.urssaf.image.parser_opencsv.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import fr.urssaf.image.parser_opencsv.application.reader.BndCsvReaderBuilder;
import fr.urssaf.image.parser_opencsv.application.reader.correspondance.CorrespondanceReaderBuilder;

@Configuration
@PropertySource(value = "${external.properties}")
// @PropertySource(value = "${external.properties}", ignoreResourceNotFound = true)
@PropertySource("file:${bnd.config.path}/sae-webservice-security.properties")
@ComponentScan(basePackages = "fr.urssaf.image.parser_opencsv.application")
@Import({EcdeConfiguration.class, SaeConfiguration.class, JobConfiguration.class, PersistenceJPAConfig.class})
public class GlobalConfiguration {

   private static final String CORRESPONDANCE_CAISSE_FILE = "src/main/resources/correspondances/correspondance_caisse_ssti.csv";

   private static final String CORRESPONDANCE_RND_FILE = "src/main/resources/correspondances/correspondance_rnd_ssti.csv";

   /**
    * Bean Builder du reader du fichier CSV fourni par SSTI
    * 
    * @return
    */
   @Bean(name = "bnd_csv_reader_builder")
   @Scope("prototype")
   public BndCsvReaderBuilder getCSVReaderBuilder() {

      return new BndCsvReaderBuilder();
   }

   /**
    * Bean de lecture du fichier CSV de correspondance en caisse TI et Organisme propriétaire GED
    * 
    * @return
    */
   @Bean(name = "reader_correspondance_caisse")
   public CorrespondanceReaderBuilder getMatcherCorrespCaisse() {
      return new CorrespondanceReaderBuilder(CORRESPONDANCE_CAISSE_FILE);
   }

   @Bean(name = "reader_correspondance_rnd")
   public CorrespondanceReaderBuilder getMatcherCorrespRnd() {
      return new CorrespondanceReaderBuilder(CORRESPONDANCE_RND_FILE);
   }

}
