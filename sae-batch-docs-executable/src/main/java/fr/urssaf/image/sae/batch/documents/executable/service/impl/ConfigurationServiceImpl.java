package fr.urssaf.image.sae.batch.documents.executable.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationEnvironnement;
import fr.urssaf.image.sae.batch.documents.executable.model.ConfigurationsEnvironnement;

/**
 * Classe implémentant le service ConfigurationService
 * 
 * 
 */
public class ConfigurationServiceImpl {

   /**
    * Renvoie la liste de toutes les configurations des environnements
    * (CASSANDRA et services Web)
    * 
    * @param fichier
    *           Fichier contenant le liste des configurations environnement
    * 
    * @return Liste de toutes les configurations environnement 
    * 
    * @throws IOException
    *            Exception générée si le fichier n'est pas lisible
    * 
    */
   public final ConfigurationsEnvironnement chargerConfiguration(File fichier)
         throws IOException {
      return chargerConfiguration(new FileInputStream(fichier));
   }
   
   public final ConfigurationsEnvironnement chargerConfiguration(InputStream fileStream)
   throws IOException {
      //-- Désérialisation des objets EcdeSource via Xstream
      StaxDriver staxDriver = new StaxDriver();
      XStream xstream = new XStream(staxDriver);

      xstream.registerConverter(new AbstractSingleValueConverter() {
         /**
          * Conversion
          */
         @Override
         public boolean canConvert(Class type) {
            return type == URI.class;
         }
         /**
          * Conversion
          */
         @Override
         public Object fromString(String str) {
            URI uri;
            try {
               uri = new URI(str);
            } catch (URISyntaxException e) {
               uri = null;
            }
            return uri;
         }
         /**
          * Conversion
          */
         public String toString(Object obj) {
            return ((URI) obj).toString();
         }
      });

      xstream.alias("parametre", String.class);
      xstream.alias("configuration", ConfigurationEnvironnement.class);
      xstream.alias("configurations", new ConfigurationEnvironnement[] {}.getClass());
      ConfigurationsEnvironnement confs = new ConfigurationsEnvironnement();
      confs.setConfigurations((ConfigurationEnvironnement[]) xstream.fromXML(fileStream));

      return confs;
   }
}
