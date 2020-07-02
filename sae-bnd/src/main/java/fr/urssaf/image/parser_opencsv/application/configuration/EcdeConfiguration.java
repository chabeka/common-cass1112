package fr.urssaf.image.parser_opencsv.application.configuration;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

@Configuration
public class EcdeConfiguration {

   @Value("${sae.ecde.cheminFichierConfig}")
   private String ecdeSourcePath;

   @Bean(name = "ecde_source")
   public EcdeSource getEcdeSoure() {
      XML xml;
      try {
         xml = new XMLDocument(new File(ecdeSourcePath));
      }
      catch (final FileNotFoundException e) {
         throw new RuntimeException(e);
      }
      final String ecdeName = xml.xpath("/sources/source/host/text()").get(0);
      final String ecdeMountPoint = xml.xpath("/sources/source/basePath/text()").get(0);

      final EcdeSource ecdeSource = new EcdeSource();
      ecdeSource.setBasePath(new File(ecdeMountPoint));
      ecdeSource.setHost(ecdeName);

      return ecdeSource;
   }

   @Bean(name = "ecde_sources")
   public EcdeSources getSources() {
      final EcdeSources sources = new EcdeSources();
      final EcdeSource[] ecdeSources = new EcdeSource[] {getEcdeSoure()};
      sources.setSources(ecdeSources);
      return sources;
   }

   @Bean
   static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {

      return new PropertySourcesPlaceholderConfigurer();
   }

}
