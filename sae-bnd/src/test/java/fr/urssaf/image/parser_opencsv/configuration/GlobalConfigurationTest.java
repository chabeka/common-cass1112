package fr.urssaf.image.parser_opencsv.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import fr.urssaf.image.parser_opencsv.application.component.BndMigrationComponent;
import fr.urssaf.image.parser_opencsv.application.reader.BndCsvReaderBuilder;
import fr.urssaf.image.parser_opencsv.application.writer.SommaireWriter;

@Configuration
@PropertySource("classpath:test/app-test.properties")
public class GlobalConfigurationTest {

   @Bean(name = "bnd_csv_reader_builder")
   public BndCsvReaderBuilder getCSVReaderBuilder() {

      return new BndCsvReaderBuilder();
   }

   @Bean(name = "bnd_migration_script")
   public BndMigrationComponent getBndScript() {
      return new BndMigrationComponent();
   }

   @Bean(name = "bnd_sommaire_writer")
   public SommaireWriter getSommaireWriter(@Value("${bnd.sommaire.path}") final String sommairePath) throws IOException {
      return new SommaireWriter(sommairePath);
   }
}
