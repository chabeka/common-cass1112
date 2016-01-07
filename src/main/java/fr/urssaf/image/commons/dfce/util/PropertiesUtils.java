package fr.urssaf.image.commons.dfce.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * Classe utilitaire pour les objets de type {@link Properties}
 * 
 */
public final class PropertiesUtils {

   private PropertiesUtils() {

   }

   /**
    * Instancie un objet {@link Properties} à partir du chemin complet du
    * fichier .properties
    * 
    * @param propertiesFile
    *           fichier properties
    * @return objet {@link Properties}
    * @throws IOException
    *            exception levée lors de la lecture ou du chargement du fichier
    *            .properties
    */
   public static Properties load(File propertiesFile) throws IOException {

      FileInputStream input = FileUtils.openInputStream(propertiesFile);

      return load(input);

   }

   /**
    * Instancie un objet {@link Properties} à partir d'un InputStream pointant
    * sur un fichier properties
    * 
    * @param propertiesStream
    *           le stream pointant fichier properties
    * @return objet {@link Properties}
    * @throws IOException
    *            exception levée lors de la lecture ou du chargement du fichier
    *            .properties
    */
   public static Properties load(InputStream propertiesStream)
         throws IOException {

      Properties properties = new Properties();

      properties.load(propertiesStream);

      return properties;
   }

}
