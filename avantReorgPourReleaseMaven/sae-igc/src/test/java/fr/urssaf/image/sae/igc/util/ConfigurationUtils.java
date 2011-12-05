package fr.urssaf.image.sae.igc.util;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Class utilitaire pour {@link XMLConfiguration}
 * 
 * 
 */
public final class ConfigurationUtils {

   private ConfigurationUtils() {

   }

   /**
    * 
    * @param path
    *           chemin du fichier
    * @return chemin absolu fdu fichier
    */
   public static String getAbsolute(String path) {

      String absolutePath = null;

      if (path != null) {
         absolutePath = new File(path).getAbsolutePath();
      }

      return absolutePath;
   }

   private static final String IGC_CONFIG = "src/test/resources/igcConfig/";

   /**
    * 
    * @param path
    *           chemin du fichier de configuration xml
    * @return instance de {@link XMLConfiguration}
    */
   public static XMLConfiguration createConfig(String path) {
      try {
         return new XMLConfiguration(getIgcConfig(path));
      } catch (ConfigurationException e) {
         throw new IllegalArgumentException(e);
      }
   }

   /**
    * 
    * 
    * @param file
    *           nom du fichier se trouvant dans {@value #IGC_CONFIG}
    * @return chemin absolu du fichier
    */
   public static String getIgcConfig(String file) {

      return new File(IGC_CONFIG + file).getAbsolutePath();
   }

}
