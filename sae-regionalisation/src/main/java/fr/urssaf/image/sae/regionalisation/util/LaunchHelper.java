/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * Classe regroupant les fonctions utilitaires pour le lancement du traitement
 * 
 */
public final class LaunchHelper {

   /**
    * constructeur
    */
   private LaunchHelper() {
   }

   /**
    * @return le contenu du readme
    */
   public static String getReadme() {

      ClassPathResource resource = new ClassPathResource("readme.txt");
      String message;

      try {
         InputStream stream = resource.getInputStream();
         message = IOUtils.toString(stream);
      } catch (IOException e) {
         message = "Impossible de récupérer le readme";
      }

      return message;
   }

}