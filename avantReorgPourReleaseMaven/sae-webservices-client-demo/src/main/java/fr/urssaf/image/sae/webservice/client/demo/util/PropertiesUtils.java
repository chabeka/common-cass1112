package fr.urssaf.image.sae.webservice.client.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilitaires de manipulation de {@link Properties}
 * 
 * 
 */
public final class PropertiesUtils {

   private PropertiesUtils() {

   }

   /**
    * instancie un objet {@link Properties} à partir d'un fichier de propriétés<br>
    * <br>
    * Cette méthode permet de récupérer des valeurs dans des
    * fichiers de paramètres de type .properties
    * 
    * @param input
    *           fichier de propriétés
    * @return instance de {@link Properties}
    */
   public static Properties load(InputStream input) {

      Properties properties = new Properties();
      try {
         properties.load(input);

      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }

      return properties;
   }
}
